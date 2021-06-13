package com.example.travelwithme.fragments;

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

import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;

import java.util.ArrayList;


public class UsersSearchFragment extends Fragment {
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private View view;
    private String inputText;
    boolean isLoading = false;
    private User currentUser;
    private ArrayList<User> usersList = new ArrayList<>();

    public UsersSearchFragment(String inputText) {
        this.inputText = inputText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_users, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = preferences.getString("user", "");
        currentUser = gson.fromJson(json, User.class);

        initRecyclerView();
        long offset = 0;
        long count = 100;
        if (currentUser != null) {
            searchUsers(currentUser.getUserID(), offset, count);
        }
       // initScrollListener();
        return view;
    }

    private void searchUsers(long userId, long offset, long count) {
        new Api().searchUsers(userId, inputText, offset, count, users -> {
            //     usersAdapter.clearItems();
            usersAdapter.setItems(users);
        });
    }

    private void initRecyclerView() {
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersAdapter.OnUsersClickListener onUsersClickListener = user -> {
            Fragment newFragment = new UsersProfileFragment(user);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };
        usersAdapter = new UsersAdapter(usersList, onUsersClickListener, this);
        usersRecyclerView.setAdapter(usersAdapter);
    }


    private void initScrollListener() {
        usersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == usersList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        usersList.add(null);
        usersAdapter.notifyItemInserted(usersList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            usersList.remove(usersList.size() - 1);
            int scrollPosition = usersList.size();
            usersAdapter.notifyItemRemoved(scrollPosition);
            searchUsers(currentUser.getUserID(), scrollPosition, 10);
            usersAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }

}