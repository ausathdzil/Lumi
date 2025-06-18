package com.example.lumi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.lumi.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: String)

    @Query("DELETE FROM tasks WHERE status = 'COMPLETED'")
    suspend fun deleteAllCompleted()

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    fun getTasks(): Flow<List<Task>>
}
