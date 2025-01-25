package ru.taskhandler.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.taskhandler.ui.utils.TaskHandler

class ViewModel : ViewModel() {

    private val taskHandler = TaskHandler(viewModelScope)
    private val mutableUIState: MutableStateFlow<ScreenState> = MutableStateFlow(
        ScreenState(
            tasks = taskHandler.tasks,
            debugMessages = taskHandler.debugMessages,
        )
    )
    val state = mutableUIState.asStateFlow()

    fun createTasks() {
        taskHandler.createTasks(state.value.newTasksCount)
    }

    fun setNewTasksCount(result: Boolean) {
        val newTasksCount = (if (result) state.value.newTasksCount + 1 else state.value.newTasksCount - 1)
            .coerceIn(1..9)
        mutableUIState.update { state ->
            state.copy(newTasksCount = newTasksCount)
        }
    }

    fun removeTask(taskId: String) {
        taskHandler.removeTask(taskId)
    }
}
