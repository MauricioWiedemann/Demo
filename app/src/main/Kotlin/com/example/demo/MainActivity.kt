package com.example.demo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.data.local.AppDatabase
import com.example.demo.data.repository.TaskRepository
import com.example.demo.ui.adapter.TaskAdapter
import com.example.demo.ui.main.TaskViewModel
import com.example.demo.util.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var editTaskTitle: EditText
    private lateinit var btnAddTask: Button
    private lateinit var rvTasks: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())
        ViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupRecyclerView()
        setupListeners()
        observeUiState()

        viewModel.loadTasks()
    }

    private fun initViews() {
        editTaskTitle = findViewById(R.id.editTaskTitle)
        btnAddTask = findViewById(R.id.btnAddTask)
        rvTasks = findViewById(R.id.rvTasks)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskChecked = { task ->
                viewModel.toggleTask(task)
            },
            onTaskDeleted = { task ->
                viewModel.deleteTask(task)
            }
        )

        rvTasks.layoutManager = LinearLayoutManager(this)
        rvTasks.adapter = taskAdapter
    }

    private fun setupListeners() {
        btnAddTask.setOnClickListener {
            val title = editTaskTitle.text.toString()
            viewModel.addTask(title)

            if (title.isNotBlank()) {
                editTaskTitle.text.clear()
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    taskAdapter.updateTasks(state.tasks)

                    state.error?.let { errorMessage ->
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}