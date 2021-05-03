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
public class MyJourneysActivityTest {

    @Test
    public void journeyListIsDisplayed_whenMyJourneysIsClicked(){

        Object scenario = ActivityScenario.launch(MainActivity.class);
        onView(withId(R.id.myJourneysBtn)).perform(click());

        // now we are in myJourneys activity
        onView(withId(R.id.myJourneysList)).check(matches(isDisplayed()));
    }


}
