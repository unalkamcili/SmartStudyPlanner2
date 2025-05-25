package com.example.curricumplannerfixed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_plans")
data class StudyPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val date: String,
    val durationMinutes: Int,
    val completed: Boolean = false
)
