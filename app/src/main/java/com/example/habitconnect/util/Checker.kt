package com.example.habitconnect.util

import android.util.Patterns
import java.util.regex.Pattern

class Checker {
    companion object {
        val EMAIL_ADDRESS: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        fun isValidEmail(email: String): Boolean {
            return EMAIL_ADDRESS.matcher(email).matches()
        }

        fun passwordCheck(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }

        fun passwordLengthCheck(password: String): Boolean {
            return password.length >= 6
        }
    }
}