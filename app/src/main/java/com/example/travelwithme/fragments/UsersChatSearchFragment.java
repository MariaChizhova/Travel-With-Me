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
import com.example.travelwithme.adapter.ChatsAdapter;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;

import java.util.ArrayList;


public class UsersChatSearchFragment extends Fragment {
    private RecyclerView usersRecyclerView;
    private ChatsAdapter usersAdapter;
    private View view;
    private String inputText;
    boolean isLoading = false;
    private User currentUser;
    private static String email;
    private ArrayList<User> usersList = new ArrayList<>();

    public UsersChatSearchFragment(String inputText) {
        this.inputText = inputText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_chat_search, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString("user_email", "");
        Gson gson = new Gson();
        String json = preferences.getString("user", "");
        currentUser = gson.fromJson(json, User.class);

        initRecyclerView();
        long offset = 0;
        long count = 10;
        if (currentUser != null) {
            searchUsers(currentUser.getUserID(), offset, count);
        }
        initScrollListener();
        return view;
    }

    private void searchUsers(long userId, long offset, long count) {
        new Api().searchUsers(userId, inputText, offset, count, users -> {
            // usersAdapter.clearItems();
            usersList.addAll(users);
            usersAdapter.notifyDataSetChanged();
        });
    }

    private void initRecyclerView() {
        usersRecyclerView = view.findViewById(R.id.users_recycler_view_chat);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ChatsAdapter.OnChatClickListener onChatClickListener = user -> new Api().getUser(email, currentUser -> {
            Long id1 = user.getUserID();
            Long id2 = currentUser.getUserID();
            if (id1 > id2) {
                Long tmp = id1;
                id1 = id2;
                id2 = tmp;
            }
            Fragment newFragment = new ChatFragment(id1.toString(), id2.toString());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        usersAdapter = new ChatsAdapter(usersList, onChatClickListener);
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