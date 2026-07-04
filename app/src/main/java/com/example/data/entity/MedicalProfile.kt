package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_profiles")
data class MedicalProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val age: Int = 0,
    val bloodGroup: String = "O+",
    val weight: Double = 0.0,
    val height: Double = 0.0,
    val allergies: String = "",
    val diabetes: Boolean = false,
    val hypertension: Boolean = false,
    val pregnancy: Boolean = false,
    val currentMedications: String = "",
    val medicalConditions: String = "",
    val contact1Name: String = "",
    val contact1Phone: String = "",
    val contact2Name: String = "",
    val contact2Phone: String = "",
    val preferredLanguage: String = "en",
    val preferredVoice: String = "female",
    val isSynced: Boolean = false
)
