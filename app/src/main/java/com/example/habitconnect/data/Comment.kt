package com.example.habitconnect.data

import com.google.firebase.Timestamp

data class Comment (
    var user : String? = null,
    var timestamp: Timestamp? = null,
    var testo:String? = null,
)