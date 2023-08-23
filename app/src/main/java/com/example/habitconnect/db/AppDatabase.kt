package com.example.habitconnect.db

import android.content.Context
import androidx.room.*
import com.example.habitconnect.db.dao.*
import com.example.habitconnect.db.model.*
import java.util.*

@Database(entities = [Task::class,
                     Reminder::class], version=3)
@TypeConverters(Converters::class)

abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun reminderDao(): ReminderDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}

// per salvare una data su room bisogna convertirla in tipo Long
class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.time?.toString()
    }
}