package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.theme.GradientStart
import com.example.ui.theme.GradientEnd
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(viewModel: PranaViewModel) {
    val context = LocalContext.current
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val profile by viewModel.medicalProfile.collectAsState()
    val history by viewModel.treatmentHistories.collectAsState()
    val packs by viewModel.offlinePacks.collectAsState()

    val greetingText = if (isHindi) "आज मैं आपकी क्या सहायता कर सकता हूँ?" else "How can I help you today?"
    val userName = profile?.name?.ifEmpty { if (isHindi) "अतिथि" else "Guest" } ?: (if (isHindi) "अतिथि" else "Guest")

    Box(modifier = Modifier.fillMaxSize().background(BackgroundOffWhite)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Welcomer section (Geometric minimal typography)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = if (isHindi) "नमस्ते, $userName" else "Hello, $userName",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF3A4537),
                        lineHeight = 38.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = greetingText,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenAccent,
                        lineHeight = 30.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "शुरू करने के लिए कार्ड दबाएं या अपनी आवाज़ का उपयोग करें।" else "Tap a card or use voice to begin.",
                        fontSize = 14.sp,
                        color = OliveGreenSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Primary Action 1: Start AR First Aid (Full Width Gradient Card, 28.dp Rounded)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(144.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GradientStart, GradientEnd)
                            )
                        )
                        .clickable { viewModel.currentTab.value = "ar" }
                        .testTag("home_ar_first_aid_card")
                ) {
                    // Decorative background overlay icon
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(120.dp)
                            .graphicsLayer(translationX = 30f, translationY = 30f)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = if (isHindi) "एआर प्राथमिक चिकित्सा" else "Start AR First Aid",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) "चोट पर कैमरा केंद्रित करें" else "Point camera at injury site",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        // Action badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isHindi) "शुरू करें" else "LAUNCH",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Primary Action 2 & 3: Learn First Aid & Medical Profile (Side-by-Side Cards, WarmBeige, 28.dp Rounded)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Learn Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(WarmBeige)
                            .clickable { viewModel.currentTab.value = "learn" }
                            .testTag("home_learn_card")
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalHospital,
                                    contentDescription = null,
                                    tint = ForestGreenAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = if (isHindi) "चिकित्सा सीखें" else "Learn First Aid",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenAccent
                            )
                        }
                    }

                    // Profile Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(WarmBeige)
                            .clickable { viewModel.currentTab.value = "profile" }
                            .testTag("home_profile_card")
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MedicalInformation,
                                    contentDescription = null,
                                    tint = ForestGreenAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                text = if (isHindi) "चिकित्सा प्रोफ़ाइल" else "Medical Profile",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenAccent
                            )
                        }
                    }
                }
            }

            // Primary Action 4: Emergency Help (SOS) Card (Full Width Beautiful Minimal Card, 28.dp Rounded)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(AccentRed.copy(alpha = 0.12f))
                        .clickable { viewModel.currentTab.value = "emergency" }
                        .testTag("home_emergency_card")
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AccentRed,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isHindi) "आपातकालीन नियंत्रण" else "Emergency Assistance",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                text = if (isHindi) "एक टैप में सहायता टूल्स" else "One-Tap local emergency SOS tools",
                                fontSize = 12.sp,
                                color = OliveGreenSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = AccentRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Quick Actions Segment
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = (if (isHindi) "त्वरित कार्रवाई" else "Quick Actions").uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = SageGreenPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Call Ambulance Banner styled like Quick Action
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .border(androidx.compose.foundation.BorderStroke(1.5.dp, WarmBeige), RoundedCornerShape(24.dp))
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    viewModel.speak(if (isHindi) "एम्बुलेंस नंबर डायल किया जा रहा है: 102" else "Dialing ambulance number 102")
                                }
                            }
                            .testTag("call_ambulance_button")
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(BackgroundOffWhite, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = AccentRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isHindi) "एम्बुलेंस को बुलाएं (102)" else "Call Ambulance (102)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = if (isHindi) "आपातकालीन चिकित्सा वाहन के लिए डायल करें" else "Directly dials 102 emergency medical helpline",
                                    fontSize = 12.sp,
                                    color = OliveGreenSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = ForestGreenAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Emergency Contacts Quick Glance
            item {
                val contactName = profile?.contact1Name ?: ""
                val contactPhone = profile?.contact1Phone ?: ""

                if (contactName.isNotEmpty() && contactPhone.isNotEmpty()) {
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .border(androidx.compose.foundation.BorderStroke(1.5.dp, WarmBeige), RoundedCornerShape(24.dp))
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactPhone"))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {}
                                }
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(BackgroundOffWhite, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = ForestGreenAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = if (isHindi) "आपातकालीन संपर्क को कॉल करें" else "Call Emergency Contact",
                                        fontSize = 12.sp,
                                        color = OliveGreenSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "$contactName ($contactPhone)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Offline Packs segment
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (if (isHindi) "ऑफ़लाइन पैक" else "Offline First-Aid Packs").uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = SageGreenPrimary
                        )
                        Text(
                            text = if (isHindi) "सभी सुरक्षित करें" else "Save All Offline",
                            fontSize = 12.sp,
                            color = ForestGreenAccent,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                packs.forEach { viewModel.downloadOfflinePack(it.id) }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    packs.take(3).forEach { pack ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White)
                                .border(androidx.compose.foundation.BorderStroke(1.5.dp, WarmBeige), RoundedCornerShape(24.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(BackgroundOffWhite, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (pack.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                            contentDescription = null,
                                            tint = if (pack.isDownloaded) ForestGreenAccent else OliveGreenSecondary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (isHindi) pack.titleHindi else pack.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                        Text(
                                            text = if (pack.isDownloaded) {
                                                if (isHindi) "ऑफ़लाइन उपलब्ध (100% सुरक्षित)" else "Saved Offline (100% secure)"
                                            } else if (pack.downloadProgress > 0f) {
                                                if (isHindi) "डाउनलोड हो रहा है... ${((pack.downloadProgress)*100).toInt()}%" else "Downloading... ${((pack.downloadProgress)*100).toInt()}%"
                                            } else {
                                                if (isHindi) "टैप करके डाउनलोड करें" else "Tap to download"
                                            },
                                            fontSize = 12.sp,
                                            color = OliveGreenSecondary
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            if (pack.isDownloaded) {
                                                viewModel.removeOfflinePack(pack.id)
                                            } else {
                                                viewModel.downloadOfflinePack(pack.id)
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (pack.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                            contentDescription = "Download",
                                            tint = if (pack.isDownloaded) ForestGreenAccent else SageGreenPrimary
                                        )
                                    }
                                }

                                if (pack.downloadProgress > 0f && pack.downloadProgress < 1f) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    LinearProgressIndicator(
                                        progress = pack.downloadProgress,
                                        color = SageGreenPrimary,
                                        trackColor = Color.LightGray.copy(alpha = 0.3f),
                                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recent Treatment History stream
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = (if (isHindi) "हाल के प्राथमिक उपचार" else "Recent Treatments Log").uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = SageGreenPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (history.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(WarmBeige)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isHindi) "अभी तक कोई प्राथमिक उपचार दर्ज नहीं है।" else "No recent treatment logs found. Your saved actions will stream here.",
                                fontSize = 13.sp,
                                color = OliveGreenSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    } else {
                        history.take(4).forEach { log ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.White)
                                    .border(androidx.compose.foundation.BorderStroke(1.5.dp, WarmBeige), RoundedCornerShape(24.dp))
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(BackgroundOffWhite, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = null,
                                            tint = ForestGreenAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = formatInjuryName(log.injury, isHindi),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                        Text(
                                            text = "${log.stepsCompleted}/${log.totalSteps} ${if (isHindi) "कदम पूरे हुए" else "steps completed"} • ${log.durationSeconds}s",
                                            fontSize = 12.sp,
                                            color = OliveGreenSecondary
                                        )
                                    }
                                    Text(
                                        text = formatDateTime(log.date),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Spacing for floating button padding
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun PrimaryActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = OliveGreenSecondary,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

fun formatInjuryName(injury: String, isHindi: Boolean): String {
    return when (injury) {
        "burns" -> if (isHindi) "जलन का उपचार" else "Burns & Scalds"
        "cuts" -> if (isHindi) "कट और रक्तस्राव" else "Cuts & Minor Bleeding"
        "cpr" -> if (isHindi) "सीपीआर (CPR)" else "CPR Resuscitation"
        "choking" -> if (isHindi) "दम घुटना" else "Choking First Aid"
        "fracture" -> if (isHindi) "हड्डी टूटना" else "Fracture Management"
        "snakebite" -> if (isHindi) "सांप का काटना" else "Snake Bite"
        "bleeding" -> if (isHindi) "गंभीर रक्तस्राव" else "Severe Bleeding"
        else -> injury.capitalize(Locale.ROOT)
    }
}

fun formatDateTime(timestamp: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        val netDate = Date(timestamp)
        sdf.format(netDate)
    } catch (e: Exception) {
        "Just now"
    }
}
