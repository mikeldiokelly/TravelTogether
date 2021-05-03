package com.example.traveltogether;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CreateJourneyActivityTest {

    @Test
    public void srcBtn_whenClicked(){

        Object scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.CreateJourneyBtn)).perform(click());

        // now we are in create journey activity
        onView(withId(R.id.srcBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.srcBtn)).perform(click());

        //verify we see customizable preferences
        onView(withId(R.id.mapView2)).check(matches(isDisplayed()));

    }

    @Test
    public void destBtn_whenClicked(){

        Object scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.CreateJourneyBtn)).perform(click());

        // now we are in create journey activity
        onView(withId(R.id.destBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.destBtn)).perform(click());

        //verify we see customizable preferences
        onView(withId(R.id.mapView2)).check(matches(isDisplayed()));

    }


}
