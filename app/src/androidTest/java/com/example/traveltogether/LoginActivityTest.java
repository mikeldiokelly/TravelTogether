package com.example.traveltogether;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {


////    @Rule
////    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new ActivityScenarioRule<>(LoginActivity.class);
//
////    private ActivityScenario<LoginActivity> chatActivity = null;
//
//    @Before
//    public void setUp() throws Exception {
//        Object scenario = ActivityScenario.launch(LoginActivity.class);
//    }
//
//    //TODO: refactor LoginActivity ... only show login page if no user is logged in, it'll make testing way easier...
//
//    @Test
//    public void testLaunch() throws InterruptedException {
//
//        // somehow do a  sign out here
////        FirebaseAuth.getInstance().signOut();
////        if(instance.getCurrentUser() != null) {
////            instance.signOut();
////            Intent intent = new Intent();
////            intent.setClass(mActivityTestRule, LoginActivity.class);
////            startActivity(intent);
////        }
//
////        Thread.sleep(5000);
////        }
//        onView(withId(R.id.login_email)).perform(typeText("okellymikeldi@gmail.com"));
//        onView(withId(R.id.login_password)).perform(typeText("123456"));
//        onView(withId(R.id.login)).perform(click());
//
////        onView(withId(R.id.text_container)).check(matches(withText(stringToBeChecked)))
//
//        //verify success in login or the other thing,
//        //check the next activity is main activity
//    }
//
//    @After
//    public void tearDown() throws Exception {
////        chatActivity = null;
//    }

}