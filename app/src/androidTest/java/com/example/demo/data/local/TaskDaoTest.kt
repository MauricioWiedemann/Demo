package com.example.demo.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTask_andGetAllTasks() = runBlocking {
        val task = TaskEntity(title = "Estudiar Room")

        taskDao.insertTask(task)
        val tasks = taskDao.getAllTasks()

        assertEquals(1, tasks.size)
        assertEquals("Estudiar Room", tasks.first().title)
        assertFalse(tasks.first().isDone)
    }

    @Test
    fun updateTask_updatesIsDoneCorrect() = runBlocking {
        val task = TaskEntity(title = "Completar tarea")

        taskDao.insertTask(task)
        val insertedTask = taskDao.getAllTasks().first()

        val updatedTask = insertedTask.copy(isDone = true)
        taskDao.updateTask(updatedTask)

        val tasks = taskDao.getAllTasks()

        assertEquals(1, tasks.size)
        assertTrue(tasks.first().isDone)
    }

    @Test
    fun deleteTask_removesTaskFromDatabase() = runBlocking {
        val task = TaskEntity(title = "Eliminar tarea")

        taskDao.insertTask(task)
        val insertedTask = taskDao.getAllTasks().first()

        taskDao.deleteTask(insertedTask)
        val tasks = taskDao.getAllTasks()

        assertTrue(tasks.isEmpty())
    }
}