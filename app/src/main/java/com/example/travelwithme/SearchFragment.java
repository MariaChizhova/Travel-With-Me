package com.example.travelwithme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.travelwithme.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

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
        SharedPreferences mSettings = getActivity().getPreferences(Context.MODE_PRIVATE);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(mSettings.getInt("followers_index", 0));
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
            fragmentTransaction.add(R.id.relative_layout, usersSearchFragment);
            fragmentTransaction.commit();
            List<Fragment> fragmentsList = getActivity().getSupportFragmentManager().getFragments();
            for (int i = 0; i < fragmentsList.size() - 1; i++) {
                Fragment fragment = fragmentsList.get(i);
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commit();
            }


        });
        return view;
    }

}
