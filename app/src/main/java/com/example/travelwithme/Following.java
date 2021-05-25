package com.example.travelwithme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;

public class Following extends Fragment {
    private UsersAdapter followingAdapter;
    private RecyclerView followingRecyclerView;
    private View view;
    private long currentId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.following_fragment, container, false);
        initRecyclerView();
        loadFollowing();

        //ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        //viewPager.setCurrentItem(1);
        return view;
    }

    private void loadFollowing() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String email = preferences.getString("user_email", "");
        final Api api = new Api();

        api.getUser(email, user -> {
            api.getFollowing(user.getUserID(), followersList -> {
                followingAdapter.setItems(followersList);
            });
        });
    }

    private void initRecyclerView() {
        followingRecyclerView = view.findViewById(R.id.following_recycler_view);
        followingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersAdapter.OnUsersClickListener onUsersClickListener = user -> {
            Fragment newFragment = new UsersProfileFragment(user);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };
        followingAdapter = new UsersAdapter(onUsersClickListener, this);
        followingRecyclerView.setAdapter(followingAdapter);
    }
}
