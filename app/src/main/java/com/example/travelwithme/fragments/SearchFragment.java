package com.example.travelwithme.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.travelwithme.R;
import com.example.travelwithme.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(mSettings.getInt("followers_index", 0));
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Button searchButton = toolbar.findViewById(R.id.search_button);
        EditText searchEditText = toolbar.findViewById(R.id.search_edit_text);
        searchButton.setOnClickListener(v -> {
            viewPager.removeAllViews();
            viewPagerAdapter.notifyDataSetChanged();
            tabLayout.removeAllTabs();
            String inputText = searchEditText.getText().toString();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UsersSearchFragment usersSearchFragment = new UsersSearchFragment(inputText);
            fragmentTransaction.replace(R.id.relative_layout, usersSearchFragment);
            fragmentTransaction.commit();
        });
        return view;
    }


}

