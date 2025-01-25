package ru.taskhandler.ui.utils

import androidx.compose.runtime.mutableStateOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.taskhandler.ui.model.Task
import java.util.UUID

class TaskHandler(private val scope: CoroutineScope) {

    private val jobs = mutableMapOf<String, Job>()
    private val taskFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val tasks = mutableStateOf<ImmutableMap<String, Task>>(persistentMapOf())
    val debugMessages = mutableStateOf<ImmutableList<String>>(persistentListOf())

    private fun message(text: String) {
        debugMessages.value = debugMessages.value.toMutableList().apply {
            add(text)
        }.toImmutableList()
    }

    init {
        scope.launch {
            taskFlow.collect { ids ->
                ids.forEach { taskId ->
                    if (tasks.value.contains(taskId)) {
                        val job = scope.launch(Dispatchers.Default) {
                            runCatching {
                                message("task $taskId started")
                                tasks.value = tasks.value.toMutableMap().apply {
                                    this[taskId]?.let { newTask ->
                                        this[taskId] = newTask.copy(isLaunched = true)
                                    }
                                }.toImmutableMap()

                                // execute task
                                delay(5000)

                                tasks.value = tasks.value.toMutableMap().apply {
                                    this[taskId]?.let { newTask ->
                                        this[taskId] = newTask.copy(isFinished = true)
                                    }
                                }.toImmutableMap()
                                message("task $taskId finished")
                            }.onFailure {
                                message("task $taskId cancelled")
                            }
                        }

                        jobs[taskId] = job
                        job.join()
                    }
                }
            }
        }
    }

    fun createTasks(taskCount: Int) {
        scope.launch {
            val newTasks = List(taskCount) { UUID.randomUUID().toString() }
            tasks.value = tasks.value.toMutableMap().apply {
                newTasks.forEach { taskId ->
                    this[taskId] = Task()
                }
            }.toImmutableMap()

            taskFlow.emit(newTasks)
        }
    }

    fun removeTask(taskId: String) {
        jobs[taskId]?.cancel()
        tasks.value = tasks.value.toMutableMap().apply {
            remove(taskId)
        }.toImmutableMap()
    }
}
