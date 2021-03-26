package com.example.traveltogether;
import com.example.traveltogether.Adapter.*;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends AppCompatActivity {

    TabItem chats, journeys, profile;
    TabLayout chat_tab;
    ViewPager viewPager;

    public ChatActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeButtons();
        initializePagerAdapter();
    }

    public void initializeButtons(){
        chat_tab = findViewById(R.id.main_tab);
        chats = findViewById(R.id.chat);
        journeys = findViewById(R.id.journeys);
        profile = findViewById(R.id.profile_tab);
        viewPager = findViewById(R.id.view_pager);
    }

    public void initializePagerAdapter(){
        PagerAdapter pagerAdapter = new
                PagerAdapter(getSupportFragmentManager(),
                chat_tab.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        chat_tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
