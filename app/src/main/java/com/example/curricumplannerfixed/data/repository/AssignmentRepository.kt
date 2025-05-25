package com.example.curricumplannerfixed.data.repository

import com.example.curricumplannerfixed.data.model.Assignment
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val assignmentDao: AssignmentDao) {

    fun getAssignmentsForLesson(lessonId: Int): Flow<List<Assignment>> =
        assignmentDao.getAssignmentsForLesson(lessonId)

    fun getAllAssignments(): Flow<List<Assignment>> =
        assignmentDao.getAllAssignments()

    suspend fun addAssignment(assignment: Assignment) =
        assignmentDao.insertAssignment(assignment)

    suspend fun updateAssignment(assignment: Assignment) =
        assignmentDao.updateAssignment(assignment)

    suspend fun deleteAssignment(assignment: Assignment) =
        assignmentDao.deleteAssignment(assignment)
}
