package com.example.curricumplannerfixed.data.repository

import androidx.room.*
import com.example.curricumplannerfixed.data.model.Lesson
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons")
    fun getAllLessons(): Flow<List<Lesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: Lesson)

    @Delete
    suspend fun deleteLesson(lesson: Lesson)

    @Update
    suspend fun updateLesson(lesson: Lesson)

}
