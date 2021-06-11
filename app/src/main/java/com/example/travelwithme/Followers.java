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

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.fragments.UsersProfileFragment;

public class Followers extends Fragment {
    private UsersAdapter followersAdapter;
    private View view;
    private long currentId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.followers_fragment, container, false);
        initRecyclerView();
        loadFollowers();
        return view;
    }

    private void loadFollowers() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String email = preferences.getString("user_email", "");
        final Api api = new Api();

        api.getUser(email, user -> {
            api.getFollowers(user.getUserID(), followersList -> {
                followersAdapter.setItems(followersList);
            });
        });
    }

    private void initRecyclerView() {
        RecyclerView followersRecyclerView = view.findViewById(R.id.followers_recycler_view);
        followersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersAdapter.OnUsersClickListener onUsersClickListener = user -> {
            Fragment newFragment = new UsersProfileFragment(user);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };
        followersAdapter = new UsersAdapter(onUsersClickListener, this);
        followersRecyclerView.setAdapter(followersAdapter);
    }
}
