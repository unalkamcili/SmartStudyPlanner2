package com.example.curricumplannerfixed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.curricumplannerfixed.data.model.Lesson
import com.example.curricumplannerfixed.data.repository.LessonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LessonViewModel(private val repository: LessonRepository) : ViewModel() {

    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons

    init {
        viewModelScope.launch {
            repository.getAllLessons().collectLatest {
                _lessons.value = it
            }
        }
    }

    fun addLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.insertLesson(lesson)
        }
    }

    fun deleteLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.deleteLesson(lesson)
        }
    }
    fun updateLesson(lesson: Lesson) {
        viewModelScope.launch {
            repository.updateLesson(lesson)
        }
    }

}