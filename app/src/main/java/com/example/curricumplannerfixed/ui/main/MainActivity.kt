package com.example.curricumplannerfixed.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.curricumplannerfixed.data.DatabaseProvider
import com.example.curricumplannerfixed.data.repository.AssignmentRepository
import com.example.curricumplannerfixed.data.repository.ExamRepository
import com.example.curricumplannerfixed.data.repository.LessonRepository
import com.example.curricumplannerfixed.ui.theme.CurricumPlannerFixedTheme
import com.example.curricumplannerfixed.viewmodel.*
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    private val lessonViewModel: LessonViewModel by viewModels {
        val dao = DatabaseProvider.getDatabase(applicationContext).lessonDao()
        LessonViewModelFactory(LessonRepository(dao))
    }

    private val assignmentViewModel: AssignmentViewModel by viewModels {
        val dao = DatabaseProvider.getDatabase(applicationContext).assignmentDao()
        AssignmentViewModelFactory(AssignmentRepository(dao))
    }

    private val examViewModel: ExamViewModel by viewModels {
        val dao = DatabaseProvider.getDatabase(applicationContext).examDao()
        ExamViewModelFactory(ExamRepository(dao))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeViewModel = remember { ThemeViewModel() }
            val isDarkTheme by themeViewModel.isDarkMode.collectAsState()

            CurricumPlannerFixedTheme(darkTheme = isDarkTheme) {
                MainWithDrawer(
                    lessonViewModel = lessonViewModel,
                    assignmentViewModel = assignmentViewModel,
                    examViewModel = examViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainWithDrawer(
    lessonViewModel: LessonViewModel,
    assignmentViewModel: AssignmentViewModel,
    examViewModel: ExamViewModel,
    themeViewModel: ThemeViewModel
) {


    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf("Home", "Lessons", "Schedule", "Assignments", "Exams")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.School,
        Icons.Filled.CalendarMonth,
        Icons.Filled.List,
        Icons.Filled.Quiz
    )

    var currentScreenTitle by remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📘 Curriculum Planner",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
                items.forEachIndexed { index, screen ->
                    NavigationDrawerItem(
                        label = { Text(screen) },
                        selected = currentScreenTitle == screen,
                        onClick = {
                            currentScreenTitle = screen
                            scope.launch { drawerState.close() }
                            when (screen) {
                                "Home" -> navController.navigate("home")
                                "Lessons" -> navController.navigate("lessons")
                                "Schedule" -> navController.navigate("schedule")
                                "Assignments" -> navController.navigate("assignments")
                                "Exams" -> navController.navigate("exams")
                            }
                        },
                        icon = { Icon(icons[index], contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Dark Mode")
                    Spacer(Modifier.weight(1f))
                    val isDark = themeViewModel.isDarkMode.collectAsState()
                    Switch(
                        checked = isDark.value,
                        onCheckedChange = { themeViewModel.toggleTheme() }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = currentScreenTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            content = { innerPadding ->
                AppNavigator(
                    navController = navController,
                    lessonViewModel = lessonViewModel,
                    assignmentViewModel = assignmentViewModel,
                    examViewModel = examViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}

@Composable
fun AppNavigator(
    navController: NavHostController,
    lessonViewModel: LessonViewModel,
    assignmentViewModel: AssignmentViewModel,
    examViewModel: ExamViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("lessons") {
            LessonScreen(
                viewModel = lessonViewModel,
                onNavigateToSchedule = { navController.navigate("schedule") },
                onNavigateToAssignments = { navController.navigate("assignments") }
            )
        }
        composable("schedule") {
            WeeklyScheduleScreen(
                lessonViewModel = lessonViewModel,
                assignmentViewModel = assignmentViewModel,
                examViewModel = examViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("assignments") {
            AssignmentScreen(
                assignmentViewModel = assignmentViewModel,
                lessonViewModel = lessonViewModel
            )
        }
        composable("exams") {
            ExamScreen(
                viewModel = examViewModel,
                lessonViewModel = lessonViewModel
            )
        }
    }
}
