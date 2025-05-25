package com.example.curricumplannerfixed.data.repository

import androidx.room.*
import com.example.curricumplannerfixed.data.model.Exam
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Update
    suspend fun updateExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)

    @Query("SELECT * FROM exams")
    fun getAllExams(): Flow<List<Exam>>
}
