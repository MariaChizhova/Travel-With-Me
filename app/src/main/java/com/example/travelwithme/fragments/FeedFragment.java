package com.example.travelwithme.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.adapter.PostAdapter;


public class FeedFragment extends Fragment {
    private View view;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;
    private static String email;
    private ProgressDialog progressDialog;
    boolean isLoading = false;

    public FeedFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString("user_email", "");
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        loadPosts(0, 5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        initRecyclerView();
        initScrollListener();
        return view;
    }

    private void loadPosts(long offset, long count) {
        Api api = new Api();
        api.getUser(email, user -> {
            api.getFollowingsPosts(user.getUserID(), offset, count, posts -> {
                postAdapter.setItems(posts);
                progressDialog.dismiss();
                view.setVisibility(View.VISIBLE);
            });
        });
    }

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view_feed);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view, false, email);
        postsRecyclerView.setAdapter(postAdapter);
    }

    private void initScrollListener() {
        postsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() == postAdapter.postsList.size() - 1) {
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        postAdapter.postsList.add(null);
        postAdapter.notifyItemInserted(postAdapter.postsList.size() - 1);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            postAdapter.postsList.remove(postAdapter.postsList.size() - 1);
            int scrollPosition = postAdapter.postsList.size();
            postAdapter.notifyItemRemoved(scrollPosition);
            loadPosts(scrollPosition, 5);
            postAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 1000);
    }
}