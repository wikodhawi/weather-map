package com.dhabasoft.weathermap.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.utils.EspressoIdlingResource
import com.dhabasoft.weathermap.view.login.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by dhaba
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(LoginActivity::class.java)

    //make sure user not logged in and if logged in please manually logout that user from stories activity
    @Before
    fun setUp() {
        ActivityScenario.launch(LoginActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    private fun hideKeyboard() {
        onView(ViewMatchers.isRoot()).perform(ViewActions.closeSoftKeyboard())
    }

    @Test
    fun testSuccessLoginToMainActivityWithSpecificUserNameAndPassword() {
        onView(withId(R.id.ed_login_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(typeText("dhaba1@test.com"))
        onView(withId(R.id.ed_login_passwodrd))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ed_login_passwodrd)).perform(typeText("123456"))
        hideKeyboard()
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(6000)
        onView(withId(R.id.btn_stories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testFailedLoginToMainActivityStillInLoginActivityWithSpecificUserNameAndPassword() {
        onView(withId(R.id.ed_login_email))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ed_login_email)).perform(typeText("dhaba1@test.com"))
        onView(withId(R.id.ed_login_passwodrd))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ed_login_passwodrd)).perform(typeText("1234567"))
        hideKeyboard()
        onView(withId(R.id.btn_login)).perform(click())
        Thread.sleep(6000)
        onView(withId(R.id.ed_login_email))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }
}