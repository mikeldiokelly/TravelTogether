package com.example.traveltogether;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.traveltogether.Model.Preferences;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class PreferenceActivityTest {


    @Test
    public void test_setting_preferences(){
        Object scenario = ActivityScenario.launch(PreferenceActivity.class);

        //verify we see customizable preferences
        onView(withText("Gender")).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerGender)).perform(click());
        String selection = Preferences.listGender.get(0);
        onData(allOf(is(instanceOf(String.class)), is(selection))).perform(click());
        onView(withId(R.id.spinnerGender)).check(matches(withSpinnerText(selection)));

        onView(withText("Age")).check(matches(isDisplayed()));
        onView(withId(R.id.spinnerAge)).perform(click());
        selection = Preferences.listAge.get(0);
        onData(allOf(is(instanceOf(String.class)), is(selection))).perform(click());
        onView(withId(R.id.spinnerAge)).check(matches(withSpinnerText(selection)));

        onView(withText("Minimum Rating")).check(matches(isDisplayed()));
        onView(withId(R.id.seekBar)).perform(click());
        //test the seekbar further if you want..

    }
}
