package com.example.habitconnect

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.habitconnect.util.PrefUtil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4



@RunWith(JUnit4::class) // serve per creare un mock per prendere il context
class PrefUtilTest {
    private lateinit var appContext: Context

    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext()
        PrefUtil.setTimerLength(40, appContext)
    }

    @Test
    fun checkGetTimerLength() {
        assertEquals(PrefUtil.getTimerLength(appContext), 40)
    }
}