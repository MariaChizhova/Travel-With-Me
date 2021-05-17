package com.example.travelwithme.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.travelwithme.Followers;
import com.example.travelwithme.Following;

import org.jetbrains.annotations.NotNull;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NotNull FragmentManager fragmentManager, int behavior) {
        super(fragmentManager, behavior);
    }

    @Override
    public @NotNull Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Followers();
                break;
            case 1:
                fragment = new Following();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Followers";
            case 1:
                return "Following";
        }
        return null;
    }
}