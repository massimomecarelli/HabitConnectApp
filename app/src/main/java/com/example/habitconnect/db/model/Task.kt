package com.example.habitconnect.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val Id: Int,  // primary key autoincrementale
    val nome: String,
    val obiettivo: Int,
    val completo: Boolean)

