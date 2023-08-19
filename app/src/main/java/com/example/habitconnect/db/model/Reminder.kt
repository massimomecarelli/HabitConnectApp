package com.example.habitconnect.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val Id: Int,  // primary key autoincrementale
    val testo: String,
    val data: Long)
