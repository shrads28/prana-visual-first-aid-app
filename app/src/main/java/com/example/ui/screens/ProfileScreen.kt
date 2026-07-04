package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.MedicalProfile
import com.example.ui.PranaViewModel
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.ForestGreenAccent
import com.example.ui.theme.OliveGreenSecondary
import com.example.ui.theme.SageGreenPrimary
import com.example.ui.theme.TextDark
import com.example.ui.theme.WarmBeige

@Composable
fun ProfileScreen(viewModel: PranaViewModel) {
    val context = LocalContext.current
    val isHindi = viewModel.preferredLanguage.collectAsState().value == "hi"
    val profileState by viewModel.medicalProfile.collectAsState()

    // Form states
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("O+") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var diabetes by remember { mutableStateOf(false) }
    var hypertension by remember { mutableStateOf(false) }
    var pregnancy by remember { mutableStateOf(false) }
    var currentMedications by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }
    var contact1Name by remember { mutableStateOf("") }
    var contact1Phone by remember { mutableStateOf("") }
    var contact2Name by remember { mutableStateOf("") }
    var contact2Phone by remember { mutableStateOf("") }

    var dropdownExpanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

    // Populate fields when profile loads
    LaunchedEffect(profileState) {
        profileState?.let { prof ->
            name = prof.name
            age = if (prof.age > 0) prof.age.toString() else ""
            bloodGroup = prof.bloodGroup
            weight = if (prof.weight > 0) prof.weight.toString() else ""
            height = if (prof.height > 0) prof.height.toString() else ""
            allergies = prof.allergies
            diabetes = prof.diabetes
            hypertension = prof.hypertension
            pregnancy = prof.pregnancy
            currentMedications = prof.currentMedications
            medicalConditions = prof.medicalConditions
            contact1Name = prof.contact1Name
            contact1Phone = prof.contact1Phone
            contact2Name = prof.contact2Name
            contact2Phone = prof.contact2Phone
        }
    }

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
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = ForestGreenAccent,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = if (isHindi) "व्यक्तिगत चिकित्सा प्रोफ़ाइल" else "Personal Medical Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenAccent
        )

        Text(
            text = if (isHindi) "यह विवरण स्थानीय रूप से सुरक्षित रूप से संग्रहीत किया जाता है।" else "Keep your records updated offline. Syncs only with emergency responders.",
            fontSize = 13.sp,
            color = OliveGreenSecondary,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // General Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (isHindi) "सामान्य विवरण (General Details)" else "General Vitals",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenAccent
                )

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (isHindi) "पूरा नाम" else "Full Name") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary, focusedLabelColor = ForestGreenAccent),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Age Input
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text(if (isHindi) "उम्र" else "Age") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary, focusedLabelColor = ForestGreenAccent),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("profile_age_input")
                    )

                    // Blood Group Picker
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = bloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(if (isHindi) "रक्त समूह" else "Blood Group") },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                            shape = RoundedCornerShape(14.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { dropdownExpanded = true }
                                )
                            },
                            modifier = Modifier.fillMaxWidth().clickable { dropdownExpanded = true }.testTag("profile_blood_group_dropdown")
                        )

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            bloodGroups.forEach { bg ->
                                DropdownMenuItem(
                                    text = { Text(bg) },
                                    onClick = {
                                        bloodGroup = bg
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Weight
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text(if (isHindi) "वजन (kg)" else "Weight (kg)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("profile_weight_input")
                    )

                    // Height
                    OutlinedTextField(
                        value = height,
                        onValueChange = { height = it },
                        label = { Text(if (isHindi) "ऊंचाई (cm)" else "Height (cm)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).testTag("profile_height_input")
                    )
                }
            }
        }

        // Emergency Health conditions card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (isHindi) "चिकित्सा स्थिति विवरण" else "Medical Conditions & History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenAccent
                )

                OutlinedTextField(
                    value = allergies,
                    onValueChange = { allergies = it },
                    label = { Text(if (isHindi) "एलर्जी (दवा/भोजन)" else "Allergies (Meds/Food)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().testTag("profile_allergies_input")
                )

                OutlinedTextField(
                    value = currentMedications,
                    onValueChange = { currentMedications = it },
                    label = { Text(if (isHindi) "वर्तमान दवाएं" else "Current Medications") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Switches for quick vitals checking
                VitalsSwitchRow(
                    label = if (isHindi) "मधुमेह (Diabetes)" else "Diabetes Patient",
                    checked = diabetes,
                    onCheckedChange = { diabetes = it }
                )

                VitalsSwitchRow(
                    label = if (isHindi) "उच्च रक्तचाप (Hypertension)" else "Hypertension (BP)",
                    checked = hypertension,
                    onCheckedChange = { hypertension = it }
                )

                VitalsSwitchRow(
                    label = if (isHindi) "गर्भावस्था (Pregnancy)" else "Pregnancy Status",
                    checked = pregnancy,
                    onCheckedChange = { pregnancy = it }
                )
            }
        }

        // Emergency Contacts Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (isHindi) "आपातकालीन संपर्क (SOS Contacts)" else "Emergency Contacts (SOS Contacts)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenAccent
                )

                // Primary Contact
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isHindi) "मुख्य संपर्क (Primary Contact)" else "Primary Contact",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OliveGreenSecondary
                    )
                    OutlinedTextField(
                        value = contact1Name,
                        onValueChange = { contact1Name = it },
                        label = { Text(if (isHindi) "नाम" else "Contact Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("profile_contact_name_input")
                    )
                    OutlinedTextField(
                        value = contact1Phone,
                        onValueChange = { contact1Phone = it },
                        label = { Text(if (isHindi) "मोबाइल नंबर" else "Mobile Phone Number") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("profile_contact_phone_input")
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Secondary Contact
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (isHindi) "वैकल्पिक संपर्क (Secondary Contact)" else "Secondary Contact",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OliveGreenSecondary
                    )
                    OutlinedTextField(
                        value = contact2Name,
                        onValueChange = { contact2Name = it },
                        label = { Text(if (isHindi) "वैकल्पिक नाम" else "Optional Contact Name") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contact2Phone,
                        onValueChange = { contact2Phone = it },
                        label = { Text(if (isHindi) "वैकल्पिक मोबाइल" else "Optional Contact Phone") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SageGreenPrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Save Action button
        Button(
            onClick = {
                val updatedProf = MedicalProfile(
                    name = name,
                    age = age.toIntOrNull() ?: 0,
                    bloodGroup = bloodGroup,
                    weight = weight.toDoubleOrNull() ?: 0.0,
                    height = height.toDoubleOrNull() ?: 0.0,
                    allergies = allergies,
                    diabetes = diabetes,
                    hypertension = hypertension,
                    pregnancy = pregnancy,
                    currentMedications = currentMedications,
                    medicalConditions = medicalConditions,
                    contact1Name = contact1Name,
                    contact1Phone = contact1Phone,
                    contact2Name = contact2Name,
                    contact2Phone = contact2Phone,
                    preferredLanguage = profileState?.preferredLanguage ?: "en",
                    preferredVoice = profileState?.preferredVoice ?: "female"
                )
                viewModel.updateMedicalProfile(updatedProf)
                Toast.makeText(context, if (isHindi) "प्रोफ़ाइल सफलतापूर्वक सहेजी गई!" else "Medical profile saved successfully offline!", Toast.LENGTH_LONG).show()
                viewModel.speak(if (isHindi) "प्रोफ़ाइल सुरक्षित कर दी गई है।" else "Medical profile updated successfully.")
            },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenAccent),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("profile_save_button")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isHindi) "प्रोफ़ाइल सहेजें" else "Save Medical Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun VitalsSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SageGreenPrimary,
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.LightGray.copy(alpha = 0.3f)
            )
        )
    }
}
