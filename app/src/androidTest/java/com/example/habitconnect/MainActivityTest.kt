package com.example.habitconnect

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test // test se la Main activity è mostrata correttamente
    fun activityShown(){
        ActivityScenario.launch(MainActivity::class.java)
        Espresso.onView(withId(R.id.main_layout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(1000)
    }
}