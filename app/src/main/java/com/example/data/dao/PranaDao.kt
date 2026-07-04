package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.MedicalProfile
import com.example.data.entity.TreatmentHistory
import com.example.data.entity.OfflinePack
import kotlinx.coroutines.flow.Flow

@Dao
interface PranaDao {

    // --- Medical Profile ---
    @Query("SELECT * FROM medical_profiles WHERE id = 1 LIMIT 1")
    fun getMedicalProfile(): Flow<MedicalProfile?>

    @Query("SELECT * FROM medical_profiles WHERE id = 1 LIMIT 1")
    suspend fun getMedicalProfileDirect(): MedicalProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalProfile(profile: MedicalProfile)

    // --- Treatment History ---
    @Query("SELECT * FROM treatment_histories ORDER BY date DESC")
    fun getAllTreatmentHistories(): Flow<List<TreatmentHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreatmentHistory(history: TreatmentHistory)

    @Query("DELETE FROM treatment_histories")
    suspend fun clearHistory()

    // --- Offline Packs ---
    @Query("SELECT * FROM offline_packs")
    fun getAllOfflinePacks(): Flow<List<OfflinePack>>

    @Query("SELECT * FROM offline_packs WHERE id = :id LIMIT 1")
    fun getOfflinePackFlow(id: String): Flow<OfflinePack?>

    @Query("SELECT * FROM offline_packs WHERE id = :id LIMIT 1")
    suspend fun getOfflinePackDirect(id: String): OfflinePack?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflinePack(pack: OfflinePack)

    @Update
    suspend fun updateOfflinePack(pack: OfflinePack)

    @Query("UPDATE offline_packs SET downloadProgress = :progress, isDownloaded = :isDownloaded, localVideoPath = :localPath WHERE id = :id")
    suspend fun updateDownloadStatus(id: String, progress: Float, isDownloaded: Boolean, localPath: String?)
}
