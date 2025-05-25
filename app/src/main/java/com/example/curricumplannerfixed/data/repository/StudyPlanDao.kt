package com.example.curricumplannerfixed.data.repository

import androidx.room.*
import com.example.curricumplannerfixed.data.model.StudyPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyPlanDao {

    @Query("SELECT * FROM study_plans ORDER BY date")
    fun getAllPlans(): Flow<List<StudyPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: StudyPlan)

    @Update
    suspend fun updatePlan(plan: StudyPlan)

    @Delete
    suspend fun deletePlan(plan: StudyPlan)
}
