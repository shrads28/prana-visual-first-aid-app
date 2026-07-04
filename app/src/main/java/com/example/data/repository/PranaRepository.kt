package com.example.data.repository

import com.example.data.dao.PranaDao
import com.example.data.entity.MedicalProfile
import com.example.data.entity.TreatmentHistory
import com.example.data.entity.OfflinePack
import kotlinx.coroutines.flow.Flow

class PranaRepository(private val pranaDao: PranaDao) {

    val medicalProfile: Flow<MedicalProfile?> = pranaDao.getMedicalProfile()
    val allTreatmentHistories: Flow<List<TreatmentHistory>> = pranaDao.getAllTreatmentHistories()
    val allOfflinePacks: Flow<List<OfflinePack>> = pranaDao.getAllOfflinePacks()

    suspend fun getMedicalProfileDirect(): MedicalProfile? {
        return pranaDao.getMedicalProfileDirect()
    }

    suspend fun saveMedicalProfile(profile: MedicalProfile) {
        pranaDao.insertMedicalProfile(profile)
    }

    suspend fun saveTreatmentHistory(history: TreatmentHistory) {
        pranaDao.insertTreatmentHistory(history)
    }

    suspend fun clearHistory() {
        pranaDao.clearHistory()
    }

    suspend fun getOfflinePackDirect(id: String): OfflinePack? {
        return pranaDao.getOfflinePackDirect(id)
    }

    fun getOfflinePackFlow(id: String): Flow<OfflinePack?> {
        return pranaDao.getOfflinePackFlow(id)
    }

    suspend fun saveOfflinePack(pack: OfflinePack) {
        pranaDao.insertOfflinePack(pack)
    }

    suspend fun updateDownloadStatus(id: String, progress: Float, isDownloaded: Boolean, localPath: String?) {
        pranaDao.updateDownloadStatus(id, progress, isDownloaded, localPath)
    }
}
