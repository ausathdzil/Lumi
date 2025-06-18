package com.example.lumi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.lumi.R
import com.example.lumi.data.StatusType
import com.example.lumi.data.Task
import kotlinx.coroutines.launch

@Composable
fun LumiHomeScreen(
    name: String,
    taskList: List<Task>,
    onAddTask: (String) -> Unit,
    onUpdateTask: (String, String, StatusType) -> Unit,
    onDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.hello))
            Text(
                text = name,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        AddTaskField(onAddTask = onAddTask)
        if (taskList.isEmpty()) {
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
                Text(text = stringResource(R.string.add_first_task))
            }
        } else {
            TaskList(
                taskList = taskList,
                onUpdateTask = onUpdateTask,
                onDeleteTask = onDeleteTask,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AddTaskField(
    onAddTask: (String) -> Unit,
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
            label = { Text(text = stringResource(R.string.task_title)) },
            placeholder = { Text(text = stringResource(R.string.add_new_task)) },
            singleLine = true
        )
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    onAddTask(title)
                    title = ""
                }
            },
            modifier = Modifier.fillMaxHeight(),
            enabled = title.isNotBlank()
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_task)
            )
        }
    }
}

@Composable
fun TaskList(
    taskList: List<Task>,
    onUpdateTask: (String, String, StatusType) -> Unit,
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
                            onUpdateTask(
                                task.id,
                                task.title,
                                if (isChecked) StatusType.COMPLETED else StatusType.TODO
                            )
                        },
                    )
                    Text(
                        text = (task.title),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (task.status === StatusType.COMPLETED) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                    EditTaskBottomSheet(
                        task = task,
                        onUpdateTask = onUpdateTask
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskBottomSheet(
    task: Task,
    onUpdateTask: (String, String, StatusType) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf(task.title) }

    IconButton(onClick = { showBottomSheet = true }) {
        Icon(
            Icons.Outlined.Edit,
            contentDescription = stringResource(R.string.edit_task),
            tint = MaterialTheme.colorScheme.outline
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            modifier = modifier,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.edit_task),
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.task_title)) },
                    singleLine = true
                )
                Column {
                    Button(
                        onClick = {
                            onUpdateTask(task.id, title, task.status)
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.update_task))
                    }
                    Button(
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}
