package com.example.curricumplannerfixed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val lessonName: String,
    val examDate: String,
    val startTime: String,
    val endTime: String,
    val description: String = "",

    val date: String,
)
