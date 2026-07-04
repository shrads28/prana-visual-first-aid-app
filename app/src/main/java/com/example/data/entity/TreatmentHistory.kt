package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treatment_histories")
data class TreatmentHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val injury: String,
    val durationSeconds: Int,
    val stepsCompleted: Int,
    val totalSteps: Int,
    val voiceLanguage: String,
    val isSynced: Boolean = false
)
