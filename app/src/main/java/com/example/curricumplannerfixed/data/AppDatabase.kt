package com.example.curricumplannerfixed.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.curricumplannerfixed.data.model.Assignment
import com.example.curricumplannerfixed.data.model.Exam
import com.example.curricumplannerfixed.data.model.Lesson
import com.example.curricumplannerfixed.data.model.StudyPlan
import com.example.curricumplannerfixed.data.repository.AssignmentDao
import com.example.curricumplannerfixed.data.repository.ExamDao
import com.example.curricumplannerfixed.data.repository.LessonDao
import com.example.curricumplannerfixed.data.repository.StudyPlanDao

@Database(
    entities = [Lesson::class, Assignment::class, Exam::class, StudyPlan::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonDao(): LessonDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun examDao(): ExamDao
    abstract fun studyPlanDao(): StudyPlanDao
}
