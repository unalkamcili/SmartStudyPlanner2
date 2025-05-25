package com.example.curricumplannerfixed.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.curricumplannerfixed.data.model.Exam
import com.example.curricumplannerfixed.data.model.Lesson
import com.example.curricumplannerfixed.viewmodel.ExamViewModel
import com.example.curricumplannerfixed.viewmodel.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    viewModel: ExamViewModel,
    lessonViewModel: LessonViewModel
) {
    val examList by viewModel.exams.collectAsState()
    val lessonList by lessonViewModel.lessons.collectAsState()

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<Int?>(null) }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("09:00") }

    val timeOptions = (8..20).map { hour -> "%02d:00".format(hour) }
    var expandedStart by remember { mutableStateOf(false) }
    var expandedEnd by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add / Edit Exam", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Exam Title") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedLesson?.name ?: "Select Lesson",
                onValueChange = {},
                readOnly = true,
                label = { Text("Lesson") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                lessonList.forEach { lesson ->
                    DropdownMenuItem(
                        text = { Text(lesson.name) },
                        onClick = {
                            selectedLesson = lesson
                            expanded = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(expanded = expandedStart, onExpandedChange = { expandedStart = !expandedStart }) {
            OutlinedTextField(
                value = startTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("Start Time") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedStart, onDismissRequest = { expandedStart = false }) {
                timeOptions.forEach { time ->
                    DropdownMenuItem(text = { Text(time) }, onClick = {
                        startTime = time
                        expandedStart = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = expandedEnd, onExpandedChange = { expandedEnd = !expandedEnd }) {
            OutlinedTextField(
                value = endTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("End Time") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedEnd, onDismissRequest = { expandedEnd = false }) {
                timeOptions.forEach { time ->
                    DropdownMenuItem(text = { Text(time) }, onClick = {
                        endTime = time
                        expandedEnd = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Exam Date (e.g. 2024-06-20)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                if (title.isNotBlank() && date.isNotBlank() && selectedLesson != null) {
                    val exam = Exam(
                        id = editingId ?: 0,
                        title = title,
                        lessonName = selectedLesson!!.name,
                        examDate = date,
                        startTime = startTime,
                        endTime = endTime,
                        description = description,
                        date = ""
                    )
                    if (editingId == null) viewModel.addExam(exam)
                    else viewModel.updateExam(exam)

                    title = ""
                    date = ""
                    description = ""
                    selectedLesson = null
                    startTime = "08:00"
                    endTime = "09:00"
                    editingId = null
                }
            }) {
                Text(if (editingId == null) "Add Exam" else "Update Exam")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Exam List", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(examList) { exam ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${exam.title} (${exam.lessonName}) - ${exam.examDate} ${exam.startTime}-${exam.endTime}")
                        Text(exam.description)
                    }
                    Row {
                        IconButton(onClick = {
                            title = exam.title
                            date = exam.examDate
                            description = exam.description
                            startTime = exam.startTime
                            endTime = exam.endTime
                            selectedLesson = lessonList.find { it.name == exam.lessonName }
                            editingId = exam.id
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            viewModel.deleteExam(exam)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
