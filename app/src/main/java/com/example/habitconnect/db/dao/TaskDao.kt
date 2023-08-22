package com.example.habitconnect.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.habitconnect.db.model.Task

@Dao
interface TaskDao {
    @Query("select * from task")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("select * from task where Id=:Id")
    fun getTaskById(Id: Int): Task

    @Query("select * from task where completo=false")
    fun getNonCompleti(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg task: Task)

    @Update
    fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)
}