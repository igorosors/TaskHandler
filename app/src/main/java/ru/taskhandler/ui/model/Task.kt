package ru.taskhandler.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class Task(
    val isLaunched: Boolean = false,
    val isFinished: Boolean = false,
)
