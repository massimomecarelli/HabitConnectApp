package com.example.habitconnect

import com.example.habitconnect.util.Checker.Companion.isValidEmail
import com.example.habitconnect.util.Checker.Companion.passwordCheck
import com.example.habitconnect.util.Checker.Companion.passwordLengthCheck
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
internal class CheckerTest{
    @Test
    fun checkIsValidEmail() {
        val email = "prova.prova@gmail.com"
        assertTrue(isValidEmail(email))
    }

    @Test
    fun checkPasswordCheck() {
        val password = "test"
        val confirm = "test"
        assertTrue(passwordCheck(password,confirm))
    }

    @Test
    fun checkPaswordLengthCheck() {
        val password = "12345"
        assertFalse(passwordLengthCheck(password))
    }
}