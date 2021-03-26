package com.example.traveltogether;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SettingFragmentTest {

    @Test
    public void testPreferences_whenClicked(){

        Object scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.settingFragment)).perform(click());

        // now we are in settings fragment

        onView(withText("Journey Preferences")).check(matches(isDisplayed()));
        onView(withId(R.id.setting_userPreference)).perform(click());

        //verify we see customizable preferences
        onView(withText("Gender")).check(matches(isDisplayed()));
        onView(withText("Age")).check(matches(isDisplayed()));
        onView(withText("Minimum Rating")).check(matches(isDisplayed()));

    }

}
