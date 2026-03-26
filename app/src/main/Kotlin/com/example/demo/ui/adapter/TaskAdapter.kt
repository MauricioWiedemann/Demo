package com.example.demo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.data.local.TaskEntity

class TaskAdapter(
    private val onTaskChecked: (TaskEntity) -> Unit,
    private val onTaskDeleted: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var tasks: List<TaskEntity> = emptyList()

    fun updateTasks(newTasks: List<TaskEntity>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val checkTaskDone: CheckBox = itemView.findViewById(R.id.checkTaskDone)
        val btnDeleteTask: ImageButton = itemView.findViewById(R.id.btnDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTaskTitle.text = task.title
        holder.checkTaskDone.isChecked = task.isDone

        holder.checkTaskDone.setOnClickListener {
            onTaskChecked(task)
        }

        holder.btnDeleteTask.setOnClickListener {
            onTaskDeleted(task)
        }
    }

    override fun getItemCount(): Int = tasks.size
}