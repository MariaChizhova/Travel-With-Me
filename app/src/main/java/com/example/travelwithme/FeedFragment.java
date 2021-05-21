package com.example.travelwithme;

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
import com.example.travelwithme.api.GetPostApi;
import com.example.travelwithme.pojo.Post;
import com.example.travelwithme.requests.PostCreateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedFragment extends Fragment {
    private View view;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;

    public FeedFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        initRecyclerView();
        loadPosts();
        return view;
    }

    private void loadPosts() {
        Collection<Post> postsList = getPosts();
        postAdapter.setItems(postsList);
    }

    private Collection<Post> getPosts() {
        Collection<Post> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetPostApi getPostApi = retrofit.create(GetPostApi.class);
        Call<PostCreateRequest> call = getPostApi.getPost((long) 5);
        call.enqueue(new Callback<PostCreateRequest>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<PostCreateRequest> call, Response<PostCreateRequest> response) {
                PostCreateRequest postCreateRequest = response.body();
                if(postCreateRequest != null) {
                    lst.add(postCreateRequest.getPost());
                }
            }

            @Override
            public void onFailure(Call<PostCreateRequest> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return lst;
    }

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view_feed);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view);
        postsRecyclerView.setAdapter(postAdapter);
    }
}