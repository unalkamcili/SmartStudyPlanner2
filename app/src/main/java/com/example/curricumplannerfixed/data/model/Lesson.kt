package com.example.curricumplannerfixed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons")
data class Lesson(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val date: String? = null
)
