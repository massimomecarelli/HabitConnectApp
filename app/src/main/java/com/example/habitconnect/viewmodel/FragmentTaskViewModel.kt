package com.example.habitconnect.viewmodel

import android.app.Application
import android.provider.CalendarContract.CalendarEntity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitconnect.db.AppDatabase
import com.example.habitconnect.db.AppDatabase.Companion.getInstance

//importa il companion object che definisce un'istanza del db
import com.example.habitconnect.db.model.Task


class FragmentTaskViewModel (application: Application) : AndroidViewModel(application) {
    private val taskDao = getInstance(application).taskDao()

    val tasks by lazy {taskDao.getAllTasks()}
/*
    private var _task = MutableLiveData(Task(0,"",0,false))
    val task: LiveData<Task>
        get() = _task

    private var _taskList = MutableLiveData(listOf<Task>())
    val taskList: LiveData<List<Task>>
        get() = _taskList


    fun getTask(Id: Int){
        _task.value = taskDao.getTaskById(Id)    }

    fun getAllTasks(){
        val x = taskDao.getAllTasks()
        _taskList.value = x
    }

    fun insert(vararg task: Task){
        taskDao.insert(*task)
    }


    */




 /*    lateinit var allTasks : MutableLiveData<List<Task>>

     init {
         allTasks = MutableLiveData()
     }*/

     fun getAllTasksObservers(): LiveData<List<Task>>{
         return tasks
     }

     fun getAllTasks(){
        // val taskDao = AppDatabase.getInstance(getApplication()).taskDao()
     //    val list = taskDao.getAllTasks()
         taskDao.getAllTasks()
         tasks
      //   tasks.postValue(list)
     }

     fun insertTask(entity: Task){
        // val taskDao = AppDatabase.getInstance(getApplication()).taskDao()
         taskDao.insert(entity)
         getAllTasks()
     }

     fun deleteTask(entity: Task){
         taskDao.delete(entity)
         getAllTasks()
     }
}
