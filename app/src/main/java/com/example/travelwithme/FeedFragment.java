package com.example.travelwithme;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.travelwithme.adapter.PostAdapter;


public class FeedFragment extends Fragment {
    private View view;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;
    private static String email;
    ProgressDialog progressDialog;

    public FeedFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString("user_email", "");
        if (savedInstanceState == null) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
            loadUserInfo(); //TODO: load one time!!!
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        initRecyclerView();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUserInfo() {
        new Api().getUser(email, user -> {
            loadPosts(user.getUserID());
            progressDialog.dismiss();
            view.setVisibility(View.VISIBLE);
        });
    }

    private void loadPosts(long userId) {
        new Api().getFollowingsPosts(userId, posts -> postAdapter.setItems(posts));
    }


    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view_feed);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view, false);
        postsRecyclerView.setAdapter(postAdapter);
    }
}