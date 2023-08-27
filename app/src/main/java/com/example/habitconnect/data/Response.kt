package com.example.habitconnect.data

data class Response(
    var products: List<Comment>? = null,
    var exception: Exception? = null
)