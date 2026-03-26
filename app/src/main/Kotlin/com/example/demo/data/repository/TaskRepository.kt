package com.example.demo.data.repository

import com.example.demo.data.local.TaskDao
import com.example.demo.data.local.TaskEntity

class TaskRepository(
    private val taskDao: TaskDao
) {
    suspend fun getTasks(): List<TaskEntity> {
        return taskDao.getAllTasks()
    }

    suspend fun addTask(title: String) {
        val task = TaskEntity(title = title)
        taskDao.insertTask(task)
    }

    suspend fun toggleTask(task: TaskEntity) {
        val updatedTask = task.copy(isDone = !task.isDone)
        taskDao.updateTask(updatedTask)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}