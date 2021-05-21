package com.example.travelwithme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;

public class Followers extends Fragment {
    private UsersAdapter followersAdapter;
    Collection<User> followersList;
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
        followersList = getFollowers();
        System.out.println(followersList.size());
        followersAdapter.setItems(followersList);
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
        followersAdapter = new UsersAdapter(onUsersClickListener);
        followersRecyclerView.setAdapter(followersAdapter);
    }

    private Collection<User> getFollowers() {
        Collection<User> lst = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
//            lst.add(new User(
//                    currentId++,
//                    "https://www.w3schools.com/w3images/streetart2.jpg",
//                    "Andrew",
//                    "@andrew",
//                    "No description",
//                    "Russia",
//                    100,
//                    100,
//                    1
//            ));
        }
        return lst;
    }
}
