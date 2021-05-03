package com.example.traveltogether.Adapter;

import com.example.traveltogether.Fragments.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mNumOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new JourneysFragment();
            case 2:
                return new ProfileFragment();
            default:
                return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
