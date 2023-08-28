package com.example.habitconnect.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.habitconnect.db.model.Reminder

@Dao
interface ReminderDao {
    @Query("select * from reminder")
    fun getAllReminders(): LiveData<List<Reminder>>

    @Query("select * from reminder where Id=:Id")
    fun getReminderById(Id: Int): Reminder

    @Query("select * from reminder where testo=:testo")
    fun getRemindersByTesto(testo: String): MutableList<Reminder>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg reminder: Reminder)

    @Update
    fun update(reminder: Reminder)
    @Delete
    suspend fun delete(reminder: Reminder)
    @Query("delete from reminder where Id = :Id")
    fun deletebyId(Id: Int)
}