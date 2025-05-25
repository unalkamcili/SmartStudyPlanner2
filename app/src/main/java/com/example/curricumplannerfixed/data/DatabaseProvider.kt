package com.example.curricumplannerfixed.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "curriculum_db"
            )
                .fallbackToDestructiveMigration() // Eski veriler silinerek yeni yapı oluşturulur
                .build().also {
                    INSTANCE = it
                }
        }
    }
}

