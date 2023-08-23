package com.example.habitconnect.viewmodel

import android.app.Application
import android.provider.CalendarContract.CalendarEntity
import androidx.lifecycle.*
import com.example.habitconnect.db.AppDatabase

//importa il companion object che definisce un'istanza del db
import com.example.habitconnect.db.model.Reminder
import kotlinx.coroutines.launch

class FragmentRemindersViewModel(application: Application) : AndroidViewModel(application) {
    private val _reminderDao = AppDatabase.getInstance(application).reminderDao()
    val reminders by lazy {_reminderDao.getAllReminders()}

    fun getAllremindersObservers(): LiveData<List<Reminder>> {
        return reminders
    }

    fun getAllReminders(){
        _reminderDao.getAllReminders()
        reminders
    }

    fun insertReminder(entity: Reminder){
        _reminderDao.insert(entity)
        getAllReminders()
    }

    fun deleteReminder(entity: Reminder) = viewModelScope.launch{
        _reminderDao.delete(entity)
        getAllReminders()
    }

    fun updateReminder(entity: Reminder){
        _reminderDao.update(entity)
        getAllReminders()
    }
}
