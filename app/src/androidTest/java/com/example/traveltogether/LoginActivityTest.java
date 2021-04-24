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
public class LoginActivityTest {

    @Before
    public void setUp(){
        FirebaseAuth.getInstance().signOut();
        Object scenario = ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void testLaunch() throws InterruptedException {


        onView(withId(R.id.login_email)).perform(typeText("okellymikeldi@gmail.com"));
        onView(withId(R.id.login_password)).perform(typeText("123456"));
        onView(withId(R.id.login)).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.settingFragment)).check(matches(isDisplayed()));
    }


}