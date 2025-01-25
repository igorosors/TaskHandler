package ru.taskhandler.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import ru.taskhandler.ui.model.Task

@Stable
data class ScreenState(
    val tasks: MutableState<ImmutableMap<String, Task>>,
    val debugMessages: MutableState<ImmutableList<String>>,
    val newTasksCount: Int = 0,
)
