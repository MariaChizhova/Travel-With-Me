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
import androidx.viewpager.widget.ViewPager;

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;

public class Following extends Fragment {
    private UsersAdapter followingAdapter;
    private RecyclerView followingRecyclerView;
    Collection<User> followingList;
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
        followingList = getFollowing();
        followingAdapter.setItems(followingList);
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
        followingAdapter = new UsersAdapter(onUsersClickListener);
        followingRecyclerView.setAdapter(followingAdapter);
    }

    private Collection<User> getFollowing() {
        Collection<User> lst = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            lst.add(new User(
                    currentId,
                    "https://sun9-6.userapi.com/impf/c845217/v845217483/8560e/LRcTo6l9VBE.jpg?size=1864x2048&quality=96&sign=b2f39df96c4d2fe3e6f16f6df6f528c5&type=album",
                    "Anna",
                    "@anchouls",
                    "I love teorver",
                    "Russia",
                    100,
                    100,
                    1
            ));
            lst.add(new User(
                    currentId,
                    "https://www.w3schools.com/w3images/streetart2.jpg",
                    "Andrew",
                    "@andrew",
                    "No description",
                    "Russia",
                    100,
                    100,
                    1
            ));
        }
        return lst;
    }
}