package com.example.habitconnect.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.habitconnect.db.model.Reminder

@Dao
interface ReminderDao {
    @Query("select * from reminder")
    fun getAllReminders(): List<Reminder>

    @Query("select * from reminder where Id=:Id")
    fun getReminderById(Id: Int): Reminder

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg task: Reminder)

    @Update
    fun update(reminder: Reminder)
    @Delete
    fun delete(reminder: Reminder)
}