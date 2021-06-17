package com.example.travelwithme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.fragments.UsersProfileFragment;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;

import java.util.ArrayList;


public class Following extends Fragment {
    private UsersAdapter followingAdapter;
    private RecyclerView followingRecyclerView;
    private View view;
    boolean isLoading = false;
    private User currentUser;
    private final ArrayList<User> followingList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.following_fragment, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = preferences.getString("user", "");
        currentUser = gson.fromJson(json, User.class);

        initRecyclerView();
        long offset = 0;
        long count = 30;
        if (currentUser != null) {
            loadFollowing(currentUser.getUserID(), offset, count);
        }
        initScrollListener();
        return view;
    }

    private void loadFollowing(long userId, long offset, long count) {
        new Api().getFollowing(userId, offset, count, lst -> {
            followingList.addAll(lst);
            followingAdapter.notifyDataSetChanged();
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
        followingAdapter = new UsersAdapter(followingList, onUsersClickListener, this);
        followingRecyclerView.setAdapter(followingAdapter);
    }


    private void initScrollListener() {
        followingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == followingList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        followingList.add(null);
        followingAdapter.notifyItemInserted(followingList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            followingList.remove(followingList.size() - 1);
            int scrollPosition = followingList.size();
            followingAdapter.notifyItemRemoved(scrollPosition);
            loadFollowing(currentUser.getUserID(), scrollPosition, 10);
            followingAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }


}
