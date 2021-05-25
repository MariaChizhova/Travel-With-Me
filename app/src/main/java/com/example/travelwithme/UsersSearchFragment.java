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


public class UsersSearchFragment extends Fragment {
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private View view;
    private String inputText;

    public UsersSearchFragment(String inputText) {
        this.inputText = inputText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_users, container, false);
        initRecyclerView();
        searchUsers();
        return view;
    }

    private void searchUsers() {
        new Api().searchUsers(52, inputText, users -> {
            usersAdapter.clearItems();
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
        usersAdapter = new UsersAdapter(onUsersClickListener, this);
        usersRecyclerView.setAdapter(usersAdapter);
    }
}