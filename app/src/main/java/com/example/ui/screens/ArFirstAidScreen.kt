package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.material3.TextButton
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import android.graphics.Bitmap
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.PranaViewModel
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlin.math.sin

@Composable
fun ArFirstAidScreen(viewModel: PranaViewModel) {
    val activeInjury by viewModel.activeInjury.collectAsState()
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"

    Box(modifier = Modifier.fillMaxSize()) {
        if (activeInjury == null) {
            // Screen to choose / start session
            SelectInjuryDashboard(viewModel, isHindi)
        } else {
            // Active AR screen
            ActiveArGuidanceScreen(viewModel, activeInjury!!, isHindi)
        }
    }
}

@Composable
fun SelectInjuryDashboard(viewModel: PranaViewModel, isHindi: Boolean) {
    val items = listOf(
        Triple("burns", if (isHindi) "जलन (Burns & Scalds)" else "Burns & Scalds", "Water flow guidance"),
        Triple("cuts", if (isHindi) "कट और मामूली रक्तस्राव" else "Cuts & Minor Bleeding", "Pressure & Bandage guide"),
        Triple("cpr", if (isHindi) "सीपीआर (CPR)" else "CPR Resuscitation", "100 BPM metronome circle"),
        Triple("choking", if (isHindi) "दम घुटना (Choking)" else "Choking First Aid", "Back blow arrow guides"),
        Triple("fracture", if (isHindi) "हड्डी टूटना (Fracture)" else "Fractures & Bone Injury", "Splinting & Hold vectors"),
        Triple("snakebite", if (isHindi) "सांप का काटना (Snake Bite)" else "Snake Bites", "Keep still and low-heart guide"),
        Triple("bleeding", if (isHindi) "गंभीर रक्तस्राव (Bleeding)" else "Severe Bleeding Control", "Pressure wrapping guides")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF8F3))
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(SageGreenPrimary.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = ForestGreenAccent,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isHindi) "एआर कैमरा प्राथमिक चिकित्सा" else "Start AR First Aid Camera",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isHindi) "एक चोट का चयन करें। हमारा कैमरा एआर मार्गदर्शन को सीधे घाव पर ओवरले करेगा।" else "Select an injury. Prana overlays animated AR guidelines onto your camera feed.",
            fontSize = 14.sp,
            color = OliveGreenSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))

        items.forEach { (id, name, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.startFirstAidSession(id) }
                    .testTag("injury_select_card_$id"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(SageGreenPrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Healing,
                            contentDescription = null,
                            tint = ForestGreenAccent
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = desc,
                            fontSize = 12.sp,
                            color = OliveGreenSecondary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Start",
                        tint = SageGreenPrimary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ActiveArGuidanceScreen(viewModel: PranaViewModel, injury: String, isHindi: Boolean) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val activeStep by viewModel.activeStep.collectAsState()
    val isPlaying by viewModel.arIsPlaying.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val showTips by viewModel.showGestureRecognitionTip.collectAsState()
    val cprBpmCount by viewModel.cprBpmCount.collectAsState()
    val complianceState by viewModel.complianceState.collectAsState()

    // Collect Dynamic AI vision states
    val isDynamicAiMode by viewModel.isDynamicAiMode.collectAsState()
    val aiWoundDiagnosis by viewModel.aiWoundDiagnosis.collectAsState()
    val aiIsAnalyzing by viewModel.aiIsAnalyzing.collectAsState()

    var showMenu by remember { mutableStateOf(false) }
    var woundPosition by remember { mutableStateOf<Offset?>(null) }
    var isScanning by remember { mutableStateOf(true) }
    var isStepsCardExpanded by remember { mutableStateOf(false) }
    var showGestureSimulator by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    var useSimulationMode by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            useSimulationMode = true
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasCameraPermission && !useSimulationMode) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF8F3))
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(SageGreenPrimary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = ForestGreenAccent,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (isHindi) "कैमरा अनुमति की आवश्यकता है" else "Camera Access Required",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenAccent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isHindi) {
                    "प्राण एआर उपचार सीधे घाव पर ओवरले करने के लिए कैमरे का उपयोग करता है। कृपया लाइव वीडियो देखने के लिए कैमरा पहुंच की अनुमति दें।"
                } else {
                    "Prana uses your camera to scan injuries, analyze severity via AI, and overlay real-time animated treatment steps directly onto your skin."
                },
                fontSize = 14.sp,
                color = OliveGreenSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (isHindi) "अनुमति दें (Grant Permission)" else "Enable Live Camera Feed",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = { useSimulationMode = true }
            ) {
                Text(
                    text = if (isHindi) "सिमुलेशन मोड का उपयोग करें (Try Simulator)" else "Try Interactive Sandbox/Simulator",
                    color = SageGreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            TextButton(
                onClick = { viewModel.endFirstAidSession() }
            ) {
                Text(
                    text = if (isHindi) "पीछे जाएं (Go Back)" else "Cancel & Go Back",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
        return
    }

    LaunchedEffect(injury) {
        isScanning = true
        delay(2500)
        isScanning = false
        viewModel.speak(
            if (isHindi) "एआई कैमरा ने घाव स्थल की पहचान की है। आप मार्गदर्शन को बदलने के लिए स्क्रीन पर कहीं भी टैप कर सकते हैं या लाइव एआई विश्लेषण के लिए स्कैन बटन पर क्लिक कर सकते हैं।"
            else "AI Camera has located the wound site. Tap anywhere on the screen to relocate the interactive AR guidance, or tap the AI Scan button for dynamic custom instruction."
        )
    }

    // CameraX setup with ImageAnalysis
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    val previewView = remember { PreviewView(context).apply { layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) } }

    LaunchedEffect(Unit) {
        try {
            cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Real-time Visual Color Cluster Centroid Tracker inside ImageAnalysis
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            var lastAnalyzedTime = 0L
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastAnalyzedTime >= 250) { // Limit to 4 FPS for battery/CPU efficiency
                    lastAnalyzedTime = currentTime

                    val planes = imageProxy.planes
                    if (planes.size >= 3) {
                        val uBuffer = planes[1].buffer
                        val vBuffer = planes[2].buffer

                        val width = imageProxy.width
                        val height = imageProxy.height

                        var sumX = 0L
                        var sumY = 0L
                        var redPixelCount = 0

                        val rowStrideUV = planes[1].rowStride
                        val pixelStrideUV = planes[1].pixelStride

                        // Subsample step 16 is extremely fast and light
                        for (y in 0 until height step 16) {
                            for (x in 0 until width step 16) {
                                val uvIndex = (y / 2) * rowStrideUV + (x / 2) * pixelStrideUV
                                if (uvIndex < uBuffer.capacity() && uvIndex < vBuffer.capacity()) {
                                    val uVal = (uBuffer.get(uvIndex).toInt() and 0xFF) - 128
                                    val vVal = (vBuffer.get(uvIndex).toInt() and 0xFF) - 128

                                    // In YUV, high V (redness) indicates blood-red or wound swelling pink
                                    if (vVal > 22 && uVal < 5) {
                                        sumX += x
                                        sumY += y
                                        redPixelCount++
                                    }
                                }
                            }
                        }

                        if (redPixelCount > 6) { // Cluster identified
                            val centroidX = sumX.toFloat() / redPixelCount
                            val centroidY = sumY.toFloat() / redPixelCount

                            // Map image coordinates (typically landscape) to screen (portrait) coordinates
                            val mappedX = (centroidY / height) * previewView.width
                            val mappedY = (centroidX / width) * previewView.height

                            // Low-pass filter for buttery smooth movement
                            val currentPos = woundPosition
                            if (currentPos == null) {
                                woundPosition = Offset(mappedX, mappedY)
                            } else {
                                val dx = mappedX - currentPos.x
                                val dy = mappedY - currentPos.y
                                woundPosition = Offset(
                                    currentPos.x + dx * 0.25f,
                                    currentPos.y + dy * 0.25f
                                )
                            }
                        }
                    }
                }
                imageProxy.close()
            }

            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
        } catch (e: Exception) {
            Log.e("ArFirstAidScreen", "CameraX binding failed: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                cameraProvider?.unbindAll()
            } catch (e: Exception) {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(injury) {
                detectTapGestures { offset ->
                    woundPosition = offset
                    viewModel.speak(
                        if (isHindi) "एआर प्रोजेक्टर को नए घाव स्थल पर स्थानांतरित कर दिया गया है।"
                        else "AR overlays shifted to pinpoint location."
                    )
                }
            }
    ) {
        // Background CameraX feed or beautiful medical forearm/hand illustration
        if (hasCameraPermission && !useSimulationMode) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
            // Soft overlay to ensure high contrast for instructions
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f))
            )
        } else {
            // Highly polished vector medical illustration of a hand/forearm as background
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFEBE4)) // Soft warm background
            ) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f
                val centerY = h / 2f - 120f

                // Draw technical grid lines to look like an AR medical overlay environment
                val gridStep = 60f
                for (x in 0..(w.toInt()) step gridStep.toInt()) {
                    drawLine(
                        color = ForestGreenAccent.copy(alpha = 0.05f),
                        start = Offset(x.toFloat(), 0f),
                        end = Offset(x.toFloat(), h),
                        strokeWidth = 1f
                    )
                }
                for (y in 0..(h.toInt()) step gridStep.toInt()) {
                    drawLine(
                        color = ForestGreenAccent.copy(alpha = 0.05f),
                        start = Offset(0f, y.toFloat()),
                        end = Offset(w, y.toFloat()),
                        strokeWidth = 1f
                    )
                }

                // Draw stylized arm/hand structure
                val path = Path().apply {
                    // Forearm coming from bottom
                    moveTo(centerX - 80f, h)
                    lineTo(centerX - 60f, centerY + 200f)
                    // Wrist
                    quadraticTo(centerX - 50f, centerY + 120f, centerX - 70f, centerY + 80f)
                    // Hand palm and fingers
                    lineTo(centerX - 100f, centerY + 20f)
                    quadraticTo(centerX - 120f, centerY - 20f, centerX - 80f, centerY - 30f) // Thumb
                    lineTo(centerX - 60f, centerY + 10f)
                    // Index finger
                    lineTo(centerX - 60f, centerY - 180f)
                    quadraticTo(centerX - 40f, centerY - 200f, centerX - 20f, centerY - 180f)
                    lineTo(centerX - 20f, centerY - 10f)
                    // Middle finger
                    lineTo(centerX - 15f, centerY - 210f)
                    quadraticTo(centerX + 5f, centerY - 230f, centerX + 25f, centerY - 210f)
                    lineTo(centerX + 25f, centerY - 10f)
                    // Ring finger
                    lineTo(centerX + 30f, centerY - 190f)
                    quadraticTo(centerX + 50f, centerY - 210f, centerX + 70f, centerY - 190f)
                    lineTo(centerX + 65f, centerY + 10f)
                    // Pinky
                    lineTo(centerX + 75f, centerY - 130f)
                    quadraticTo(centerX + 95f, centerY - 150f, centerX + 110f, centerY - 120f)
                    lineTo(centerX + 90f, centerY + 40f)
                    // Outer hand edge
                    quadraticTo(centerX + 110f, centerY + 120f, centerX + 80f, centerY + 200f)
                    lineTo(centerX + 100f, h)
                    close()
                }

                // Draw hand body with smooth skin/beige tone
                drawPath(
                    path = path,
                    color = Color(0xFFF3E5D8)
                )
                // Draw hand outer stroke
                drawPath(
                    path = path,
                    color = ForestGreenAccent.copy(alpha = 0.35f),
                    style = Stroke(width = 4f)
                )

                // Draw abstract wound area on palm/forearm area (centered right at centerX, centerY)
                // Soft pink redness around wound
                drawCircle(
                    color = Color(0xFFFFCDD2).copy(alpha = 0.8f),
                    radius = 80f,
                    center = Offset(centerX, centerY)
                )

                when (injury) {
                    "burns" -> {
                        // Blistered red burn mark
                        drawCircle(
                            color = Color(0xFFE57373),
                            radius = 42f,
                            center = Offset(centerX, centerY)
                        )
                        drawCircle(
                            color = Color(0xFFFFEB3B).copy(alpha = 0.7f),
                            radius = 14f,
                            center = Offset(centerX - 12f, centerY - 12f)
                        )
                    }
                    "cuts", "bleeding" -> {
                        // Slash line
                        drawLine(
                            color = Color(0xFFB71C1C),
                            start = Offset(centerX - 45f, centerY - 15f),
                            end = Offset(centerX + 45f, centerY + 15f),
                            strokeWidth = 14f,
                            cap = StrokeCap.Round
                        )
                    }
                    "snakebite" -> {
                        // Two fangs punctures
                        drawCircle(color = Color(0xFF311B92), radius = 8f, center = Offset(centerX - 15f, centerY))
                        drawCircle(color = Color(0xFF311B92), radius = 8f, center = Offset(centerX + 15f, centerY))
                    }
                    "fracture" -> {
                        // Bent line showing bone breakage
                        drawLine(
                            color = Color(0xFF795548),
                            start = Offset(centerX - 50f, centerY),
                            end = Offset(centerX, centerY - 25f),
                            strokeWidth = 12f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = Color(0xFF795548),
                            start = Offset(centerX, centerY - 25f),
                            end = Offset(centerX + 50f, centerY),
                            strokeWidth = 12f,
                            cap = StrokeCap.Round
                        )
                    }
                    else -> {
                        drawCircle(
                            color = Color(0xFFE57373),
                            radius = 30f,
                            center = Offset(centerX, centerY)
                        )
                    }
                }
            }
        }

        // AR Overlays layer
        ArCanvasOverlay(
            injury = injury,
            activeStep = activeStep,
            cprBpmCount = cprBpmCount,
            isPlaying = isPlaying,
            woundPosition = woundPosition
        )

        // Moving Horizontal Scanning Line Overlay when scanning is active
        if (isScanning) {
            val infiniteTransition = rememberInfiniteTransition(label = "scan_line")
            val scanAnim by infiniteTransition.animateFloat(
                initialValue = 0.1f,
                targetValue = 0.9f,
                animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Reverse),
                label = "scan"
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green.copy(alpha = 0.12f))
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val y = size.height * scanAnim
                    drawLine(
                        color = Color.Green,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 4.dp.toPx()
                    )
                    drawRect(
                        color = Color.Green.copy(alpha = 0.15f),
                        topLeft = Offset(0f, y - 40f),
                        size = Size(size.width, 80f)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color.Green, CircleShape)
                        )
                        Text(
                            text = if (isHindi) "घाव स्कैन किया जा रहा है... (AI Analyzing)" else "SCANNING WOUND IRL... (AI Analyzing)",
                            color = Color.Green,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Live Holographic Sonar Pulses Overlay during Gemini Vision API call
        if (aiIsAnalyzing) {
            val infiniteTransition = rememberInfiniteTransition(label = "ai_pulse")
            val pulseRadius by infiniteTransition.animateFloat(
                initialValue = 40f,
                targetValue = 280f,
                animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
                label = "radius"
            )
            val pulseAlpha by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 0.0f,
                animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
                label = "alpha"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = woundPosition ?: Offset(size.width / 2, size.height / 2)
                    drawCircle(
                        color = SageGreenPrimary.copy(alpha = pulseAlpha),
                        radius = pulseRadius,
                        center = center,
                        style = Stroke(width = 4.dp.toPx())
                    )
                    drawCircle(
                        color = ForestGreenAccent.copy(alpha = pulseAlpha * 0.5f),
                        radius = pulseRadius * 0.7f,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .border(2.dp, SageGreenPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI processing",
                            tint = SageGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Text(
                        text = if (isHindi) "प्राण एआई लाइव घाव का विश्लेषण कर रहा है..." else "Prana AI is analyzing your wound live...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (isHindi) "कृपया अपने फोन को स्थिर रखें और शांत रहें।" else "Please hold your phone steady and remain calm.",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Floating Diagnosis capsule at top center if dynamic AI mode is active
        if (isDynamicAiMode && aiWoundDiagnosis != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 110.dp)
                    .background(ForestGreenAccent.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = aiWoundDiagnosis!!,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Top Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Close Session Button
            IconButton(
                onClick = { viewModel.endFirstAidSession() },
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .testTag("close_ar_session_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Injury title dropdown selector
            Box {
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .clickable { showMenu = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatInjuryName(injury, isHindi),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    val list = listOf("burns", "cuts", "cpr", "choking", "fracture", "snakebite", "bleeding")
                    list.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(formatInjuryName(item, isHindi)) },
                            onClick = {
                                viewModel.startFirstAidSession(item)
                                showMenu = false
                            }
                        )
                    }
                }
            }

            // Audio Feedback status indicator
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (isSpeaking) SageGreenPrimary.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { viewModel.repeatArVoice() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Repeat Voice",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Mid Overlay: Active Visual Tip Banner for Gesture Navigation
        if (showTips && !isDynamicAiMode) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 160.dp, start = 24.dp, end = 24.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.75f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gesture,
                            contentDescription = null,
                            tint = SageGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isHindi) "हाथ खाली नहीं हैं? आगे बढ़ने के लिए 👍 (थम्स-अप) दिखाएं!" else "Hands occupied? Give a 👍 Thumbs Up to move to the next step!",
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.showGestureRecognitionTip.value = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Dismiss",
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        // Floating Snapchat-like Vertical Control Sidebar on the Right
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. DYNAMIC AI SCAN BUTTON (MANDATORY VISION TRIGGER)
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(SageGreenPrimary, ForestGreenAccent)
                        ),
                        shape = CircleShape
                    )
                    .border(width = 2.dp, color = Color.White, shape = CircleShape)
                    .clickable {
                        // Capture the current camera preview frame as a Bitmap directly from PreviewView
                        val bitmap = try {
                            previewView.bitmap
                        } catch (e: Exception) {
                            null
                        }

                        if (bitmap != null) {
                            // High performance resizing & encoding
                            val resized = bitmap.resizeToMax(512)
                            val base64 = resized.toBase64()
                            viewModel.analyzeWoundWithAi(base64, injury)
                        } else {
                            // Graceful fallback to simulated vision call if bitmap is empty (e.g. emulator)
                            viewModel.analyzeWoundWithAi("", injury)
                        }
                    }
                    .testTag("ai_scan_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Trigger AI Vision Analysis",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            // 2. Gesture Simulator HUD toggle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        if (showGestureSimulator) SageGreenPrimary.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.65f),
                        CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (showGestureSimulator) Color.White else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable { showGestureSimulator = !showGestureSimulator },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Gesture,
                    contentDescription = "Toggle Gesture Simulator",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 3. Relocate overlay back to center
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color.Black.copy(alpha = 0.65f), CircleShape)
                    .clickable {
                        woundPosition = null
                        viewModel.speak(
                            if (isHindi) "एआर संकेतक केंद्र में वापस स्थानांतरित कर दिया गया है।"
                            else "AR overlay reset to center. Tap any point on the screen to relocate."
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Healing,
                    contentDescription = "Reset AR Target",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Bottom Controls: Text Guidance & Navigation Buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Sleek conversational glassmorphic speech guidance box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.72f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Caregiver Support Agent avatar icon
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SageGreenPrimary.copy(alpha = 0.2f), CircleShape)
                            .border(1.dp, SageGreenPrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Healing,
                            contentDescription = "Nurse Companion",
                            tint = SageGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isHindi) "🩺 प्राण लाइव असिस्ट" else "🩺 Prana Live Care Guide",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SageGreenPrimary
                            )
                            if (isDynamicAiMode) {
                                Box(
                                    modifier = Modifier
                                        .background(ForestGreenAccent, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "DYNAMIC AI",
                                        fontSize = 7.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = viewModel.getStepText(injury, activeStep, isHindi),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Compact Navigation Button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prev button (icon only)
                IconButton(
                    onClick = { viewModel.prevArStep() },
                    enabled = activeStep > 0,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (activeStep > 0) Color.Black.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.2f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous",
                        tint = if (activeStep > 0) Color.White else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Pause button (icon only)
                IconButton(
                    onClick = { viewModel.pauseArSession() },
                    modifier = Modifier
                        .size(36.dp)
                        .background(SageGreenPrimary.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Pause",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Next pill button taking up remaining width
                Button(
                    onClick = { viewModel.nextArStep() },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent.copy(alpha = 0.9f)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("ar_next_step_button"),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (activeStep < viewModel.getActiveInjuryTotalSteps() - 1) {
                                if (isHindi) "अगला कदम" else "Next Step"
                            } else {
                                if (isHindi) "उपचार पूर्ण" else "Complete"
                            },
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

            // Hands-Free Gesture Simulation Panel (Collapsible / Toggled by floating sidebar)
            if (showGestureSimulator) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.75f)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isHindi) "हैंड्स-फ्री जेस्चर सिमुलेशन (कैमरा)" else "Hands-Free Gesture Simulator HUD",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SageGreenPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            GestureSimulatorItem(label = "👍 Next", tag = "gesture_thumbs_up", onClick = { viewModel.triggerGestureSimulation("thumbs_up") })
                            GestureSimulatorItem(label = "✋ Pause", tag = "gesture_palm", onClick = { viewModel.triggerGestureSimulation("palm") })
                            GestureSimulatorItem(label = "☝ Replay", tag = "gesture_point", onClick = { viewModel.triggerGestureSimulation("point") })
                            GestureSimulatorItem(label = "👌 Voice", tag = "gesture_pinch", onClick = { viewModel.triggerGestureSimulation("pinch") })
                            GestureSimulatorItem(label = "👐 Back", tag = "gesture_open_hand", onClick = { viewModel.triggerGestureSimulation("open_hand") })
                        }
                    }
                }
            }
        }
    }

@Composable
fun GestureSimulatorItem(label: String, tag: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { onClick() }
            .testTag(tag)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ArCanvasOverlay(injury: String, activeStep: Int, cprBpmCount: Int, isPlaying: Boolean, woundPosition: Offset?) {
    val infiniteTransition = rememberInfiniteTransition(label = "ar_anim")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "alpha"
    )
    val floatOffsetAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "float"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = woundPosition ?: Offset(size.width / 2f, size.height / 2f - 120f)

        // Draw AI Tracking crosshair HUD
        drawCircle(
            color = Color.Green.copy(alpha = 0.4f),
            radius = 35f,
            center = center,
            style = Stroke(width = 2f)
        )
        drawLine(
            color = Color.Green.copy(alpha = 0.8f),
            start = Offset(center.x - 55f, center.y),
            end = Offset(center.x + 55f, center.y),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Green.copy(alpha = 0.8f),
            start = Offset(center.x, center.y - 55f),
            end = Offset(center.x, center.y + 55f),
            strokeWidth = 2f
        )

        when (injury) {
            "burns" -> {
                if (isPlaying) {
                    if (activeStep == 0 || activeStep == 1) {
                        // Water tap and flow visualizer particles
                        val tapX = center.x
                        val tapY = center.y - 250f // faucet positioned 250px above pinpoint

                        // 1. Draw a beautiful stylized water faucet/tap
                        drawLine(
                            color = Color(0xFFB0BEC5), // metallic grey
                            start = Offset(tapX - 100f, tapY),
                            end = Offset(tapX, tapY),
                            strokeWidth = 24f,
                            cap = StrokeCap.Round
                        )
                        // Faucet mouth curving down
                        drawLine(
                            color = Color(0xFF90A4AE),
                            start = Offset(tapX, tapY),
                            end = Offset(tapX, tapY + 40f),
                            strokeWidth = 28f,
                            cap = StrokeCap.Square
                        )

                        // 2. Cascade water flow lines pouring from the faucet down to the pinpoint
                        val flowHeight = 210f // from tapY + 40f to center.y
                        val startY = tapY + 40f

                        for (i in 0..4) {
                            val offsetMultiplier = (floatOffsetAnim + (i * 25)) % 100
                            val progress = offsetMultiplier / 100f
                            val yPos = startY + progress * flowHeight
                            
                            // Main stream line
                            drawLine(
                                color = Color(0xFF64B5F6).copy(alpha = 0.5f),
                                start = Offset(tapX - 10f + (i * 5f), startY),
                                end = Offset(center.x - 5f + (i * 2.5f), center.y),
                                strokeWidth = 6f
                            )
                            
                            // Falling droplets
                            drawCircle(
                                color = Color(0xFFE3F2FD).copy(alpha = 0.8f),
                                radius = 10f + (i % 2) * 4f,
                                center = Offset(tapX - 20f + (i * 10f) + (progress * 5f), yPos)
                            )
                        }

                        // 3. Water splashing ripples on the pinpoint
                        drawCircle(
                            color = Color(0xFF2196F3).copy(alpha = alphaAnim * 0.8f),
                            radius = 40f + (floatOffsetAnim % 50f) * 1.5f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                    } else if (activeStep == 2) {
                        // Avoid butter/toothpaste: draw defensive barrier shield around wound
                        drawCircle(
                            color = AccentOrange.copy(alpha = alphaAnim),
                            radius = 80f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        // Cross line warning over the wound
                        drawLine(
                            color = AccentOrange.copy(alpha = 0.7f),
                            start = Offset(center.x - 45f, center.y - 45f),
                            end = Offset(center.x + 45f, center.y + 45f),
                            strokeWidth = 8f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = AccentOrange.copy(alpha = 0.7f),
                            start = Offset(center.x + 45f, center.y - 45f),
                            end = Offset(center.x - 45f, center.y + 45f),
                            strokeWidth = 8f,
                            cap = StrokeCap.Round
                        )
                    } else {
                        // Apply dry cloth: gauze bandage wrapping around the wound
                        val progress = floatOffsetAnim / 100f
                        val bandageWidth = 150f * progress
                        drawRoundRect(
                            color = Color(0xFFFFFFFF).copy(alpha = 0.85f),
                            topLeft = Offset(center.x - 75f, center.y - 25f),
                            size = Size(bandageWidth, 50f),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            color = SageGreenPrimary,
                            topLeft = Offset(center.x - 75f, center.y - 25f),
                            size = Size(bandageWidth, 50f),
                            cornerRadius = CornerRadius(6f, 6f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        if (progress > 0.6f) {
                            // Draw green cross symbol in center
                            drawLine(color = ForestGreenAccent, start = Offset(center.x, center.y - 12f), end = Offset(center.x, center.y + 12f), strokeWidth = 4f)
                            drawLine(color = ForestGreenAccent, start = Offset(center.x - 12f, center.y), end = Offset(center.x + 12f, center.y), strokeWidth = 4f)
                        }
                    }
                }
            }
            "cuts" -> {
                if (isPlaying) {
                    if (activeStep == 0 || activeStep == 1) {
                        // Firm pressure animation: compressing concentric rings
                        val compressScale = 1.3f - (floatOffsetAnim % 100f) / 100f * 0.5f
                        drawCircle(
                            color = Color.White.copy(alpha = 0.7f),
                            radius = 80f * compressScale,
                            center = center,
                            style = Stroke(width = 6f)
                        )
                        drawCircle(
                            color = SageGreenPrimary.copy(alpha = alphaAnim),
                            radius = 45f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        // Downward pressing arrowheads
                        for (i in 0..3) {
                            val angle = i * Math.PI / 2
                            val startX = center.x + Math.cos(angle).toFloat() * 95f
                            val startY = center.y + Math.sin(angle).toFloat() * 95f
                            val endX = center.x + Math.cos(angle).toFloat() * 55f
                            val endY = center.y + Math.sin(angle).toFloat() * 55f
                            drawLine(color = Color.White, start = Offset(startX, startY), end = Offset(endX, endY), strokeWidth = 4f, cap = StrokeCap.Round)
                        }
                    } else if (activeStep == 2) {
                        // Rinse with water: spray mist pouring down
                        val sprayY = center.y - 120f
                        drawRect(
                            color = Color(0xFF455A64),
                            topLeft = Offset(center.x - 12f, sprayY - 20f),
                            size = Size(24f, 30f)
                        )
                        for (i in 0..10) {
                            val angle = Math.toRadians((75 + i * 3.0).toDouble())
                            val length = 40f + (floatOffsetAnim % 50f) * 1.8f
                            val destX = center.x + Math.cos(angle).toFloat() * length
                            val destY = (sprayY + 10f) + Math.sin(angle).toFloat() * length
                            drawCircle(
                                color = Color(0xFFB3E5FC).copy(alpha = alphaAnim * 0.7f),
                                radius = 3f + (floatOffsetAnim % 8f) * 0.4f,
                                center = Offset(destX, destY)
                            )
                        }
                    } else {
                        // Apply bandaging
                        val progress = floatOffsetAnim / 100f
                        val bandageWidth = 140f * progress
                        drawRoundRect(
                            color = Color(0xFFF5F5F5).copy(alpha = 0.9f),
                            topLeft = Offset(center.x - 70f, center.y - 20f),
                            size = Size(bandageWidth, 40f),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            color = ForestGreenAccent,
                            topLeft = Offset(center.x - 70f, center.y - 20f),
                            size = Size(bandageWidth, 40f),
                            cornerRadius = CornerRadius(6f, 6f),
                            style = Stroke(width = 2.dp.toPx())
                        )
                        if (progress > 0.5f) {
                            drawLine(color = ForestGreenAccent, start = Offset(center.x, center.y - 10f), end = Offset(center.x, center.y + 10f), strokeWidth = 4f)
                            drawLine(color = ForestGreenAccent, start = Offset(center.x - 10f, center.y), end = Offset(center.x + 10f, center.y), strokeWidth = 4f)
                        }
                    }
                }
            }
            "cpr" -> {
                if (isPlaying) {
                    if (activeStep == 0) {
                        // Hands placement guide: draw handprints crosshair
                        drawCircle(
                            color = Color.White.copy(alpha = alphaAnim),
                            radius = 50f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        // Left & Right hand symbols (sketched minimally as blocks)
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.8f),
                            topLeft = Offset(center.x - 30f, center.y - 15f),
                            size = Size(25f, 30f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.8f),
                            topLeft = Offset(center.x + 5f, center.y - 15f),
                            size = Size(25f, 30f),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    } else if (activeStep == 1) {
                        // Push hard & fast: Beating concentric metronome pulse
                        val beatScale = 1f + 0.18f * sin(cprBpmCount.toFloat() * 1.5f)
                        val pulseRadius = 100f * beatScale
                        drawCircle(
                            color = AccentRed.copy(alpha = 0.35f),
                            radius = pulseRadius,
                            center = center
                        )
                        drawCircle(
                            color = SageGreenPrimary.copy(alpha = 0.7f),
                            radius = 120f,
                            center = center,
                            style = Stroke(width = 4.dp.toPx())
                        )
                    } else if (activeStep == 2) {
                        // Allow chest to rise fully: Upward expanding double arrows
                        val upOffset = (floatOffsetAnim / 100f) * 120f
                        for (i in 0..1) {
                            val startY = center.y + 60f - (i * 40f) - upOffset
                            drawLine(color = Color.White.copy(alpha = alphaAnim), start = Offset(center.x, startY), end = Offset(center.x, startY - 30f), strokeWidth = 6f, cap = StrokeCap.Round)
                            drawLine(color = Color.White.copy(alpha = alphaAnim), start = Offset(center.x, startY - 30f), end = Offset(center.x - 15f, startY - 15f), strokeWidth = 6f, cap = StrokeCap.Round)
                            drawLine(color = Color.White.copy(alpha = alphaAnim), start = Offset(center.x, startY - 30f), end = Offset(center.x + 15f, startY - 15f), strokeWidth = 6f, cap = StrokeCap.Round)
                        }
                    } else {
                        // Give 2 rescue breaths: expanding rings radiating from mouth indicator
                        val breathRadius = 40f + (floatOffsetAnim % 100f) * 1.4f
                        drawCircle(
                            color = Color(0xFF00E5FF).copy(alpha = (1f - floatOffsetAnim / 100f).coerceIn(0f, 1f)),
                            radius = breathRadius,
                            center = Offset(center.x, center.y - 150f),
                            style = Stroke(width = 4f)
                        )
                        drawCircle(
                            color = Color(0xFF00E5FF).copy(alpha = 0.8f),
                            radius = 30f,
                            center = Offset(center.x, center.y - 150f)
                        )
                    }
                }
            }
            "choking" -> {
                if (isPlaying) {
                    if (activeStep == 0) {
                        // Stand behind & lean forward: slant angle guide line
                        drawLine(
                            color = Color.White.copy(alpha = alphaAnim),
                            start = Offset(center.x - 80f, center.y + 100f),
                            end = Offset(center.x + 80f, center.y - 100f),
                            strokeWidth = 4f
                        )
                        drawCircle(color = SageGreenPrimary, radius = 12f, center = Offset(center.x + 80f, center.y - 100f))
                    } else if (activeStep == 1) {
                        // 5 back blows: impact target radar lines
                        drawCircle(
                            color = AccentOrange.copy(alpha = alphaAnim),
                            radius = 60f + (floatOffsetAnim % 40f),
                            center = center,
                            style = Stroke(width = 3f)
                        )
                        drawCircle(color = AccentOrange, radius = 15f, center = center)
                    } else if (activeStep == 2) {
                        // 5 abdominal thrusts: upward thrust arrow
                        val arrowYStart = center.y + 100f
                        val animOffset = (floatOffsetAnim / 100f) * 140f
                        val arrowYEnd = arrowYStart - animOffset
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowYStart), end = Offset(center.x, arrowYEnd), strokeWidth = 14f, cap = StrokeCap.Round)
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowYEnd), end = Offset(center.x - 25f, arrowYEnd + 20f), strokeWidth = 14f, cap = StrokeCap.Round)
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowYEnd), end = Offset(center.x + 25f, arrowYEnd + 20f), strokeWidth = 14f, cap = StrokeCap.Round)
                    } else {
                        // Repeat cycle: looping arrows
                        drawCircle(
                            color = Color.White.copy(alpha = 0.5f),
                            radius = 70f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        drawCircle(
                            color = SageGreenPrimary.copy(alpha = alphaAnim),
                            radius = 12f,
                            center = Offset(center.x + 70f, center.y)
                        )
                    }
                }
            }
            "fracture" -> {
                if (isPlaying) {
                    if (activeStep == 0) {
                        // Keep limb still: lock icon & shield perimeter
                        drawCircle(
                            color = Color(0xFFB0BEC5).copy(alpha = alphaAnim),
                            radius = 110f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        // Lock graphic
                        drawRect(color = Color.White, topLeft = Offset(center.x - 20f, center.y), size = Size(40f, 30f))
                        drawCircle(color = Color.White, radius = 12f, center = Offset(center.x, center.y), style = Stroke(width = 4f))
                    } else if (activeStep == 1) {
                        // Support with splint: wooden panels on side
                        val splitHeight = 150f
                        drawRoundRect(
                            color = Color(0xFF8D6E63),
                            topLeft = Offset(center.x - 85f, center.y - splitHeight),
                            size = Size(20f, splitHeight * 2),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                        drawRoundRect(
                            color = Color(0xFF8D6E63),
                            topLeft = Offset(center.x + 65f, center.y - splitHeight),
                            size = Size(20f, splitHeight * 2),
                            cornerRadius = CornerRadius(4f, 4f)
                        )
                    } else if (activeStep == 2) {
                        // Apply ice wrapped in cloth: ice crystals radiating
                        val progress = floatOffsetAnim / 100f
                        for (i in 0..5) {
                            val angle = i * Math.PI / 3
                            val r = 30f + progress * 70f
                            val ix = center.x + Math.cos(angle).toFloat() * r
                            val iy = center.y + Math.sin(angle).toFloat() * r
                            drawRect(
                                color = Color(0xFF80DEEA).copy(alpha = 1f - progress),
                                topLeft = Offset(ix - 10f, iy - 10f),
                                size = Size(20f, 20f)
                            )
                        }
                    } else {
                        // Call ambulance: glowing emergency beacon pulses
                        drawCircle(
                            color = AccentRed.copy(alpha = alphaAnim * 0.7f),
                            radius = 130f,
                            center = center,
                            style = Stroke(width = 4f)
                        )
                        drawCircle(color = AccentRed, radius = 25f, center = center)
                    }
                }
            }
            "snakebite" -> {
                if (isPlaying) {
                    if (activeStep == 0) {
                        // Keep patient completely still: calming deep blue heartbeat waves
                        val pulseRadius = 50f + (floatOffsetAnim % 100f) * 1.2f
                        drawCircle(
                            color = Color(0xFF0D47A1).copy(alpha = (1f - floatOffsetAnim / 100f).coerceIn(0f, 1f)),
                            radius = pulseRadius,
                            center = center,
                            style = Stroke(width = 3f)
                        )
                    } else if (activeStep == 1) {
                        // Keep bite below heart level: downward flowing pointer arrows
                        val arrowY = center.y + 60f + (floatOffsetAnim % 50f)
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowY), end = Offset(center.x, arrowY + 35f), strokeWidth = 8f, cap = StrokeCap.Round)
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowY + 35f), end = Offset(center.x - 12f, arrowY + 20f), strokeWidth = 8f, cap = StrokeCap.Round)
                        drawLine(color = AccentOrange, start = Offset(center.x, arrowY + 35f), end = Offset(center.x + 12f, arrowY + 20f), strokeWidth = 8f, cap = StrokeCap.Round)
                    } else if (activeStep == 2) {
                        // Wash gently: clean water droplets or protective bounding shield
                        drawCircle(
                            color = Color(0xFF00E5FF).copy(alpha = alphaAnim),
                            radius = 70f,
                            center = center,
                            style = Stroke(width = 3f)
                        )
                        for (i in 0..8) {
                            val rx = center.x - 50f + (i * 12f)
                            val ry = center.y - 40f + sin((floatOffsetAnim + i * 30f) * 0.15f) * 10f
                            drawCircle(color = Color(0xFFE0F7FA), radius = 4f, center = Offset(rx, ry))
                        }
                    } else {
                        // Hospital anti-venom: pulsing medical green cross with wide sonar
                        drawCircle(
                            color = SageGreenPrimary.copy(alpha = alphaAnim * 0.4f),
                            radius = 140f,
                            center = center,
                            style = Stroke(width = 5f)
                        )
                        drawLine(color = SageGreenPrimary, start = Offset(center.x, center.y - 20f), end = Offset(center.x, center.y + 20f), strokeWidth = 8f)
                        drawLine(color = SageGreenPrimary, start = Offset(center.x - 20f, center.y), end = Offset(center.x + 20f, center.y), strokeWidth = 8f)
                    }
                }
            }
            "bleeding" -> {
                if (isPlaying) {
                    if (activeStep == 0) {
                        // Direct pressure: Concentric contracting force rings
                        val scale = 1.3f - (floatOffsetAnim % 100f) / 100f * 0.5f
                        drawCircle(
                            color = AccentRed.copy(alpha = 0.8f),
                            radius = 85f * scale,
                            center = center,
                            style = Stroke(width = 6f)
                        )
                    } else if (activeStep == 1) {
                        // Elevate limb: Upward floating energy signals
                        val up = (floatOffsetAnim / 100f) * 110f
                        drawLine(color = Color.White, start = Offset(center.x, center.y + 40f - up), end = Offset(center.x, center.y - up), strokeWidth = 6f, cap = StrokeCap.Round)
                        drawLine(color = Color.White, start = Offset(center.x, center.y - up), end = Offset(center.x - 15f, center.y + 15f - up), strokeWidth = 6f, cap = StrokeCap.Round)
                        drawLine(color = Color.White, start = Offset(center.x, center.y - up), end = Offset(center.x + 15f, center.y + 15f - up), strokeWidth = 6f, cap = StrokeCap.Round)
                    } else if (activeStep == 2) {
                        // Pressure bandage wrap
                        val progress = floatOffsetAnim / 100f
                        drawCircle(
                            color = ForestGreenAccent.copy(alpha = alphaAnim * 0.8f),
                            radius = 40f + progress * 55f,
                            center = center,
                            style = Stroke(width = 5f)
                        )
                    } else {
                        // Apply additional dressing over the first: Draw concentric layers
                        drawCircle(
                            color = Color.White.copy(alpha = 0.8f),
                            radius = 65f,
                            center = center,
                            style = Stroke(width = 6f)
                        )
                        drawCircle(
                            color = SageGreenPrimary.copy(alpha = alphaAnim),
                            radius = 85f,
                            center = center,
                            style = Stroke(width = 8f)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ComplianceButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor else Color.LightGray.copy(alpha = 0.15f))
            .border(
                width = 1.5.dp,
                color = if (isSelected) activeColor else Color.LightGray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextDark,
            textAlign = TextAlign.Center
        )
    }
}

fun Bitmap.toBase64(): String {
    val outputStream = java.io.ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
    return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.NO_WRAP)
}

fun Bitmap.resizeToMax(maxDimension: Int): Bitmap {
    if (width <= maxDimension && height <= maxDimension) return this
    val aspectRatio = width.toFloat() / height.toFloat()
    val newWidth = if (width > height) maxDimension else (maxDimension * aspectRatio).toInt()
    val newHeight = if (height > width) maxDimension else (maxDimension / aspectRatio).toInt()
    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}
