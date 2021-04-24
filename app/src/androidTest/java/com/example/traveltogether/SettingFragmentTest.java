package com.example.traveltogether;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SettingFragmentTest {

    @Before
    public void setUp() throws InterruptedException {
        FirebaseAuth.getInstance().signOut();
        Object scenario = ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.login_email)).perform(typeText("okellymikeldi@gmail.com"));
        onView(withId(R.id.login_password)).perform(typeText("123456"));
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(5000);
    }

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
