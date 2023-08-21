package com.example.habitconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.db.model.Task


//Adapter per la recycle view
class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var task_list = taskList
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.task_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // imposta una nuova variabile, faremo l'inflate di qualcosa, prendendo il context del parent
        // e per ogni row fa l'inflate del layout task_row (è il layout per ogni row)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.taskName.text = task.nome
    }

    override fun getItemCount() = taskList.size //dice alla recyclerview quanti records abbiamo

    fun setTasks(tasks: List<Task>) {
        this.task_list = tasks
        notifyDataSetChanged()
    }
}