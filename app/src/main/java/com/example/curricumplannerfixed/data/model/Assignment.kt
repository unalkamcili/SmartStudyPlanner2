package com.example.curricumplannerfixed.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lessonId: Int,
    val title: String,
    val lessonName: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val comment: String? = null

)
