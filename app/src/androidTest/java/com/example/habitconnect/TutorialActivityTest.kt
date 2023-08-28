package com.example.habitconnect

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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


    @Test // test per fare il check che al primo click su next, sia visibile prev button
    fun isPrevButtonDisplayed() {
        Espresso.onView(withId(R.id.next_button)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.prev_button))
            .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

}