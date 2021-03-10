package com.example.traveltogether.Adapter;
import com.example.traveltogether.Fragments.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super (fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
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
        return numOfTabs;
    }
}
