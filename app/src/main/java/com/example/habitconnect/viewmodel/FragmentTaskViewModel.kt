package com.example.habitconnect.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.habitconnect.db.AppDatabase.Companion.getInstance
//importa il companion object che definisce un'istanza del db
import com.example.habitconnect.db.model.Task


class FragmentTaskViewModel (application: Application) : AndroidViewModel(application) {
        private val taskDao = getInstance(application).taskDao()
        val tasks: LiveData<List<Task>> = taskDao.getAllTasks()
    }
