package com.dhabasoft.weathermap.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dhabasoft.weathermap.R
import com.dhabasoft.weathermap.utils.EspressoIdlingResource
import com.dhabasoft.weathermap.view.stories.StoriesActivity
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
class StoriesActivityTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(StoriesActivity::class.java)

    //make sure user already logged in and if not please logged in please manually
    @Before
    fun setUp() {
        ActivityScenario.launch(StoriesActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testLoadStoriesDataAndScrollToBottom() {
        Espresso.onView(withId(R.id.rcy_stories)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        Espresso.onView(withId(R.id.rcy_stories)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
        Thread.sleep(3000)
        Espresso.onView(withId(R.id.rcy_stories)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                20
            )
        )
    }

    @Test
    fun testGoToDetailStory() {
        Espresso.onView(withId(R.id.rcy_stories)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(withId(R.id.tv_detail_description)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}