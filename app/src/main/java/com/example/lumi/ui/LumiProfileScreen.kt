package com.example.lumi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.lumi.R
import com.example.lumi.data.model.StatusType
import com.example.lumi.data.model.Task
import com.example.lumi.data.model.User

@Composable
fun LumiProfileScreen(
    user: User,
    taskList: List<Task>,
    onUpdateUser: (String) -> Unit,
    onDeleteTask: (String) -> Unit,
    onDeleteAllCompletedTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedTask = taskList.filter { it.status == StatusType.COMPLETED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        UpdateUserField(
            user = user,
            onUpdateUser = onUpdateUser,
        )
        HorizontalDivider()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.completed_tasks),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(
                    R.string.no_tasks_completed,
                    taskList.count { it.status == StatusType.COMPLETED }
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onDeleteAllCompletedTask,
                enabled = completedTask.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(stringResource(R.string.delete_all_completed))
            }
        }
        if (completedTask.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
                Text(text = stringResource(R.string.no_tasks_yet))
            }
        } else {
            CompletedTaskList(
                taskList = completedTask,
                onDeleteTask = onDeleteTask,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun UpdateUserField(
    user: User,
    onUpdateUser: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.name)) },
            placeholder = { Text(text = user.name) },
            singleLine = true
        )
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    keyboardController?.hide()
                    onUpdateUser(name)
                    name = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank()
        ) {
            Text(stringResource(R.string.update))
        }
    }
}

@Composable
fun CompletedTaskList(
    taskList: List<Task>,
    onDeleteTask: (String) -> Unit,
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
            ElevatedCard(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.status == StatusType.COMPLETED,
                        onCheckedChange = { },
                        enabled = false
                    )
                    Text(
                        text = (task.title),
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.surfaceDim,
                        textDecoration = TextDecoration.LineThrough
                    )
                    IconButton(onClick = { onDeleteTask(task.id) }) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_task),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}
