package com.example.traveltogether;
import com.example.traveltogether.Adapter.*;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TabLayout chat_tab = findViewById(R.id.main_tab);
        TabItem chats = findViewById(R.id.chat);
        TabItem journeys = findViewById(R.id.journeys);
        TabItem profile = findViewById(R.id.profile_tab);
        ViewPager viewPager = findViewById(R.id.view_pager);

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