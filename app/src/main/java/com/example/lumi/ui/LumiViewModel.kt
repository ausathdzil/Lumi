package com.example.lumi.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lumi.data.local.AppDatabase
import com.example.lumi.data.model.StatusType
import com.example.lumi.data.model.Task
import com.example.lumi.data.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class LumiViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao = AppDatabase.getDatabase(application).taskDao()

    private val userDao = AppDatabase.getDatabase(application).userDao()

    val uiState: StateFlow<LumiUiState> = combine(
        taskDao.getTasks(),
        userDao.getUser()
    ) { tasks, user ->
        LumiUiState(tasks = tasks, user = user ?: User(name = "Guest"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LumiUiState()
    )

    fun addTask(title: String) = viewModelScope.launch {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            status = StatusType.TODO
        )
        taskDao.upsertTask(newTask)
    }

    fun updateTask(taskId: String, newTitle: String, newStatus: StatusType) = viewModelScope.launch {
        val updatedTask = Task(
            id = taskId,
            title = newTitle,
            status = newStatus
        )
        taskDao.upsertTask(updatedTask)
    }

    fun deleteTask(taskId: String) = viewModelScope.launch {
        taskDao.deleteTaskById(taskId)
    }

    fun deleteAllCompleted() = viewModelScope.launch {
        taskDao.deleteAllCompleted()
    }

    fun updateUser(name: String) = viewModelScope.launch {
        userDao.upsertUser(User(name = name))
    }
}
