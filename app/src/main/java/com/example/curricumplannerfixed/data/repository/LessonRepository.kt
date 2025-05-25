package com.example.curricumplannerfixed.data.repository

import com.example.curricumplannerfixed.data.model.Lesson
import kotlinx.coroutines.flow.Flow

class LessonRepository(private val lessonDao: LessonDao) {

    fun getAllLessons(): Flow<List<Lesson>> = lessonDao.getAllLessons()

    suspend fun insertLesson(lesson: Lesson) {
        lessonDao.insertLesson(lesson)
    }

    suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }
    suspend fun updateLesson(lesson: Lesson) {
        lessonDao.updateLesson(lesson)
    }

}
