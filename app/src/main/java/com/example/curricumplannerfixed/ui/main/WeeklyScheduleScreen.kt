package com.example.curricumplannerfixed.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.curricumplannerfixed.viewmodel.AssignmentViewModel
import com.example.curricumplannerfixed.viewmodel.ExamViewModel
import com.example.curricumplannerfixed.viewmodel.LessonViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val scheduleDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

@Composable
fun WeeklyScheduleScreen(
    lessonViewModel: LessonViewModel,
    assignmentViewModel: AssignmentViewModel,
    examViewModel: ExamViewModel,
    onBack: () -> Unit
) {
    val lessons by lessonViewModel.lessons.collectAsState()
    val assignments by assignmentViewModel.assignments.collectAsState()
    val exams by examViewModel.exams.collectAsState()

    var showLessons by remember { mutableStateOf(true) }
    var showAssignments by remember { mutableStateOf(true) }
    var showExams by remember { mutableStateOf(true) }

    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(6)) }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Weekly Schedule", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = showLessons, onCheckedChange = { showLessons = it })
            Text("Lessons", modifier = Modifier.padding(end = 8.dp))
            Checkbox(checked = showAssignments, onCheckedChange = { showAssignments = it })
            Text("Assignments", modifier = Modifier.padding(end = 8.dp))
            Checkbox(checked = showExams, onCheckedChange = { showExams = it })
            Text("Exams")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                startDate = startDate.minusDays(7)
                endDate = endDate.minusDays(7)
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Week")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = startDate.toString(),
                onValueChange = { runCatching { LocalDate.parse(it) }.onSuccess { startDate = it } },
                label = { Text("Start Date") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = endDate.toString(),
                onValueChange = { runCatching { LocalDate.parse(it) }.onSuccess { endDate = it } },
                label = { Text("End Date") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                startDate = startDate.plusDays(7)
                endDate = endDate.plusDays(7)
            }) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Next Week")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onBack) {
            Text("Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(50.dp))
            scheduleDays.forEach { day ->
                Text(
                    text = day.take(3),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        for (hour in 8..18) {
            Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text(
                    text = "$hour:00",
                    modifier = Modifier.width(50.dp).padding(4.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                scheduleDays.forEach { day ->
                    val lessonMatches = lessons.filter {
                        it.dayOfWeek.equals(day, ignoreCase = true) &&
                                try {
                                    val start = LocalTime.parse(it.startTime, timeFormatter)
                                    val end = LocalTime.parse(it.endTime, timeFormatter)
                                    hour in start.hour..<end.hour
                                } catch (e: Exception) {
                                    false
                                }
                    }

                    val assignmentMatches = assignments.filter {
                        try {
                            val assignmentDate = LocalDate.parse(it.dueDate)
                            val inRange = !assignmentDate.isBefore(startDate) && !assignmentDate.isAfter(endDate)
                            assignmentDate.dayOfWeek.name.equals(day.uppercase()) && inRange
                        } catch (e: Exception) {
                            false
                        }
                    }

                    val examMatches = exams.filter {
                        try {
                            val examDate = LocalDate.parse(it.examDate)
                            val examDayMatches = examDate.dayOfWeek.name.equals(day.uppercase())
                            val examStartHour = LocalTime.parse(it.startTime, timeFormatter).hour
                            val inRange = !examDate.isBefore(startDate) && !examDate.isAfter(endDate)
                            examDayMatches && examStartHour == hour && inRange
                        } catch (e: Exception) {
                            false
                        }
                    }

                    val bgColor = when {
                        showExams && examMatches.isNotEmpty() -> Color(0xFFFFCDD2)
                        showLessons && lessonMatches.isNotEmpty() -> Color(0xFFBBDEFB)
                        showAssignments && assignmentMatches.isNotEmpty() -> Color(0xFFC8E6C9)
                        else -> Color(0xFFECEFF1)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .background(bgColor)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (showLessons) lessonMatches.forEach { Text(it.name, style = MaterialTheme.typography.bodySmall) }
                            if (showAssignments) assignmentMatches.forEach { Text(it.title, style = MaterialTheme.typography.bodySmall) }
                            if (showExams) examMatches.forEach { Text("\uD83D\uDCDD ${it.title}", style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }
        }
    }
}
