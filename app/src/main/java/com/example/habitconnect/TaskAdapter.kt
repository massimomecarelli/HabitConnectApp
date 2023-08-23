package com.example.habitconnect

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
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
        //di seguito fa il controllo se il task è completo
        if(task.completo) {
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraint_row).setBackgroundColor(Color.rgb(99, 200, 99))
            holder.itemView.findViewById<TextView>(R.id.task_name).setTextColor(Color.WHITE)
            holder.itemView.findViewById<TextView>(R.id.task_obiettivo).setTextColor(Color.WHITE)
            holder.itemView.findViewById<ImageView>(R.id.imageTask).setImageResource(R.drawable.ic_baseline_check_24)
        }
        //inserisco un onClickListener
        holder.itemView.setOnClickListener {
            //esegue la funzione onItemClick che implemento nel FragmentTask
            clickListener.onItemClick(task)
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