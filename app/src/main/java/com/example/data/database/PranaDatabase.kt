package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.PranaDao
import com.example.data.entity.MedicalProfile
import com.example.data.entity.TreatmentHistory
import com.example.data.entity.OfflinePack

@Database(
    entities = [MedicalProfile::class, TreatmentHistory::class, OfflinePack::class],
    version = 1,
    exportSchema = false
)
abstract class PranaDatabase : RoomDatabase() {
    abstract fun pranaDao(): PranaDao

    companion object {
        @Volatile
        private var INSTANCE: PranaDatabase? = null

        fun getDatabase(context: Context): PranaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PranaDatabase::class.java,
                    "prana_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
