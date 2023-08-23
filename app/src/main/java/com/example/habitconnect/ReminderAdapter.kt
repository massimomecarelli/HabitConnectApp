package com.example.habitconnect

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.habitconnect.db.model.Reminder
import com.example.habitconnect.viewmodel.FragmentRemindersViewModel


//Adapter per la recycle view
class ReminderAdapter(private val reminderList: List<Reminder>, val clickListener: ClickListener) : RecyclerView.Adapter<ReminderAdapter.RemindersViewHolder>() {
    var reminder_list = reminderList

    class RemindersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reminderName: TextView = view.findViewById(R.id.TestoNotifica)
        val reminderDate: TextView = view.findViewById(R.id.Date)
        val reminderTime: TextView = view.findViewById(R.id.Time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        // imposta una nuova variabile, faremo l'inflate di qualcosa, prendendo il context del parent
        // e per ogni row fa l'inflate del layout task_row (è il layout per ogni row)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_row, parent, false)
        return RemindersViewHolder(view)
    }

    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        var reminder = reminderList[position]
        holder.reminderName.text = reminder.testo
        holder.reminderDate.text = reminder.data.toString()
        holder.reminderTime.text = reminder.time.toString()
        holder.itemView.setOnClickListener {
            //esegue la funzione onItemClick che implemento nel FragmentReminder
            clickListener.onItemClick(reminder)
        }
    }

    override fun getItemCount() = reminderList.size

    fun setTasks(tasks: List<Reminder>) {
        this.reminder_list = tasks
        notifyDataSetChanged()
    }

    interface ClickListener {
        fun onItemClick(reminder : Reminder)
    }
}