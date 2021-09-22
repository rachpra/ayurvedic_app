package com.rozan.liquordeliveryapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
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
class SignUpTestingUI {
    @get:Rule
    val testRule2= ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun checkSignUpUI(){
        Espresso.onView(withId(R.id.etFname))
                .perform(typeText("rojan"))
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.etLname))
                .perform(typeText("shrestha"))
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.etUsername))
                .perform(typeText("rojjan23"))
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.etEmail))
                .perform(typeText("roJJan@gmail.com"))
        Thread.sleep(1000)

        Espresso.onView(withId(R.id.etPassword))
                .perform(typeText("roJan"))
        Thread.sleep(1000)
        closeSoftKeyboard()

        Espresso.onView(withId(R.id.btnSignup))
                .perform(click())
        Thread.sleep(2000)

        Espresso.onView(withId(R.id.btnLogin))
                .check(matches(isDisplayed()))
    }
}