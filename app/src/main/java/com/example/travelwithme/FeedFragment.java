package com.example.travelwithme;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

    public FeedFragment() { }

    public FeedFragment(String email) {
        FeedFragment.email = email;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getActivity().setTitle("Loading...");
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
        new Api().getPosts(userId, posts -> postAdapter.setItems(posts));
    }


    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view_feed);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view);
        postsRecyclerView.setAdapter(postAdapter);
    }
}