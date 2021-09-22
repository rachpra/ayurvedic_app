package com.rozan.liquordeliveryapplication

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginTestingUI {
    @get:Rule
    val testRule= ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLoginUI(){
        onView(withId(R.id.etUsername))
                .perform(typeText("sankar12"))
        Thread.sleep(1000)

        onView(withId(R.id.etPassword))
                .perform(typeText("sankar"))
        Thread.sleep(1000)
        closeSoftKeyboard()

        onView(withId(R.id.btnLogin))
                .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.container))
                .check(matches(isDisplayed()))
    }


}