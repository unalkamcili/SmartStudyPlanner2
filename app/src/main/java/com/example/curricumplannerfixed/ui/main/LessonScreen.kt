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
import androidx.compose.ui.unit.dp
import com.example.curricumplannerfixed.data.model.Lesson
import com.example.curricumplannerfixed.viewmodel.LessonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    viewModel: LessonViewModel,
    onNavigateToSchedule: () -> Unit,
    onNavigateToAssignments: () -> Unit
) {
    val lessonList by viewModel.lessons.collectAsState()

    var lessonName by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("Monday") }
    var selectedStartTime by remember { mutableStateOf("08:00") }
    var selectedEndTime by remember { mutableStateOf("09:00") }
    var editingLessonId by remember { mutableStateOf<Int?>(null) }

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val timeOptions = (8..20).map { hour -> "%02d:00".format(hour) }

    var expandedDay by remember { mutableStateOf(false) }
    var expandedStart by remember { mutableStateOf(false) }
    var expandedEnd by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add / Edit Lesson", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = lessonName,
            onValueChange = { lessonName = it },
            label = { Text("Lesson Name") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(expanded = expandedDay, onExpandedChange = { expandedDay = !expandedDay }) {
            OutlinedTextField(
                value = selectedDay,
                onValueChange = {},
                readOnly = true,
                label = { Text("Day of Week") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedDay, onDismissRequest = { expandedDay = false }) {
                daysOfWeek.forEach { day ->
                    DropdownMenuItem(text = { Text(day) }, onClick = {
                        selectedDay = day
                        expandedDay = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = expandedStart, onExpandedChange = { expandedStart = !expandedStart }) {
            OutlinedTextField(
                value = selectedStartTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("Start Time") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedStart, onDismissRequest = { expandedStart = false }) {
                timeOptions.forEach { time ->
                    DropdownMenuItem(text = { Text(time) }, onClick = {
                        selectedStartTime = time
                        expandedStart = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = expandedEnd, onExpandedChange = { expandedEnd = !expandedEnd }) {
            OutlinedTextField(
                value = selectedEndTime,
                onValueChange = {},
                readOnly = true,
                label = { Text("End Time") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedEnd, onDismissRequest = { expandedEnd = false }) {
                timeOptions.forEach { time ->
                    DropdownMenuItem(text = { Text(time) }, onClick = {
                        selectedEndTime = time
                        expandedEnd = false
                    })
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                if (lessonName.isNotBlank() && selectedStartTime.isNotBlank() && selectedEndTime.isNotBlank()) {
                    val lesson = Lesson(
                        id = editingLessonId ?: 0,
                        name = lessonName,
                        dayOfWeek = selectedDay,
                        startTime = selectedStartTime,
                        endTime = selectedEndTime
                    )
                    if (editingLessonId == null) viewModel.addLesson(lesson) else viewModel.updateLesson(lesson)
                    lessonName = ""
                    selectedDay = "Monday"
                    selectedStartTime = "08:00"
                    selectedEndTime = "09:00"
                    editingLessonId = null
                }
            }) {
                Text("Save Lesson")
            }

            Button(onClick = onNavigateToSchedule) {
                Text("View Weekly Schedule")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Lesson List", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(lessonList) { lesson ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${lesson.name} (${lesson.dayOfWeek} ${lesson.startTime}-${lesson.endTime})")
                    Row {
                        IconButton(onClick = {
                            lessonName = lesson.name
                            selectedDay = lesson.dayOfWeek
                            selectedStartTime = lesson.startTime
                            selectedEndTime = lesson.endTime
                            editingLessonId = lesson.id
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            viewModel.deleteLesson(lesson)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
