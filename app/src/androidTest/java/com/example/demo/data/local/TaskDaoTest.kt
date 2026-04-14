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

    // Definir la base de datos de prueba que se utilizara antes de ejecutar las pruebas
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        taskDao = database.taskDao()
    }

    // Cerrar la base de datos al terminar con las pruebas
    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTask_andGetAllTasks() = runBlocking {
        // Crear la entidad a insertar
        val task = TaskEntity(title = "Estudiar Room")
        // Insertar la entidad
        taskDao.insertTask(task)
        // Obtener la lista de todas entidades de la bd
        val tasks = taskDao.getAllTasks()

        // Validar que la lista tiene un largo de 1
        assertEquals(1, tasks.size)
        // Validar que el item tenga el titulo especificado
        assertEquals("Estudiar Room", tasks.first().title)
        // Validar que el parametro isDone sea falsa
        assertFalse(tasks.first().isDone)
    }

    @Test
    fun updateTask_updatesIsDoneCorrect() = runBlocking {
        // Crear, insertar y obtener la entidad
        val task = TaskEntity(title = "Completar tarea")
        taskDao.insertTask(task)
        val insertedTask = taskDao.getAllTasks().first()

        // Realizar la modificacion del parametro isDone a True
        val updatedTask = insertedTask.copy(isDone = true)
        // Persistir el cambio
        taskDao.updateTask(updatedTask)

        // Obtener nuevamente las entidades
        val tasks = taskDao.getAllTasks()

        // Validar que la lista tiene un largo de 1
        assertEquals(1, tasks.size)
        // Validar que el parametro isDone sea true
        assertTrue(tasks.first().isDone)
    }

    @Test
    fun deleteTask_removesTaskFromDatabase() = runBlocking {
        // Crear, insertar y obtener la entidad
        val task = TaskEntity(title = "Eliminar tarea")
        taskDao.insertTask(task)
        val insertedTask = taskDao.getAllTasks().first()

        // Se elimina la tarea insertada
        taskDao.deleteTask(insertedTask)

        // Obtener nuevamente las entidades
        val tasks = taskDao.getAllTasks()

        // Validar que la lista esta vacia
        assertTrue(tasks.isEmpty())
    }
}