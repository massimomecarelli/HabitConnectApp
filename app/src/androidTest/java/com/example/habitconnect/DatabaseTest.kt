package com.example.habitconnect

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.habitconnect.db.AppDatabase
import com.example.habitconnect.db.dao.ReminderDao
import com.example.habitconnect.db.dao.TaskDao
import com.example.habitconnect.db.model.Reminder
import com.example.habitconnect.db.model.Task
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

import org.junit.Before
import java.io.IOException
import java.util.*


class DatabaseTest {

    private lateinit var db:AppDatabase
    private lateinit var taskDao:TaskDao
    private lateinit var reminderDao: ReminderDao
    private val task = Task(0,"taskTestTest",1,false)
    private val reminder = Reminder(0,"reminderTestTest", "2023.9.12", "20:00")
    private val list_reminder : MutableList<Reminder> = mutableListOf(reminder)
    private val list_task : MutableList<Task> = mutableListOf(task)


    @Before
    fun setUp() {
        val context:Context = ApplicationProvider.getApplicationContext()
        db = AppDatabase.getInstance(context)
        taskDao = db.taskDao()
        reminderDao = db.reminderDao()
    }

    @After
    @Throws(IOException::class)
    fun reset() {
        if (!reminderDao.getRemindersByTesto("reminderTestTest").isNullOrEmpty())
            reminderDao.deletebyId(reminderDao.getRemindersByTesto("reminderTestTest")[0].Id)
        if (!taskDao.getTaskByNome("taskTestTest").isNullOrEmpty())
            taskDao.deletebyId(taskDao.getTaskByNome("taskTestTest")[0].Id)
    //    db.close()
    }

    @Test
    fun writeAndReadReminder()  {
        reminderDao.insert(reminder)
        val getReminder = reminderDao.getRemindersByTesto("reminderTestTest")
        assertEquals(getReminder[0].testo, list_reminder[0].testo)
    }

    @Test
    fun writeAndReadTask()  {
        taskDao.insert(task)
        val get_task = taskDao.getTaskByNome("taskTestTest")
        assertEquals(get_task[0].nome, list_task[0].nome)
    }

}