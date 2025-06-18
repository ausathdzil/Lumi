package com.example.lumi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumi.data.StatusType
import com.example.lumi.data.Task
import com.example.lumi.ui.theme.LumiTheme

@Composable
fun LumiHomeScreen(
    modifier: Modifier = Modifier,
    lumiViewModel: LumiViewModel = viewModel()
) {
    val lumiUiState by lumiViewModel.uiState.collectAsState()

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Hello,")
            Text(
                text = "Ausath!",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        AddTaskField(onTaskAdd = { title -> lumiViewModel.addTask(title) })
        if (lumiUiState.tasks.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Add your first task!")
            }
        } else {
            TaskList(
                onStatusChange = { taskId, newTitle, newStatus ->
                    lumiViewModel.updateTask(taskId, newTitle, newStatus)
                },
                onDeleteTask = { taskId -> lumiViewModel.deleteTask(taskId) },
                taskList = lumiUiState.tasks,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AddTaskField(
    onTaskAdd: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf("") }

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            label = { Text(text = "Add new task...") },
            singleLine = true
        )
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    onTaskAdd(title)
                    title = ""
                }
            },
            modifier = Modifier.fillMaxHeight(),
            enabled = title.isNotBlank(),
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Add task"
            )
        }
    }
}

@Composable
fun TaskList(
    taskList: List<Task>,
    onStatusChange: (Int, String, StatusType) -> Unit,
    onDeleteTask: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = taskList,
            key = { task -> task.id }
        ) { task ->
            OutlinedCard(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.status == StatusType.COMPLETED,
                        onCheckedChange = { isChecked ->
                            onStatusChange(
                                task.id,
                                task.title,
                                if (isChecked) StatusType.COMPLETED else StatusType.TODO
                            )
                        },
                    )
                    Text(
                        text = (task.title),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = if (task.status === StatusType.COMPLETED) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    )
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit task",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                    IconButton(onClick = { onDeleteTask(task.id) }) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Delete task",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LumiHomeScreenPreview() {
    LumiTheme {
        LumiHomeScreen()
    }
}
