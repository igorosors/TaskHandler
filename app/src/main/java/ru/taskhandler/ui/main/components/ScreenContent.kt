package ru.taskhandler.ui.main.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import ru.taskhandler.ui.model.Task
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenContent(
    tasks: ImmutableMap<String, Task>,
    debugMessages: ImmutableList<String>,
    onRemoveTask: (taskId: String) -> Unit,
    onCreateTask: () -> Unit,
    newTasksCount: Int,
    setNewTasksCount: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val messagesScrollState = rememberScrollState()

    LaunchedEffect(debugMessages) {
        messagesScrollState.animateScrollTo(messagesScrollState.maxValue)
    }

    Column(modifier) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                count = tasks.size,
                key = { index -> tasks.entries.elementAtOrNull(index)?.key ?: UUID.randomUUID().toString() },
            ) { index ->
                tasks.entries.elementAtOrNull(index)?.let { task ->
                    TaskCard(
                        modifier = Modifier.animateItemPlacement(),
                        taskId = task.key,
                        task = task.value,
                        onRemove = { onRemoveTask(task.key) },
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .drawBehind {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .verticalScroll(messagesScrollState)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            debugMessages.forEach { message ->
                Text(
                    text = message,
                    color = Color.Black,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { setNewTasksCount(false) }
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "—",
                    color = Color.Black,
                )
            }

            Text(
                modifier = Modifier.width(16.dp),
                text = newTasksCount.toString(),
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { setNewTasksCount(true) }
                    .size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = Color.Black,
                )
            }

            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable { onCreateTask() },
                text = "Создать",
                color = Color.Black,
            )
        }
    }
}

@Composable
@Preview
private fun ScreenContentPreview() {
    ScreenContent(
        tasks = persistentMapOf(UUID.randomUUID().toString() to Task(isLaunched = true, isFinished = false)),
        debugMessages = persistentListOf("task 1 started", "task 1 cancelled"),
        onRemoveTask = {},
        onCreateTask = {},
        setNewTasksCount = {},
        newTasksCount = 2,
    )
}
