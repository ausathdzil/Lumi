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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumi.data.StatusType
import com.example.lumi.data.Task

@Composable
fun LumiHomeScreen(
    modifier: Modifier = Modifier,
    lumiViewModel: LumiViewModel = viewModel()
) {
    val lumiUiState by lumiViewModel.uiState.collectAsState()
    var title by remember { mutableStateOf("") }

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
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                label = { Text(text = "New task") },
                leadingIcon = {
                    Icon(
                        Icons.AutoMirrored.Filled.List,
                        contentDescription = null
                    )
                },
                singleLine = true
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.fillMaxHeight(),
                enabled = title.isNotBlank(),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add task"
                )
            }
        }
        TaskList(taskList = lumiUiState.tasks)
    }
}

@Composable
fun TaskList(
    taskList: List<Task>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(taskList) { task ->
            TaskItem(task = task)
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
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
                onCheckedChange = { /*TODO*/ },
            )
            Text(
                text = (task.title),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Outlined.Edit,
                contentDescription = "Edit task",
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete task",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
