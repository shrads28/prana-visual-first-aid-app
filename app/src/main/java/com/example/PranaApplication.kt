package com.example

import android.app.Application
import com.example.data.database.PranaDatabase
import com.example.data.entity.OfflinePack
import com.example.data.repository.PranaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PranaApplication : Application() {

    // SupervisorJob and CoroutineScope for application tasks
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { PranaDatabase.getDatabase(this) }
    val repository by lazy { PranaRepository(database.pranaDao()) }

    override fun onCreate() {
        super.onCreate()
        
        // Seed default offline pack metadata
        applicationScope.launch {
            seedOfflinePacks()
        }
    }

    private suspend fun seedOfflinePacks() {
        val defaultPacks = listOf(
            OfflinePack(
                id = "burns",
                title = "Burns & Scalds",
                titleHindi = "जलना और झुलसना",
                videoUrl = "https://storage.googleapis.com/prana-videos/burns.mp4"
            ),
            OfflinePack(
                id = "cuts",
                title = "Cuts & Minor Bleeding",
                titleHindi = "कट और मामूली रक्तस्राव",
                videoUrl = "https://storage.googleapis.com/prana-videos/cuts.mp4"
            ),
            OfflinePack(
                id = "cpr",
                title = "Cardiopulmonary Resuscitation (CPR)",
                titleHindi = "सीपीआर (कृत्रिम सांस)",
                videoUrl = "https://storage.googleapis.com/prana-videos/cpr.mp4"
            ),
            OfflinePack(
                id = "choking",
                title = "Choking First Aid",
                titleHindi = "दम घुटने पर प्राथमिक उपचार",
                videoUrl = "https://storage.googleapis.com/prana-videos/choking.mp4"
            ),
            OfflinePack(
                id = "fracture",
                title = "Fractures & Bone Injuries",
                titleHindi = "हड्डी टूटना और चोट",
                videoUrl = "https://storage.googleapis.com/prana-videos/fracture.mp4"
            ),
            OfflinePack(
                id = "snakebite",
                title = "Snake Bites & Envenomation",
                titleHindi = "सांप का काटना",
                videoUrl = "https://storage.googleapis.com/prana-videos/snakebite.mp4"
            ),
            OfflinePack(
                id = "bleeding",
                title = "Severe Bleeding Control",
                titleHindi = "गंभीर रक्तस्राव नियंत्रण",
                videoUrl = "https://storage.googleapis.com/prana-videos/bleeding.mp4"
            )
        )

        val dao = database.pranaDao()
        for (pack in defaultPacks) {
            val existing = dao.getOfflinePackDirect(pack.id)
            if (existing == null) {
                dao.insertOfflinePack(pack)
            }
        }
    }
}
