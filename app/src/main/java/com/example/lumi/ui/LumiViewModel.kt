package com.example.lumi.ui

import androidx.lifecycle.ViewModel
import com.example.lumi.data.StatusType
import com.example.lumi.data.Task
import com.example.lumi.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class LumiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LumiUiState())
    val uiState: StateFlow<LumiUiState> = _uiState.asStateFlow()

    fun addTask(title: String) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            status = StatusType.TODO
        )
        _uiState.update { currentState ->
            currentState.copy(tasks = currentState.tasks + newTask)
        }
    }

    fun updateTask(taskId: String, newTitle: String, newStatus: StatusType) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) {
                    task.copy(title = newTitle, status = newStatus)
                } else {
                    task
                }
            }
            currentState.copy(tasks = updatedTasks)
        }
    }

    fun deleteTask(taskId: String) {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.filter { task -> task.id != taskId }
            currentState.copy(tasks = updatedTasks)
        }
    }

    fun deleteAllCompleted() {
        _uiState.update { currentState ->
            val updatedTasks = currentState.tasks.filter { task -> task.status != StatusType.COMPLETED }
            currentState.copy(tasks = updatedTasks)
        }
    }

    fun updateUser(name: String) {
        val newUser = User(name = name)
        _uiState.update { currentState ->
            currentState.copy(user = newUser)
        }
    }
}
