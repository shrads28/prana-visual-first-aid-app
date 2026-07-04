package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_packs")
data class OfflinePack(
    @PrimaryKey val id: String, // e.g. "burns", "cuts", "cpr", "fracture", "choking", "snakebite", "bleeding"
    val title: String,
    val titleHindi: String,
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val videoUrl: String = "",
    val localVideoPath: String? = null
)
