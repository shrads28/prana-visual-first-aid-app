package com.example.ui

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.PranaApplication
import com.example.data.entity.MedicalProfile
import com.example.data.entity.OfflinePack
import com.example.data.entity.TreatmentHistory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

// Simple imports correction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.media.MediaPlayer

class PranaViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository = (application as PranaApplication).repository

    // --- Navigation State ---
    val currentScreen = MutableStateFlow("splash") // splash, language_select, permissions, login, main
    val currentTab = MutableStateFlow("home") // home, ar, emergency, learn, profile
    val isDrawerOpen = MutableStateFlow(false)

    // --- Active Language State ---
    val preferredLanguage = MutableStateFlow("en") // "en" or "hi"
    val preferredVoice = MutableStateFlow("female") // "female" or "male"

    // --- UI State Flows from Room ---
    val medicalProfile: StateFlow<MedicalProfile?> = repository.medicalProfile.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val treatmentHistories: StateFlow<List<TreatmentHistory>> = repository.allTreatmentHistories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val offlinePacks: StateFlow<List<OfflinePack>> = repository.allOfflinePacks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Local TTS System ---
    private var tts: TextToSpeech? = null
    private val isTtsReady = MutableStateFlow(false)
    val isSpeaking = MutableStateFlow(false)

    // --- Camera & Emergency Tools ---
    private var flashlightJob: Job? = null
    val isFlashlightSosActive = MutableStateFlow(false)
    val isSirenActive = MutableStateFlow(false)
    private var toneGenerator: ToneGenerator? = null
    private var sirenJob: Job? = null

    // --- Active AR First-Aid Session State ---
    val activeInjury = MutableStateFlow<String?>(null) // cuts, burns, cpr, choking, fracture, snakebite, bleeding
    val activeStep = MutableStateFlow(0)
    val arIsPlaying = MutableStateFlow(true)
    val showGestureRecognitionTip = MutableStateFlow(true)
    val complianceState = MutableStateFlow<String>("pending") // pending, success, struggling, critical

    // --- Dynamic AI-Guided and Visual Analysis States ---
    val isDynamicAiMode = MutableStateFlow(false)
    val aiWoundDiagnosis = MutableStateFlow<String?>(null)
    val aiSteps = MutableStateFlow<List<String>>(emptyList())
    val aiIsAnalyzing = MutableStateFlow(false)
    val aiVoiceFeedback = MutableStateFlow<String>("")

    // --- Metronome CPR count ---
    val cprBpmCount = MutableStateFlow(0)
    private var cprJob: Job? = null

    // --- Live Location Mock State ---
    val currentCoordinates = MutableStateFlow("12.9716° N, 77.5946° E") // Default location (e.g., Bengaluru, India)

    init {
        // Initialize TextToSpeech
        tts = TextToSpeech(application, this)
        toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)

        // Observe medical profile changes to sync preferred language
        viewModelScope.launch {
            repository.medicalProfile.collect { profile ->
                if (profile != null) {
                    preferredLanguage.value = profile.preferredLanguage
                    preferredVoice.value = profile.preferredVoice
                    setTtsLanguage(profile.preferredLanguage)
                } else {
                    // Seed initial default medical profile
                    repository.saveMedicalProfile(MedicalProfile())
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsReady.value = true
            setTtsLanguage(preferredLanguage.value)
            Log.d("PranaViewModel", "TTS Initialized Successfully")
        } else {
            Log.e("PranaViewModel", "TTS Initialization Failed")
        }
    }

    private fun setTtsLanguage(lang: String) {
        val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
        tts?.language = locale
        // If voice adjustments are supported
        try {
            val voices = tts?.voices
            if (voices != null) {
                val voiceToSet = voices.find {
                    it.locale.language == locale.language &&
                    (if (preferredVoice.value == "female") it.name.contains("female", ignoreCase = true) else it.name.contains("male", ignoreCase = true))
                }
                if (voiceToSet != null) {
                    tts?.voice = voiceToSet
                }
            }
        } catch (e: Exception) {
            Log.e("PranaViewModel", "Error adjusting TTS voice parameter: ${e.message}")
        }
    }

    fun speak(text: String) {
        if (!isTtsReady.value) return
        viewModelScope.launch {
            isSpeaking.value = true
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "PranaGuidance")
            // Periodically check if speaking has finished
            while (tts?.isSpeaking == true) {
                delay(100)
            }
            isSpeaking.value = false
        }
    }

    fun stopSpeaking() {
        tts?.stop()
        isSpeaking.value = false
    }

    // --- Language Change ---
    fun changeLanguage(lang: String) {
        preferredLanguage.value = lang
        setTtsLanguage(lang)
        viewModelScope.launch {
            val current = repository.getMedicalProfileDirect() ?: MedicalProfile()
            repository.saveMedicalProfile(current.copy(preferredLanguage = lang))
        }
    }

    fun changeVoice(voice: String) {
        preferredVoice.value = voice
        viewModelScope.launch {
            val current = repository.getMedicalProfileDirect() ?: MedicalProfile()
            repository.saveMedicalProfile(current.copy(preferredVoice = voice))
        }
    }

    // --- Profile & Contact management ---
    fun updateMedicalProfile(profile: MedicalProfile) {
        viewModelScope.launch {
            repository.saveMedicalProfile(profile)
        }
    }

    // --- Treatment History ---
    fun logTreatment(injuryId: String, duration: Int, steps: Int, totalSteps: Int) {
        viewModelScope.launch {
            val history = TreatmentHistory(
                injury = injuryId,
                durationSeconds = duration,
                stepsCompleted = steps,
                totalSteps = totalSteps,
                voiceLanguage = preferredLanguage.value
            )
            repository.saveTreatmentHistory(history)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // --- Offline Pack download simulator ---
    fun downloadOfflinePack(packId: String) {
        viewModelScope.launch {
            repository.updateDownloadStatus(packId, 0.1f, false, null)
            for (i in 1..10) {
                delay(300)
                repository.updateDownloadStatus(packId, i * 0.1f, false, null)
            }
            val localPath = "offline_video_$packId.mp4"
            repository.updateDownloadStatus(packId, 1.0f, true, localPath)
        }
    }

    fun removeOfflinePack(packId: String) {
        viewModelScope.launch {
            repository.updateDownloadStatus(packId, 0f, false, null)
        }
    }

    // --- Flashlight SOS Mode ---
    fun toggleFlashlightSos() {
        val cameraManager = getApplication<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = try { cameraManager.cameraIdList[0] } catch (e: Exception) { null }

        if (isFlashlightSosActive.value) {
            flashlightJob?.cancel()
            isFlashlightSosActive.value = false
            if (cameraId != null) {
                try { cameraManager.setTorchMode(cameraId, false) } catch (e: Exception) {}
            }
        } else {
            isFlashlightSosActive.value = true
            if (cameraId != null) {
                flashlightJob = viewModelScope.launch {
                    var isOn = false
                    while (isFlashlightSosActive.value) {
                        isOn = !isOn
                        try {
                            cameraManager.setTorchMode(cameraId, isOn)
                        } catch (e: Exception) {
                            Log.e("PranaViewModel", "SOS Torch Error: ${e.message}")
                        }
                        delay(if (isOn) 200 else 400) // SOS pulse rate
                    }
                }
            }
        }
    }

    // --- Reassuring Siren Alarm ---
    fun toggleSiren() {
        if (isSirenActive.value) {
            sirenJob?.cancel()
            isSirenActive.value = false
        } else {
            isSirenActive.value = true
            sirenJob = viewModelScope.launch {
                while (isSirenActive.value) {
                    // Play a oscillating siren sound using ToneGenerator
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500)
                    delay(600)
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 500)
                    delay(600)
                }
            }
        }
    }

    // --- AR First-Aid Navigation & TTS synchronization ---
    fun startFirstAidSession(injuryId: String) {
        activeInjury.value = injuryId
        activeStep.value = 0
        arIsPlaying.value = true
        complianceState.value = "pending"
        currentTab.value = "ar" // switch bottom tab automatically

        if (injuryId == "cpr") {
            startCprMetronome()
        } else {
            stopCprMetronome()
        }

        speakStepInstruction()
    }

    fun endFirstAidSession() {
        val id = activeInjury.value
        val steps = activeStep.value + 1
        val maxSteps = getActiveInjuryTotalSteps()
        if (id != null) {
            logTreatment(id, 45, steps, maxSteps)
        }

        activeInjury.value = null
        activeStep.value = 0
        complianceState.value = "pending"
        isDynamicAiMode.value = false
        aiWoundDiagnosis.value = null
        aiSteps.value = emptyList()
        aiIsAnalyzing.value = false
        aiVoiceFeedback.value = ""
        stopCprMetronome()
        stopSpeaking()
    }

    fun nextArStep() {
        val total = getActiveInjuryTotalSteps()
        complianceState.value = "pending"
        if (activeStep.value < total - 1) {
            activeStep.value += 1
            speakStepInstruction()
        } else {
            // Finished Treatment!
            endFirstAidSession()
        }
    }

    fun prevArStep() {
        complianceState.value = "pending"
        if (activeStep.value > 0) {
            activeStep.value -= 1
            speakStepInstruction()
        }
    }

    fun pauseArSession() {
        arIsPlaying.value = !arIsPlaying.value
        if (!arIsPlaying.value) {
            stopSpeaking()
        } else {
            speakStepInstruction()
        }
    }

    fun repeatArVoice() {
        speakStepInstruction()
    }

    fun triggerGestureSimulation(gesture: String) {
        // Thumbs Up -> Next Step
        // Palm -> Pause
        // Point -> Replay Step
        // Pinch -> Repeat Voice
        // Open Hand -> Go Back (End Session)
        when (gesture) {
            "thumbs_up" -> {
                speakNotification("Thumbs Up gesture recognized. Moving to next step.")
                nextArStep()
            }
            "palm" -> {
                speakNotification("Palm gesture recognized. Pausing session.")
                pauseArSession()
            }
            "point" -> {
                speakNotification("Point gesture recognized. Replaying current step.")
                activeStep.value = 0
                speakStepInstruction()
            }
            "pinch" -> {
                speakNotification("Pinch gesture recognized. Repeating voice narration.")
                repeatArVoice()
            }
            "open_hand" -> {
                speakNotification("Open Hand gesture recognized. Ending session.")
                endFirstAidSession()
            }
        }
    }

    private fun speakNotification(msg: String) {
        if (!isTtsReady.value) return
        tts?.speak(msg, TextToSpeech.QUEUE_ADD, null, "GestureNotice")
    }

    fun speakStepInstruction() {
        val injury = activeInjury.value ?: return
        val step = activeStep.value
        val isHindi = preferredLanguage.value == "hi"

        val instructionText = getStepText(injury, step, isHindi)
        speak(instructionText)
    }

    fun getStepText(injury: String, step: Int, isHindi: Boolean): String {
        if (isDynamicAiMode.value && aiSteps.value.isNotEmpty() && step < aiSteps.value.size) {
            return aiSteps.value[step]
        }
        return when (injury) {
            "burns" -> when (step) {
                0 -> if (isHindi) "मैं जले हुए हिस्से को देख पा रही हूँ। सबसे पहले, आइए इसे धीरे से ठंडा करें। कृपया चोटिल हिस्से को ठंडे बहते पानी के नीचे रखें—मैं आपके साथ हूँ।" else "I see the burn. First, let's gently cool it down. Please place the injury under cool, gently running water right now—I am right here with you."
                1 -> if (isHindi) "बहुत बढ़िया। इसे ठंडे पानी की धार के नीचे ही रखें। बहते पानी को लगातार बहने दें, यह त्वचा के गहरे तापमान को सुरक्षित रूप से शांत कर रहा है।" else "Excellent. Keep holding it there under the cool stream. Let the running water continuously soothe and reduce the deep temperature of the skin."
                2 -> if (isHindi) "आप बहुत अच्छे से संभाल रहे हैं। एक छोटी सी सलाह: कृपया घाव पर टूथपेस्ट, मक्खन या सीधी बर्फ कभी न लगाएं, क्योंकि ये त्वचा की गर्मी को अंदर ही रोक देते हैं।" else "You're doing wonderfully. Just a gentle reminder: please do not apply toothpaste, butter, or ice directly, as they can lock in the heat."
                3 -> if (isHindi) "अब, आइए संवेदनशील त्वचा की रक्षा करें। एक साफ, सूखे सूती कपड़े या पट्टी से घाव को बिना दबाए धीरे से ढकें। आप बिल्कुल सुरक्षित हैं।" else "Now, let's protect the delicate skin. Gently cover the area with a clean, dry cotton cloth or sterile wrap without pressing. You are doing great."
                else -> ""
            }
            "cuts" -> when (step) {
                0 -> if (isHindi) "घबराएं नहीं, मैं मदद के लिए तैयार हूँ। सबसे पहले, एक साफ कपड़े से घाव पर सीधा और हल्का दबाव बनाएं।" else "Take a deep breath, I'm here to help. First, let's apply direct, gentle but firm pressure on the cut using a clean cloth to slow things down."
                1 -> if (isHindi) "बहुत अच्छे। दबाव लगातार बनाए रखें जब तक कि रक्त का बहना पूरी तरह से रुक न जाए। शांत रहें, सब ठीक हो जाएगा।" else "Excellent. Keep holding that steady pressure until the bleeding completely stops. Stay calm, we are getting this under control."
                2 -> if (isHindi) "अब दबाव को धीरे से कम करें और घाव को साफ गुनगुने या नल के पानी से धो लें ताकि धूल हट सके।" else "Now, let's gently ease off and rinse the area with clean water to make sure it is completely clean and clear of any dirt."
                3 -> if (isHindi) "शानदार! अब थोड़ा सा एंटीसेप्टिक या साफ क्रीम लगाएं और संक्रमण से बचाने के लिए एक साफ पट्टी बांधें।" else "Wonderful! To finish, let's apply a thin layer of antiseptic cream and secure a clean bandage. You did an amazing job."
                else -> ""
            }
            "cpr" -> when (step) {
                0 -> if (isHindi) "कृपया शांत रहें और ध्यान केंद्रित करें। व्यक्ति को पीठ के बल लिटाएं, और अपनी हथेलियों को उनकी छाती के बिल्कुल बीच में रखें।" else "Stay calm, stay focused, we can do this together. Lay them flat, and place the heels of your hands right in the center of their chest."
                1 -> if (isHindi) "अपनी कोहनियों को सीधा लॉक करें! मेरे बीप की लय के साथ तालमेल बिठाएं और छाती को तेज़ी से और ज़ोर से दबाना शुरू करें।" else "Lock your elbows straight! Match the rhythm of my metronome beep and push hard and fast, keeping a steady, powerful pace."
                2 -> if (isHindi) "बहुत बढ़िया। प्रत्येक दबाव के बाद छाती को पूरी तरह से ऊपर आने दें ताकि दिल को वापस पंप करने का समय मिले।" else "Perfect. Make sure you let the chest rise fully after each compression so the heart has room to fill back up."
                3 -> if (isHindi) "अब, प्रत्येक तीस दबावों के बाद, उनका सिर पीछे झुकाएं और उन्हें दो बार गहरी सांसें दें। फिर तुरंत दबाना शुरू करें।" else "Now, after every thirty compressions, pinch their nose, tilt their head back, and deliver two gentle rescue breaths. Keep going, you are saving a life."
                else -> ""
            }
            "choking" -> when (step) {
                0 -> if (isHindi) "मैं आपके साथ हूँ, घबराएं नहीं। तुरंत व्यक्ति के ठीक पीछे खड़े हो जाएं और उन्हें थोड़ा आगे की ओर झुकाएं।" else "I am right here with you. Stand immediately behind them and support their chest as you lean them forward."
                1 -> if (isHindi) "अपनी हथेली के पिछले हिस्से का उपयोग करके, उनकी दोनों कंधों की हड्डियों के बीच में पांच बार तेज़ और सूखी थपकी दें।" else "Using the heel of your hand, deliver five sharp, firm back blows right between their shoulder blades to dislodge the object."
                2 -> if (isHindi) "बहुत अच्छे। अब अपने दोनों हाथों को उनके पेट के ऊपरी हिस्से पर बांधें और अंदर और ऊपर की ओर पांच बार तेज़ी से झटका दें।" else "Great. Now wrap your arms around their upper abdomen and perform five quick, upward abdominal thrusts. Push with purpose."
                3 -> if (isHindi) "जब तक हवा का मार्ग पूरी तरह से साफ नहीं हो जाता, तब तक इन पीठ की थपकियों और पेट के झटकों को लगातार दोहराते रहें।" else "You are doing incredibly well. Keep alternating five back blows and five abdominal thrusts until their airway is completely clear."
                else -> ""
            }
            "fracture" -> when (step) {
                0 -> if (isHindi) "कृपया चोटिल अंग को हिलाने से बचें। उन्हें आरामदायक स्थिति में बिठाएं और अंग को पूरी तरह से स्थिर रखें।" else "Let's keep the injured limb completely still to prevent further discomfort. Reassure them and support the limb in place."
                1 -> if (isHindi) "अंग को सहारा देने के लिए उसके दोनों तरफ एक खपच्ची, स्केल या मुड़ी हुई कार्डबोर्ड शीट रखें ताकि अंग हिले नहीं।" else "Gently place a splint, rolled magazines, or sturdy cardboard alongside the limb to immobilize it and prevent joint movement."
                2 -> if (isHindi) "सूजन और दर्द को कम करने के लिए कपड़े में लपेटकर थोड़ी बर्फ घाव के पास लगाएं। सीधे त्वचा पर बर्फ न लगाएं।" else "Apply a cold compress or ice wrapped in a soft cloth near the injury to soothe the swelling. Never put ice directly on the skin."
                3 -> if (isHindi) "बहुत बढ़िया, घाव अब सुरक्षित है। आइए तुरंत आपातकालीन सेवाओं को कॉल करें या निकटतम चिकित्सा केंद्र तक सुरक्षित पहुंचें।" else "Excellent, the limb is stabilized. Now, let's safely call for professional medical help or transport them to the nearest hospital."
                else -> ""
            }
            "snakebite" -> when (step) {
                0 -> if (isHindi) "शांत रहें और मरीज को पूरी तरह शांत रखें। अत्यधिक हिलने-डुलने से जहर तेज़ी से फैल सकता है, इसलिए उन्हें बिल्कुल स्थिर रखें।" else "The most important thing is to keep them completely calm and still. Movement can speed up venom travel, so let's rest together."
                1 -> if (isHindi) "काटे गए अंग या हिस्से को हमेशा दिल के स्तर से नीचे रखें ताकि जहर का संचार धीमा हो सके।" else "Gently position the bitten area so it remains below the level of the heart to slow down the circulation of the venom."
                2 -> if (isHindi) "घाव को साफ पानी और साबुन से धीरे से धोएं। घाव पर कोई चीरा न लगाएं और न ही मुंह से जहर चूसने की कोशिश करें।" else "Gently clean the bite area with soap and water. Please, do not try to cut the wound or suck out the venom—that is a dangerous myth."
                3 -> if (isHindi) "शानदार, आप बहुत संयम से काम ले रहे हैं। अब तुरंत बिना देरी किए एंटी-वेनम के लिए निकटतम सरकारी अस्पताल पहुंचें।" else "You are doing amazing. Let's immediately arrange transport to the nearest hospital for professional anti-venom treatment."
                else -> ""
            }
            "bleeding" -> when (step) {
                0 -> if (isHindi) "चिंता न करें, हम रक्तस्राव को रोक लेंगे। घाव पर एक साफ कपड़े या बाँधने वाले पैड से सीधे और मजबूत दबाव बनाएं।" else "Do not worry, we can stop this. Place a sterile pad or clean cloth directly over the bleeding point and apply firm, continuous pressure."
                1 -> if (isHindi) "दबाव बनाए रखते हुए, चोटिल अंग को धीरे से उठाकर उनके हृदय के स्तर से ऊपर रखें ताकि रक्त का दबाव कम हो सके।" else "While holding that firm pressure, let's gently elevate the bleeding limb above the level of the heart to reduce flow."
                2 -> if (isHindi) "अब एक साफ पट्टी को घाव के ऊपर थोड़ा कसकर लपेटें ताकि दबाव बना रहे, लेकिन इसे बहुत ज्यादा टाइट न करें।" else "Secure the cloth with a pressure bandage, wrapping firmly over the wound site. Ensure it is snug but not cutting off circulation."
                3 -> if (isHindi) "यदि रक्त अभी भी बाहर आ रहा है, तो पहली पट्टी को हटाए बिना उसके ऊपर एक और पट्टी कसें। हम सुरक्षित हैं।" else "If blood still leaks through, do not peel off the first bandage. Simply layer another clean wrap directly over it and hold firm."
                else -> ""
            }
            else -> ""
        }
    }

    fun getActiveInjuryTotalSteps(): Int {
        if (isDynamicAiMode.value && aiSteps.value.isNotEmpty()) {
            return aiSteps.value.size
        }
        return 4 // Standardized to 4 simple, highly visual steps for this prototype
    }

    private fun startCprMetronome() {
        cprJob?.cancel()
        cprBpmCount.value = 0
        cprJob = viewModelScope.launch {
            while (activeInjury.value == "cpr" && arIsPlaying.value) {
                cprBpmCount.value = (cprBpmCount.value % 30) + 1
                // 100 BPM is ~600ms per compressions
                // Play a metronome beep
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(600)
            }
        }
    }

    private fun stopCprMetronome() {
        cprJob?.cancel()
        cprJob = null
    }

    fun updateCompliance(status: String) {
        complianceState.value = status
        val isHindi = preferredLanguage.value == "hi"
        val injury = activeInjury.value ?: return

        val msg = when (status) {
            "success" -> {
                if (isHindi) {
                    when (injury) {
                        "cpr" -> "बहुत बढ़िया! संपीड़न की गति और गहराई बिल्कुल सही है। इसी लय को बनाए रखें।"
                        "burns" -> "शानदार! ठंडा पानी लगातार बहने दें, यह त्वचा के तापमान को सुरक्षित रूप से कम कर रहा है।"
                        "cuts", "bleeding" -> "बहुत अच्छा! दबाव बिल्कुल सही स्थान पर है। रक्तस्राव नियंत्रण में आ जाएगा।"
                        else -> "उत्कृष्ट! आप चरणों का बिल्कुल सही ढंग से पालन कर रहे हैं। स्थिर बने रहें।"
                    }
                } else {
                    when (injury) {
                        "cpr" -> "Excellent work! Your chest compression rate and hand alignment look pristine. Keep up this exact rhythm."
                        "burns" -> "Perfect! Keeping cool running water over the burn is actively cooling the epidermal tissue."
                        "cuts", "bleeding" -> "Great job! Direct firm compression is successfully limiting fluid loss. Hold steady."
                        else -> "Perfect! You are following the guidance step precisely. Remain calm and steady."
                    }
                }
            }
            "struggling" -> {
                if (isHindi) {
                    when (injury) {
                        "cpr" -> "कोई बात नहीं, शांत रहें। छाती को कम से कम दो इंच गहरा दबाने की कोशिश करें, और अपनी कोहनियों को सीधा रखें।"
                        "burns" -> "यदि बहता पानी उपलब्ध नहीं है, तो साफ पानी से भीगे हुए गीले कपड़े का उपयोग करें। घाव को रगड़ें नहीं।"
                        "cuts", "bleeding" -> "यदि कपड़ा भीग गया है, तो उसे हटाएं नहीं। उसके ऊपर एक और साफ कपड़ा रखें और दबाव बढ़ाएं।"
                        "fracture" -> "अंग को सीधा करने की कोशिश न करें। केवल उसे उसी स्थिति में सहारा दें जैसी वह अभी है।"
                        else -> "घबराएं नहीं, आराम से करें। घाव के ठीक बीच वाले हिस्से पर ध्यान केंद्रित करें।"
                    }
                } else {
                    when (injury) {
                        "cpr" -> "Take a breath. Lock your elbows straight and press using your upper body weight. Let's practice the rhythm together."
                        "burns" -> "If direct running water is hard to maintain, gently pour clean water over it continuously, or use a moist sterile wrap."
                        "cuts", "bleeding" -> "If blood leaks through the cloth, do not lift it. Layer another cloth directly over it and increase pressure."
                        "fracture" -> "Do not try to align or snap the bone back. Simply immobilize it in its current natural position."
                        else -> "Don't panic. Take your time, position the alignment marker directly over the wound point."
                    }
                }
            }
            "critical" -> {
                if (isHindi) {
                    "सावधान। यदि दर्द गंभीर हो रहा है या रक्तस्राव नहीं रुक रहा है, तो तुरंत एसओएस बटन दबाएं और एम्बुलेंस बुलाएं।"
                } else {
                    "Critical. If pain is severe or bleeding is unrestrained, tap the Emergency SOS trigger immediately for direct dispatch."
                }
            }
            else -> ""
        }

        if (msg.isNotEmpty()) {
            speak(msg)
        }
    }

    fun analyzeWoundWithAi(base64Image: String, injuryCategory: String) {
        viewModelScope.launch {
            aiIsAnalyzing.value = true
            isDynamicAiMode.value = true
            val isHindi = preferredLanguage.value == "hi"
            val apiKey = try {
                com.example.BuildConfig.GEMINI_API_KEY
            } catch (e: Exception) {
                ""
            }

            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                // Graceful offline simulation fallback in case no API key or development key is set
                delay(2000)
                val dummyDiagnosis = if (isHindi) {
                    "एआर विश्लेषण: ${injuryCategory.uppercase()} की पहचान की गई है। मध्यम गंभीरता, घाव स्थल की स्थिति स्थिर है।"
                } else {
                    "AI Diagnosis: Identified ${injuryCategory.uppercase()} wound. Moderate severity, localized tissue margins stable."
                }
                
                val dummySteps = if (isHindi) {
                    listOf(
                        "ठंडे बहते पानी से घाव को धीरे से साफ करें और किसी भी धूल को हटाएं।",
                        "घाव के चारों ओर रोगाणुरोधी लोशन लगाएं (बीच में सीधा न लगाएं)।",
                        "साफ की गई जगह को जीवाणुहीन बाँधने वाले पैड या पट्टी से धीरे से लपेटें।",
                        "दर्द को कम करने और सूजन को नियंत्रित करने के लिए घाव वाले हिस्से को हृदय स्तर से ऊपर उठाएं।"
                    )
                } else {
                    listOf(
                        "Gently rinse the designated injury area with cool sterile fluid to clear particulate debris.",
                        "Apply mild antiseptic formulation strictly around the wound margins (avoid direct friction).",
                        "Securely wrap with a loose sterile conformant bandage, leaving breathing gaps.",
                        "Elevate the afflicted limb gently to curb hydrostatic pressure and throb."
                    )
                }
                
                aiWoundDiagnosis.value = dummyDiagnosis
                aiSteps.value = dummySteps
                activeStep.value = 0
                aiIsAnalyzing.value = false
                speakStepInstruction()
                return@launch
            }

            // Real Gemini Call using standard HttpURLConnection
            withContext(Dispatchers.IO) {
                try {
                    val url = java.net.URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    val conn = url.openConnection() as java.net.HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.setRequestProperty("Content-Type", "application/json")
                    conn.connectTimeout = 30000
                    conn.readTimeout = 30000
                    conn.doOutput = true

                    val promptText = """
                        You are a professional emergency medical responder guiding a user in live AR first aid.
                        Analyze this camera frame which shows the user's wound: specifically of category: $injuryCategory.
                        Respond in a strictly structured JSON format. 
                        Do NOT include markdown block characters (like ```json or ```). Return ONLY the raw valid JSON matching this schema:
                        {
                          "diagnosis": "A calming, concise diagnosis under 15 words. Explain severity.",
                          "steps": [
                            "Step 1: Direct action step, under 15 words.",
                            "Step 2: Direct action step, under 15 words.",
                            "Step 3: Direct action step, under 15 words.",
                            "Step 4: Direct action step, under 15 words."
                          ],
                          "calmingVoice": "A highly reassuring, calming vocal feedback prompt under 25 words directing them what to do next."
                        }
                        Make all response text in ${if (isHindi) "Hindi language" else "English language"}.
                    """.trimIndent()

                    val requestObj = org.json.JSONObject()
                    val contentsArr = org.json.JSONArray()
                    val contentObj = org.json.JSONObject()
                    val partsArr = org.json.JSONArray()

                    val promptPart = org.json.JSONObject()
                    promptPart.put("text", promptText)
                    partsArr.put(promptPart)

                    val imagePart = org.json.JSONObject()
                    val inlineDataObj = org.json.JSONObject()
                    inlineDataObj.put("mimeType", "image/jpeg")
                    inlineDataObj.put("data", base64Image)
                    imagePart.put("inlineData", inlineDataObj)
                    partsArr.put(imagePart)

                    contentObj.put("parts", partsArr)
                    contentsArr.put(contentObj)
                    requestObj.put("contents", contentsArr)

                    val configObj = org.json.JSONObject()
                    val responseFormatObj = org.json.JSONObject()
                    responseFormatObj.put("type", "OBJECT")
                    val formatTextObj = org.json.JSONObject()
                    formatTextObj.put("mimeType", "application/json")
                    responseFormatObj.put("text", formatTextObj)
                    configObj.put("responseFormat", responseFormatObj)
                    requestObj.put("generationConfig", configObj)

                    java.io.OutputStreamWriter(conn.outputStream).use { writer ->
                        writer.write(requestObj.toString())
                        writer.flush()
                    }

                    if (conn.responseCode == 200) {
                        val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                        val responseJson = org.json.JSONObject(responseText)
                        val candidates = responseJson.optJSONArray("candidates")
                        if (candidates != null && candidates.length() > 0) {
                            val candidate = candidates.getJSONObject(0)
                            val content = candidate.optJSONObject("content")
                            if (content != null) {
                                val parts = content.optJSONArray("parts")
                                if (parts != null && parts.length() > 0) {
                                    val rawText = parts.getJSONObject(0).optString("text", "")
                                    
                                    val cleanJsonText = rawText.trim()
                                        .removePrefix("```json")
                                        .removePrefix("```")
                                        .removeSuffix("```")
                                        .trim()
                                        
                                    val resultObj = org.json.JSONObject(cleanJsonText)
                                    val diagnosis = resultObj.optString("diagnosis", "Dynamic analysis successful.")
                                    val calmingVoice = resultObj.optString("calmingVoice", "")
                                    
                                    val stepsArr = resultObj.optJSONArray("steps")
                                    val parsedSteps = mutableListOf<String>()
                                    if (stepsArr != null) {
                                        for (i in 0 until stepsArr.length()) {
                                            parsedSteps.add(stepsArr.getString(i))
                                        }
                                    }
                                    
                                    withContext(Dispatchers.Main) {
                                        if (parsedSteps.size >= 4) {
                                            aiWoundDiagnosis.value = diagnosis
                                            aiSteps.value = parsedSteps
                                            activeStep.value = 0
                                            aiVoiceFeedback.value = calmingVoice
                                            aiIsAnalyzing.value = false
                                            
                                            if (calmingVoice.isNotEmpty()) {
                                                speak("$diagnosis. $calmingVoice. ${parsedSteps[0]}")
                                            } else {
                                                speakStepInstruction()
                                            }
                                        } else {
                                            throw Exception("Incomplete steps list")
                                        }
                                    }
                                    return@withContext
                                }
                            }
                        }
                    }
                    throw Exception("Status code: ${conn.responseCode}")
                } catch (e: Exception) {
                    Log.e("PranaViewModel", "Gemini vision call failed: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        val errorDiagnosis = if (isHindi) {
                            "एआर निदान: नेटवर्क व्यस्त है। हम सुरक्षित रूप से मानक उपचार चरणों का उपयोग कर रहे हैं।"
                        } else {
                            "AR Diagnosis: Offline/Busy. Standardizing to recommended offline medical protocols."
                        }
                        aiWoundDiagnosis.value = errorDiagnosis
                        aiSteps.value = emptyList()
                        isDynamicAiMode.value = false
                        aiIsAnalyzing.value = false
                        speakStepInstruction()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
        toneGenerator?.release()
        sirenJob?.cancel()
        flashlightJob?.cancel()
        stopCprMetronome()
    }
}
