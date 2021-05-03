package com.example.traveltogether;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class PeerToPeerActivityTest {

    @Test
    public void verifyTheInterface_inPeerToPeerActivity(){

        Object scenario = ActivityScenario.launch(PeerToPeerActivity.class);
        onView(withId(R.id.wifiSwitch)).check(matches(isDisplayed()));
        onView(withId(R.id.discoverPeers)).check(matches(isDisplayed()));
        onView(withId(R.id.peersListView)).check(matches(isDisplayed()));
        onView(withId(R.id.readMsgSentByPeer)).check(matches(isDisplayed()));
        onView(withId(R.id.writeMsgToPeer)).check(matches(isDisplayed()));
        onView(withId(R.id.sendButtonP2P)).check(matches(isDisplayed()));
        onView(withId(R.id.connectionStatusP2P)).check(matches(isDisplayed()));
        onView(withId(R.id.deviceNameMessage)).check(matches(isDisplayed()));

    }


}
