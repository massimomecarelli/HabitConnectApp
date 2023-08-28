package com.example.habitconnect

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Timer

@RunWith(AndroidJUnit4::class)
class TimerActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<TimerActivity> = ActivityScenarioRule(TimerActivity::class.java)


    @Test // test dello stop del timer per vedere se i bottoni si abilitano e disabilitano correttamente
    fun isTimerStopped() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab_stop)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.fab_stop))
            .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_start))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_pause))
            .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()))
    }

    @Test // test dello stop e restart del timer per vedere se i bottoni si abilitano e disabilitano correttamente
    fun isTimerRestarted() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab_stop)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab_start)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.fab_start))
            .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_pause))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_stop))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

    @Test // test della pausa del timer per vedere se i bottoni si abilitano e disabilitano correttamente
    fun isTimerPaused() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab_pause)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.fab_pause))
            .check(ViewAssertions.matches(ViewMatchers.isNotEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_stop))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        Espresso.onView(ViewMatchers.withId(R.id.fab_start))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

}