package com.example.curricumplannerfixed.data.repository

import androidx.room.*
import com.example.curricumplannerfixed.data.model.Assignment
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    @Query("SELECT * FROM assignments WHERE lessonId = :lessonId")
    fun getAssignmentsForLesson(lessonId: Int): Flow<List<Assignment>>

    @Query("SELECT * FROM assignments")
    fun getAllAssignments(): Flow<List<Assignment>>
}