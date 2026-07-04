package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.PranaViewModel
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(viewModel: PranaViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentTab by viewModel.currentTab.collectAsState()
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val profile by viewModel.medicalProfile.collectAsState()

    var showAboutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    // Synchronize UI state drawerState with ViewModel state if needed, or manage locally
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BackgroundOffWhite,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .testTag("modal_drawer_sheet")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = null,
                            tint = ForestGreenAccent,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "PRANA",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenAccent,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = if (isHindi) "ऑफ़लाइन प्राथमिक चिकित्सा" else "Offline Medical Guide",
                                fontSize = 11.sp,
                                color = OliveGreenSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Drawer Items
                    DrawerItem(
                        icon = Icons.Default.Home,
                        label = if (isHindi) "होम (Home)" else "Home",
                        selected = currentTab == "home",
                        testTag = "drawer_home_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "home"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.MedicalInformation,
                        label = if (isHindi) "चिकित्सा प्रोफ़ाइल" else "My Medical Profile",
                        selected = currentTab == "profile",
                        testTag = "drawer_profile_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "profile"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.ContactPage,
                        label = if (isHindi) "आपातकालीन संपर्क" else "Emergency Contacts",
                        selected = currentTab == "emergency",
                        testTag = "drawer_contacts_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "emergency"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Bookmark,
                        label = if (isHindi) "सहेजे गए वीडियो" else "Saved Videos",
                        selected = false,
                        testTag = "drawer_saved_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "learn"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Download,
                        label = if (isHindi) "ऑफ़लाइन डाउनलोड" else "Offline Downloads",
                        selected = false,
                        testTag = "drawer_downloads_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "learn"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Language,
                        label = if (isHindi) "भाषा (Language)" else "Language Selection",
                        selected = false,
                        testTag = "drawer_language_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "settings"
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Settings,
                        label = if (isHindi) "सेटिंग्स (Settings)" else "Settings",
                        selected = false,
                        testTag = "drawer_settings_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            viewModel.currentTab.value = "settings"
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray.copy(alpha = 0.4f)))
                    Spacer(modifier = Modifier.height(10.dp))

                    DrawerItem(
                        icon = Icons.Default.Info,
                        label = if (isHindi) "सहायता (Help)" else "Help",
                        selected = false,
                        testTag = "drawer_help_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            showHelpDialog = true
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.Info,
                        label = if (isHindi) "ऐप के बारे में" else "About Prana",
                        selected = false,
                        testTag = "drawer_about_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            showAboutDialog = true
                        }
                    )

                    DrawerItem(
                        icon = Icons.Default.PrivacyTip,
                        label = if (isHindi) "गोपनीयता नीति" else "Privacy Policy",
                        selected = false,
                        testTag = "drawer_privacy_button",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            Toast.makeText(context, if (isHindi) "यह ऑफलाइन ऐप सुरक्षित है।" else "Privacy policy: 100% secure local offline logs.", Toast.LENGTH_LONG).show()
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentTab != "ar") {
                    val profileLetter = profile?.name?.firstOrNull()?.toString()?.uppercase() ?: "P"

                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Current Tab Title
                                Text(
                                    text = getTabTitle(currentTab, isHindi),
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenAccent,
                                    fontSize = 18.sp
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    // Dynamic Language Toggle Pill
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(WarmBeige)
                                            .border(androidx.compose.foundation.BorderStroke(1.dp, Color.White), CircleShape)
                                            .clickable {
                                                coroutineScope.launch {
                                                    val newLang = if (isHindi) "en" else "hi"
                                                    viewModel.changeLanguage(newLang)
                                                    viewModel.speak(if (newLang == "hi") "भाषा हिंदी कर दी गई है" else "Language changed to English")
                                                }
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "हि",
                                            fontSize = 11.sp,
                                            fontWeight = if (isHindi) FontWeight.ExtraBold else FontWeight.Normal,
                                            color = ForestGreenAccent,
                                            modifier = Modifier.graphicsLayer(alpha = if (isHindi) 1f else 0.5f)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(3.dp)
                                                .background(ForestGreenAccent, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "EN",
                                            fontSize = 11.sp,
                                            fontWeight = if (!isHindi) FontWeight.ExtraBold else FontWeight.Normal,
                                            color = ForestGreenAccent,
                                            modifier = Modifier.graphicsLayer(alpha = if (!isHindi) 1f else 0.5f)
                                        )
                                    }

                                    // Beautiful Serif Avatar
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(SageGreenPrimary)
                                            .border(androidx.compose.foundation.BorderStroke(1.5.dp, Color.White), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = profileLetter,
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                                        )
                                    }
                                }
                            }
                        },
                        navigationIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 8.dp)
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, WarmBeige), CircleShape)
                                    .clickable { coroutineScope.launch { drawerState.open() } }
                                    .testTag("navigation_drawer_trigger"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu Drawer",
                                    tint = ForestGreenAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundOffWhite)
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    NavigationBarItem(
                        selected = currentTab == "home",
                        onClick = { viewModel.currentTab.value = "home" },
                        icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                        label = { Text(if (isHindi) "मुख्य" else "Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ForestGreenAccent,
                            selectedTextColor = ForestGreenAccent,
                            indicatorColor = SageGreenPrimary.copy(alpha = 0.25f),
                            unselectedIconColor = OliveGreenSecondary,
                            unselectedTextColor = OliveGreenSecondary
                        ),
                        modifier = Modifier.testTag("nav_home_tab")
                    )

                    NavigationBarItem(
                        selected = currentTab == "ar",
                        onClick = { viewModel.currentTab.value = "ar" },
                        icon = { Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "AR First Aid") },
                        label = { Text(if (isHindi) "एआर उपचार" else "AR First Aid", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ForestGreenAccent,
                            selectedTextColor = ForestGreenAccent,
                            indicatorColor = SageGreenPrimary.copy(alpha = 0.25f),
                            unselectedIconColor = OliveGreenSecondary,
                            unselectedTextColor = OliveGreenSecondary
                        ),
                        modifier = Modifier.testTag("nav_ar_tab")
                    )

                    NavigationBarItem(
                        selected = currentTab == "emergency",
                        onClick = { viewModel.currentTab.value = "emergency" },
                        icon = {
                            BadgedBox(
                                badge = { Badge(containerColor = AccentRed) { Text("SOS", color = Color.White, fontSize = 9.sp) } }
                            ) {
                                Icon(imageVector = Icons.Default.Warning, contentDescription = "Emergency")
                            }
                        },
                        label = { Text(if (isHindi) "आपातकाल" else "Emergency", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ForestGreenAccent,
                            selectedTextColor = ForestGreenAccent,
                            indicatorColor = SageGreenPrimary.copy(alpha = 0.25f),
                            unselectedIconColor = OliveGreenSecondary,
                            unselectedTextColor = OliveGreenSecondary
                        ),
                        modifier = Modifier.testTag("nav_emergency_tab")
                    )

                    NavigationBarItem(
                        selected = currentTab == "learn",
                        onClick = { viewModel.currentTab.value = "learn" },
                        icon = { Icon(imageVector = Icons.Default.Info, contentDescription = "Learn") },
                        label = { Text(if (isHindi) "सीखें" else "Learn", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ForestGreenAccent,
                            selectedTextColor = ForestGreenAccent,
                            indicatorColor = SageGreenPrimary.copy(alpha = 0.25f),
                            unselectedIconColor = OliveGreenSecondary,
                            unselectedTextColor = OliveGreenSecondary
                        ),
                        modifier = Modifier.testTag("nav_learn_tab")
                    )

                    NavigationBarItem(
                        selected = currentTab == "profile",
                        onClick = { viewModel.currentTab.value = "profile" },
                        icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text(if (isHindi) "प्रोफ़ाइल" else "Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ForestGreenAccent,
                            selectedTextColor = ForestGreenAccent,
                            indicatorColor = SageGreenPrimary.copy(alpha = 0.25f),
                            unselectedIconColor = OliveGreenSecondary,
                            unselectedTextColor = OliveGreenSecondary
                        ),
                        modifier = Modifier.testTag("nav_profile_tab")
                    )
                }
            },
            floatingActionButton = {
                if (currentTab != "emergency" && currentTab != "ar") {
                    FloatingActionButton(
                        onClick = { viewModel.currentTab.value = "emergency" },
                        containerColor = AccentRed,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(62.dp)
                            .border(androidx.compose.foundation.BorderStroke(3.dp, Color.White), CircleShape)
                            .testTag("floating_sos_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency SOS Quick launch",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    "home" -> HomeScreen(viewModel)
                    "ar" -> ArFirstAidScreen(viewModel)
                    "emergency" -> EmergencyScreen(viewModel)
                    "learn" -> LearnScreen(viewModel)
                    "profile" -> ProfileScreen(viewModel)
                    "settings" -> SettingsScreen(viewModel)
                }
            }
        }
    }

    // Help Dialog
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text(if (isHindi) "आपकी सहायता" else "Quick User Help Guide") },
            text = {
                Text(
                    text = if (isHindi) {
                        "1. प्राथमिक उपचार शुरू करने के लिए 'एआर उपचार' टैब पर जाएं।\n2. बिना टच स्क्रीन नियंत्रित करने के लिए हमारे जेस्चर पैनल का उपयोग करें।\n3. आपातकाल में सीधे 102 पर कॉल करने के लिए एम्बुलेंस कार्ड दबाएं।"
                    } else {
                        "1. Choose any medical category inside AR first aid to open the camera overlays.\n2. Tap 'Thumbs Up' gesture keys at the bottom to progress hands-free.\n3. Keep your Medical Profile completed offline to display crucial vitals to rescue teams instantly."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("OK")
                }
            },
            containerColor = Color.White
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = { Text(if (isHindi) "प्राण के बारे में" else "About Prana AI Assistant") },
            text = {
                Text(
                    text = if (isHindi) {
                        "प्राण ग्रामीण और निरक्षर उपयोगकर्ताओं की सहायता करने वाला भारत का पहला ऑफलाइन संवर्धित वास्तविकता (AR) प्राथमिक चिकित्सा सहायक है।"
                    } else {
                        "Prana is a modern offline-first medical assistant dedicated to bringing high-quality first-aid instruction to users with limited literacy or internet connectivity."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) SageGreenPrimary.copy(alpha = 0.25f) else Color.Transparent)
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) ForestGreenAccent else OliveGreenSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (selected) ForestGreenAccent else TextDark,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

fun getTabTitle(tab: String, isHindi: Boolean): String {
    return when (tab) {
        "home" -> if (isHindi) "प्राण होम" else "Prana Dashboard"
        "ar" -> if (isHindi) "एआर उपचार निर्देश" else "AR Visual Guide"
        "emergency" -> if (isHindi) "आपातकालीन नियंत्रण" else "Emergency Assistance"
        "learn" -> if (isHindi) "चिकित्सा सीखें" else "First Aid Tutorials"
        "profile" -> if (isHindi) "चिकित्सा विवरण" else "Medical ID"
        "settings" -> if (isHindi) "ऐप सेटिंग्स" else "Settings"
        else -> "Prana"
    }
}
