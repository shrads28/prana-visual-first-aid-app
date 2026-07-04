package com.example.ui.screens

import android.Manifest
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PranaViewModel
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: PranaViewModel) {
    val screenState by viewModel.currentScreen.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
    ) {
        when (screenState) {
            "splash" -> SplashView(onTimeout = { viewModel.currentScreen.value = "language_select" })
            "language_select" -> LanguageSelectView(viewModel)
            "permissions" -> PermissionsView(viewModel)
            "login" -> LoginView(viewModel)
        }
    }
}

@Composable
fun SplashView(onTimeout: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(Unit) {
        delay(2200)
        onTimeout()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SageGreenPrimary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = "Prana Logo",
                tint = ForestGreenAccent,
                modifier = Modifier.size(80.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "PRANA",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent,
            letterSpacing = 4.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "AI First Aid Assistant",
            fontSize = 16.sp,
            color = OliveGreenSecondary,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LanguageSelectView(viewModel: PranaViewModel) {
    val selectedLang by viewModel.preferredLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = ForestGreenAccent,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Choose Language\nभाषा चुनें",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )
        Spacer(modifier = Modifier.height(36.dp))

        // English Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("english_language_button")
                .clickable { viewModel.changeLanguage("en") },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedLang == "en") WarmBeige else Color.White
            ),
            border = if (selectedLang == "en") {
                BorderStroke(2.dp, SageGreenPrimary)
            } else {
                BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "English",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Voice assistance in English",
                        fontSize = 14.sp,
                        color = OliveGreenSecondary
                    )
                }
                if (selectedLang == "en") {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Selected",
                        tint = ForestGreenAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Hindi Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .testTag("hindi_language_button")
                .clickable { viewModel.changeLanguage("hi") },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedLang == "hi") WarmBeige else Color.White
            ),
            border = if (selectedLang == "hi") {
                BorderStroke(2.dp, SageGreenPrimary)
            } else {
                BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "हिन्दी (Hindi)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "हिन्दी में आवाज सहायता",
                        fontSize = 14.sp,
                        color = OliveGreenSecondary
                    )
                }
                if (selectedLang == "hi") {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Selected",
                        tint = ForestGreenAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                viewModel.speak(
                    if (selectedLang == "hi") "भाषा सेट हो गई है। आगे बढ़ते हैं।"
                    else "Language set successfully. Let's proceed."
                )
                viewModel.currentScreen.value = "permissions"
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("confirm_language_button")
        ) {
            Text(
                text = if (selectedLang == "hi") "आगे बढ़ें (Continue)" else "Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PermissionsView(viewModel: PranaViewModel) {
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Security",
            tint = ForestGreenAccent,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isHindi) "महत्वपूर्ण अनुमतियाँ" else "Permissions Guide",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isHindi) "प्राथमिक चिकित्सा सहायता प्रदान करने के लिए हमें इन अनुमतियों की आवश्यकता है।" else "Prana needs the following permissions to guide you correctly during an emergency.",
            fontSize = 14.sp,
            color = OliveGreenSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))

        // Camera Permission Row
        PermissionRow(
            icon = Icons.Default.CameraAlt,
            title = if (isHindi) "कैमरा (Camera)" else "Camera Access",
            description = if (isHindi) "कैमरा फीड पर एआर (AR) निर्देश दिखाने के लिए।" else "To overlay visual AR guides directly onto the injury site."
        )

        // Mic Permission Row
        PermissionRow(
            icon = Icons.Default.Mic,
            title = if (isHindi) "माइक (Microphone)" else "Microphone Access",
            description = if (isHindi) "आवाज द्वारा निर्देश सुनने और इशारों को दोहराने के लिए।" else "To enable clear voice assistance narration and interactive controls."
        )

        // Location Permission Row
        PermissionRow(
            icon = Icons.Default.MyLocation,
            title = if (isHindi) "स्थान (Location)" else "Location Services",
            description = if (isHindi) "आपातकाल में आपका सटीक स्थान एम्बुलेंस के साथ साझा करने के लिए।" else "To share GPS coordinates immediately with emergency vehicles."
        )

        // Phone Permission Row
        PermissionRow(
            icon = Icons.Default.Phone,
            title = if (isHindi) "फ़ोन कॉल (Phone Calls)" else "Dialer Access",
            description = if (isHindi) "एक टैप से सीधे एम्बुलेंस या आपातकालीन संपर्क को कॉल करने के लिए।" else "To dial ambulances or local medical centers with one click."
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = {
                viewModel.currentScreen.value = "login"
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("grant_permissions_button")
        ) {
            Text(
                text = if (isHindi) "अनुमति दें और आगे बढ़ें" else "Grant & Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PermissionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(WarmBeige.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(SageGreenPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestGreenAccent,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = OliveGreenSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun LoginView(viewModel: PranaViewModel) {
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    var mobileNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = null,
            tint = ForestGreenAccent,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isHindi) "प्राण में आपका स्वागत है" else "Welcome to Prana",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isHindi) "पंजीकरण करें या सीधे अतिथि के रूप में प्रवेश करें।" else "Secure offline health logs. Create profile or enter instantly as Guest.",
            fontSize = 14.sp,
            color = OliveGreenSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isHindi) "मोबाइल नंबर से लॉगिन करें" else "Login via Mobile Number",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    label = { Text(if (isHindi) "10 अंकों का मोबाइल नंबर" else "10-Digit Mobile Number") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SageGreenPrimary,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
                        focusedLabelColor = ForestGreenAccent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.speak(
                            if (isHindi) "प्राण में आपका स्वागत है। मुख्य स्क्रीन पर चलते हैं।"
                            else "Welcome to Prana. Loading dashboard."
                        )
                        viewModel.currentScreen.value = "main"
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("login_button")
                ) {
                    Text(
                        text = if (isHindi) "ओटीपी प्राप्त करें (Get OTP)" else "Get OTP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isHindi) "अथवा" else "OR",
            fontSize = 14.sp,
            color = OliveGreenSecondary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.speak(
                    if (isHindi) "प्राण में आपका स्वागत है। आप अतिथि के रूप में प्रवेश कर रहे हैं।"
                    else "Welcome to Prana. Entering as guest."
                )
                viewModel.currentScreen.value = "main"
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = BorderStroke(1.dp, ForestGreenAccent),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("continue_as_guest_button")
        ) {
            Text(
                text = if (isHindi) "अतिथि के रूप में जारी रखें (Guest)" else "Continue as Guest",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenAccent
            )
        }
    }
}
