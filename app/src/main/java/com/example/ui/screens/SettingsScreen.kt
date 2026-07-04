package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PranaViewModel
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun SettingsScreen(viewModel: PranaViewModel) {
    val context = LocalContext.current
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val preferredVoice by viewModel.preferredVoice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(SageGreenPrimary.copy(alpha = 0.2f), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = ForestGreenAccent,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = if (isHindi) "सेटिंग्स (Settings)" else "Settings & Controls",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Language settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = SageGreenPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isHindi) "ऐप भाषा (App Language)" else "App Language",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.changeLanguage("en") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isHindi) ForestGreenAccent else WarmBeige
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("settings_lang_en_button")
                    ) {
                        Text(
                            text = "English",
                            color = if (!isHindi) Color.White else ForestGreenAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { viewModel.changeLanguage("hi") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHindi) ForestGreenAccent else WarmBeige
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("settings_lang_hi_button")
                    ) {
                        Text(
                            text = "हिन्दी",
                            color = if (isHindi) Color.White else ForestGreenAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Voice Guidance Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.RecordVoiceOver, contentDescription = null, tint = SageGreenPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isHindi) "आवाज का चयन" else "AI Voice Guidance",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.changeVoice("female") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (preferredVoice == "female") ForestGreenAccent else WarmBeige
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("settings_voice_female_button")
                    ) {
                        Text(
                            text = if (isHindi) "महिला (Female)" else "Female Voice",
                            color = if (preferredVoice == "female") Color.White else ForestGreenAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.changeVoice("male") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (preferredVoice == "male") ForestGreenAccent else WarmBeige
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("settings_voice_male_button")
                    ) {
                        Text(
                            text = if (isHindi) "पुरुष (Male)" else "Male Voice",
                            color = if (preferredVoice == "male") Color.White else ForestGreenAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Cache & Local Storage clearance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null, tint = SageGreenPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isHindi) "स्टोरेज और डेटा प्रबंधन" else "Data Management",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                Text(
                    text = if (isHindi) "अपने इलाज का इतिहास या डाउनलोड की गई ऑफलाइन वीडियो साफ करें।" else "Clear local treatment log registers or release downloaded video offline storage.",
                    fontSize = 12.sp,
                    color = OliveGreenSecondary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.clearAllHistory()
                            Toast.makeText(context, if (isHindi) "इतिहास साफ़ किया गया!" else "Treatment history logs wiped!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmBeige),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("settings_clear_history_button")
                    ) {
                        Text(
                            text = if (isHindi) "इतिहास साफ़ करें" else "Clear Logs",
                            color = ForestGreenAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.offlinePacks.value.forEach { viewModel.removeOfflinePack(it.id) }
                            Toast.makeText(context, if (isHindi) "वीडियो कैश साफ किया गया!" else "Downloaded videos cache removed!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmBeige),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("settings_clear_cache_button")
                    ) {
                        Text(
                            text = if (isHindi) "कैश साफ़ करें" else "Clear Cache",
                            color = ForestGreenAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // App Information card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = WarmBeige.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = ForestGreenAccent)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "PRANA v1.0.0 (Prototype)",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenAccent
                    )
                }

                Text(
                    text = if (isHindi) "प्राण एक ऑफलाइन-प्रथम संवर्धित वास्तविकता (AR) प्राथमिक चिकित्सा सहायक है जो ग्रामीण और निरक्षर उपयोगकर्ताओं की मदद करता है।" else "Prana is an offline-first Augmented Reality healthcare companion that empowers low-literacy users with animated visual guides and voice commands during medical crises.",
                    fontSize = 12.sp,
                    color = OliveGreenSecondary,
                    lineHeight = 18.sp
                )

                Text(
                    text = "Made in India with ❤️",
                    fontSize = 11.sp,
                    color = ForestGreenAccent,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}
