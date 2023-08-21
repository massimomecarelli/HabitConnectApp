package com.example.habitconnect

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.db.model.Task
import com.example.habitconnect.viewmodel.FragmentTaskViewModel


//Adapter per la recycle view
class TaskAdapter(private val taskList: List<Task>, val clickListener: ClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    var task_list = taskList
    lateinit var delTask: Task
    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.task_name)
        val taskObiettivo: TextView = view.findViewById(R.id.task_obiettivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // imposta una nuova variabile, faremo l'inflate di qualcosa, prendendo il context del parent
        // e per ogni row fa l'inflate del layout task_row (è il layout per ogni row)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_row, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var task = taskList[position]
        holder.taskName.text = task.nome
        holder.taskObiettivo.text = task.obiettivo.toString()
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(task)
            if(!task.completo){
                holder.itemView.findViewById<TextView>(R.id.task_name).setTextColor(Color.GREEN)
                holder.itemView.findViewById<TextView>(R.id.task_obiettivo).setTextColor(Color.GREEN)
            } else {holder.itemView.findViewById<TextView>(R.id.task_name).setTextColor(Color.BLACK)
                holder.itemView.findViewById<TextView>(R.id.task_obiettivo).setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount() = taskList.size //dice alla recyclerview quanti records abbiamo

    fun setTasks(tasks: List<Task>) {
        this.task_list = tasks
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(task : Task)
    }
}