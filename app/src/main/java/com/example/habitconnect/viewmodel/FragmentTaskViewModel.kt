package com.example.habitconnect.viewmodel

import android.app.Application
import android.provider.CalendarContract.CalendarEntity
import androidx.lifecycle.*
import com.example.habitconnect.db.AppDatabase
import com.example.habitconnect.db.AppDatabase.Companion.getInstance

//importa il companion object che definisce un'istanza del db
import com.example.habitconnect.db.model.Task
import kotlinx.coroutines.launch


class FragmentTaskViewModel (application: Application) : AndroidViewModel(application) {
    private val taskDao = getInstance(application).taskDao()

    val tasks by lazy {taskDao.getAllTasks()}

     fun getAllTasksObservers(): LiveData<List<Task>>{
         return tasks
     }

     fun getAllTasks(){
         taskDao.getAllTasks()
         tasks // inizializza tasks
     }

     fun insertTask(entity: Task){
         taskDao.insert(entity)
         getAllTasks()
     }

     fun deleteTask(entity: Task) = viewModelScope.launch{
         taskDao.delete(entity)
         getAllTasks()
     }

    fun updateTask(entity: Task){
        taskDao.update(entity)
        getAllTasks()
    }


    fun getNonCompleti (): List<Task>{
        return taskDao.getNonCompleti()
    }
}
