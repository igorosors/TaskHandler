package ru.taskhandler.ui.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.taskhandler.ui.model.Task
import java.util.UUID

@Composable
fun TaskCard(
    taskId: String,
    task: Task,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val status = when {
            task.isFinished -> "Завершено"
            task.isLaunched -> "Выполнение"
            else -> "Ожидание"
        }

        Text(
            modifier = Modifier.weight(1f),
            text = "$taskId\n$status",
            color = Color.Black,
        )

        Text(
            modifier = Modifier
                .clickable { onRemove() }
                .padding(16.dp),
            text = "Удалить",
            color = Color.Black,
        )
    }
}

@Preview
@Composable
private fun TaskCardPreview() {
    TaskCard(
        taskId = UUID.randomUUID().toString(),
        task = Task(isLaunched = true, isFinished = false),
        onRemove = {},
    )
}
