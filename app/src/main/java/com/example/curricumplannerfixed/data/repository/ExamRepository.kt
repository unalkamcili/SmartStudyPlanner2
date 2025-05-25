package com.example.curricumplannerfixed.data.repository

import com.example.curricumplannerfixed.data.model.Exam

class ExamRepository(private val examDao: ExamDao) {
    fun getAllExams() = examDao.getAllExams()

    suspend fun addExam(exam: Exam) = examDao.insertExam(exam)
    suspend fun updateExam(exam: Exam) = examDao.updateExam(exam)
    suspend fun deleteExam(exam: Exam) = examDao.deleteExam(exam)
}
