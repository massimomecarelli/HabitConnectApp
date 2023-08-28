package com.example.habitconnect

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TutorialActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<TutorialActivity> = ActivityScenarioRule(TutorialActivity::class.java)


    // test per fare il check che al primo click su next, sia visibile prev button
    // e al successivo click su prev questo torna invisibile
    @Test
    fun isPrevButtonDisplayed() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.next_button)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.prev_button))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.prev_button)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.prev_button))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
    }

    /*
    12 click su next: verifica che next sia invisibile,
    poi clicca su prev e verifica che next sia visibile
     */
    @Test
    fun isNextButtonHidden() {
        Thread.sleep(500)
        Espresso.onView(withId(R.id.next_button))
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click())
            .perform(ViewActions.click()) // 12 click
        Espresso.onView(withId(R.id.next_button))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Thread.sleep(500)
        Espresso.onView(withId(R.id.prev_button)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.next_button))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}