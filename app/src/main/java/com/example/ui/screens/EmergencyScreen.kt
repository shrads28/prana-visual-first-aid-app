package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PranaViewModel
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun EmergencyScreen(viewModel: PranaViewModel) {
    val context = LocalContext.current
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val profile by viewModel.medicalProfile.collectAsState()
    val isFlashActive by viewModel.isFlashlightSosActive.collectAsState()
    val isSirenActive by viewModel.isSirenActive.collectAsState()
    val coordinates by viewModel.currentCoordinates.collectAsState()

    var showIdCard by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isHindi) "त्वरित आपातकालीन सहायता" else "One-Tap SOS Assistance",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = AccentRed,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (isHindi) "त्वरित प्रतिक्रिया के लिए नीचे दिए गए किसी भी बटन को दबाएं।" else "Emergency response. Tap any button below for instant local assistance.",
            fontSize = 14.sp,
            color = OliveGreenSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Large Call Ambulance Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clickable {
                    try {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Dialing 102", Toast.LENGTH_SHORT).show()
                    }
                }
                .testTag("sos_ambulance_button"),
            colors = CardDefaults.cardColors(containerColor = AccentRed),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = if (isHindi) "एम्बुलेंस को बुलाएं" else "Call Ambulance",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isHindi) "हेल्पलाइन नंबर 102 डायल करें" else "Dial National Helpline 102",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Call Emergency Contact Card
        val contactName = profile?.contact1Name ?: ""
        val contactPhone = profile?.contact1Phone ?: ""

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clickable {
                    if (contactPhone.isNotEmpty()) {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactPhone"))
                            context.startActivity(intent)
                        } catch (e: Exception) {}
                    } else {
                        Toast.makeText(context, if (isHindi) "कृपया प्रोफ़ाइल में आपातकालीन संपर्क जोड़ें।" else "Please save an emergency contact in your profile first.", Toast.LENGTH_LONG).show()
                    }
                }
                .testTag("sos_contact_button"),
            colors = CardDefaults.cardColors(containerColor = AccentOrange),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = if (isHindi) "आपातकालीन संपर्क" else "Emergency Contact",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (contactName.isNotEmpty()) "$contactName ($contactPhone)" else (if (isHindi) "कोई संपर्क सहेजा नहीं गया है" else "No saved contact"),
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Row of split secondary actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Flashlight SOS
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable { viewModel.toggleFlashlightSos() }
                    .testTag("sos_flashlight_button"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFlashActive) SageGreenPrimary else Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashlightOn,
                        contentDescription = "Flashlight",
                        tint = if (isFlashActive) Color.White else ForestGreenAccent,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = if (isHindi) "फ़्लैशलाइट SOS" else "Flashlight SOS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isFlashActive) Color.White else TextDark
                        )
                        Text(
                            text = if (isFlashActive) (if (isHindi) "सक्रिय" else "ACTIVE") else (if (isHindi) "पल्स सिग्नल" else "Pulse Signal"),
                            fontSize = 11.sp,
                            color = if (isFlashActive) Color.White.copy(alpha = 0.8f) else OliveGreenSecondary
                        )
                    }
                }
            }

            // Loud Siren Alarm
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(110.dp)
                    .clickable { viewModel.toggleSiren() }
                    .testTag("sos_siren_button"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSirenActive) AccentRed else Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = "Siren",
                        tint = if (isSirenActive) Color.White else AccentRed,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = if (isHindi) "लाउड अलार्म" else "Loud Alarm",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSirenActive) Color.White else TextDark
                        )
                        Text(
                            text = if (isSirenActive) (if (isHindi) "बज रहा है..." else "SIREN BLOWING") else (if (isHindi) "सायरन बजाएं" else "Sound Siren"),
                            fontSize = 11.sp,
                            color = if (isSirenActive) Color.White.copy(alpha = 0.8f) else OliveGreenSecondary
                        )
                    }
                }
            }
        }

        // Share Location Action Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    try {
                        val mapsLink = "https://maps.google.com/?q=$coordinates"
                        val allergiesInfo = if (profile?.allergies?.isNotEmpty() == true) "Allergies: ${profile?.allergies}" else ""
                        val bloodGroupInfo = "Blood Group: ${profile?.bloodGroup ?: "Unknown"}"
                        val shareText = "PRANA EMERGENCY SOS!\nMy Live Location coordinates: $coordinates\nGoogle Maps: $mapsLink\nPatient info: $bloodGroupInfo. $allergiesInfo"

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Location sharing invoked: $coordinates", Toast.LENGTH_SHORT).show()
                    }
                }
                .testTag("sos_share_location_button"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(SageGreenPrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = ForestGreenAccent
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isHindi) "लाइव स्थान साझा करें" else "Share Live Location",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "$coordinates (Tap to send SMS)",
                        fontSize = 12.sp,
                        color = OliveGreenSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = SageGreenPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // View Medical ID Card (For Emergency Responders)
        Button(
            onClick = { showIdCard = !showIdCard },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("sos_toggle_id_card_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "चिकित्सा आईडी कार्ड दिखाएं" else "Show Medical ID Card",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Expanded Medical ID Card overlay
        AnimatedVisibility(
            visible = showIdCard,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarmBeige),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "चिकित्सा आईडी (EMERGENCY CARD)" else "MEDICAL RESPONDER ID",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRed
                        )
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    IdRow(label = if (isHindi) "नाम (Name):" else "Patient Name:", value = profile?.name?.ifEmpty { "Guest Patient" } ?: "Guest Patient")
                    IdRow(label = if (isHindi) "रक्त समूह (Blood Group):" else "Blood Group:", value = profile?.bloodGroup ?: "O+")
                    IdRow(label = if (isHindi) "उम्र (Age):" else "Patient Age:", value = "${profile?.age ?: 0} ${if (isHindi) "वर्ष" else "Years"}")
                    IdRow(
                        label = if (isHindi) "एलर्जी (Allergies):" else "Allergic Reactions:",
                        value = profile?.allergies?.ifEmpty { if (isHindi) "कोई नहीं" else "None Saved" } ?: "None"
                    )
                    IdRow(
                        label = if (isHindi) "वर्तमान दवाएं (Medications):" else "Current Medications:",
                        value = profile?.currentMedications?.ifEmpty { if (isHindi) "कोई नहीं" else "None Saved" } ?: "None"
                    )
                    IdRow(
                        label = if (isHindi) "चिकित्सा स्थिति (Conditions):" else "Medical Conditions:",
                        value = buildString {
                            val list = mutableListOf<String>()
                            if (profile?.diabetes == true) list.add(if (isHindi) "मधुमेह" else "Diabetes")
                            if (profile?.hypertension == true) list.add(if (isHindi) "उच्च रक्तचाप" else "Hypertension")
                            if (profile?.pregnancy == true) list.add(if (isHindi) "गर्भावस्था" else "Pregnancy")
                            val other = profile?.medicalConditions ?: ""
                            if (other.isNotEmpty()) list.add(other)
                            if (list.isEmpty()) append(if (isHindi) "कोई नहीं" else "None Saved")
                            else append(list.joinToString(", "))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun IdRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = OliveGreenSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextDark,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}
