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
import com.example.curricumplannerfixed.data.model.Assignment
import com.example.curricumplannerfixed.data.model.Lesson
import com.example.curricumplannerfixed.viewmodel.AssignmentViewModel
import com.example.curricumplannerfixed.viewmodel.LessonViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentScreen(
    assignmentViewModel: AssignmentViewModel,
    lessonViewModel: LessonViewModel
) {
    val assignmentList by assignmentViewModel.assignments.collectAsState()
    val lessonList by lessonViewModel.lessons.collectAsState()

    var title by remember { mutableStateOf("") }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
    var dueDate by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var editingId by remember { mutableStateOf<Int?>(null) }
    var expandedLesson by remember { mutableStateOf(false) }
    var expandedDate by remember { mutableStateOf(false) }

    val dateOptions = List(14) { i ->
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, i)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.format(calendar.time)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add / Edit Assignment", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Assignment Title") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(expanded = expandedLesson, onExpandedChange = { expandedLesson = !expandedLesson }) {
            OutlinedTextField(
                value = selectedLesson?.name ?: "Select Lesson",
                onValueChange = {},
                readOnly = true,
                label = { Text("Lesson") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedLesson, onDismissRequest = { expandedLesson = false }) {
                lessonList.forEach { lesson ->
                    DropdownMenuItem(text = { Text(lesson.name) }, onClick = {
                        selectedLesson = lesson
                        expandedLesson = false
                    })
                }
            }
        }

        ExposedDropdownMenuBox(expanded = expandedDate, onExpandedChange = { expandedDate = !expandedDate }) {
            OutlinedTextField(
                value = dueDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Due Date") },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedDate, onDismissRequest = { expandedDate = false }) {
                dateOptions.forEach { dateOption ->
                    DropdownMenuItem(text = { Text(dateOption) }, onClick = {
                        dueDate = dateOption
                        expandedDate = false
                    })
                }
            }
        }

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
            Text("Completed")
        }

        Button(onClick = {
            if (title.isNotBlank() && dueDate.isNotBlank() && selectedLesson != null) {
                val assignment = Assignment(
                    id = editingId ?: 0,
                    title = title,
                    lessonName = selectedLesson!!.name,
                    dueDate = dueDate,
                    comment = comment,
                    isCompleted = isCompleted,
                    lessonId = selectedLesson!!.id
                )
                if (editingId == null) assignmentViewModel.addAssignment(assignment)
                else assignmentViewModel.updateAssignment(assignment)

                title = ""
                selectedLesson = null
                dueDate = ""
                comment = ""
                isCompleted = false
                editingId = null
            }
        }) {
            Text(if (editingId == null) "Add Assignment" else "Update Assignment")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Assignment List", style = MaterialTheme.typography.titleMedium)

        LazyColumn {
            items(assignmentList) { assignment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${assignment.title} (${assignment.lessonName})")
                        Text("Due: ${assignment.dueDate} | Completed: ${if (assignment.isCompleted) "Yes" else "No"}")
                        if (!assignment.comment.isNullOrBlank()) Text("Comment: ${assignment.comment}")
                    }
                    Row {
                        IconButton(onClick = {
                            title = assignment.title
                            selectedLesson = lessonList.find { it.id == assignment.lessonId }
                            dueDate = assignment.dueDate
                            comment = assignment.comment ?: ""
                            isCompleted = assignment.isCompleted
                            editingId = assignment.id
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            assignmentViewModel.deleteAssignment(assignment)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
