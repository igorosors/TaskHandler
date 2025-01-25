package ru.taskhandler.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.taskhandler.ui.main.components.ScreenContent
import ru.taskhandler.ui.theme.TaskHandlerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<ViewModel>()

        enableEdgeToEdge()
        setContent {
            TaskHandlerTheme {
                val state by viewModel.state.collectAsState()

                ScreenContent(
                    modifier = Modifier.systemBarsPadding(),
                    tasks = state.tasks.value,
                    debugMessages = state.debugMessages.value,
                    onRemoveTask = remember { { taskId -> viewModel.removeTask(taskId) } },
                    onCreateTask = remember { { viewModel.createTasks() } },
                    setNewTasksCount = remember { { result -> viewModel.setNewTasksCount(result) } },
                    newTasksCount = state.newTasksCount,
                )
            }
        }
    }
}
