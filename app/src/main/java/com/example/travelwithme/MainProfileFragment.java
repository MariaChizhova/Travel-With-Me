package com.example.travelwithme;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.api.GetPostsApi;
import com.example.travelwithme.api.GetUserApi;
import com.example.travelwithme.pojo.Post;
import com.example.travelwithme.requests.PostCreateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String email;

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;
    private View view;
    private long currentId = 1;
    boolean isLoading = false;
    private long currentLike = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainProfileFragment(String email) {
        this.email = email;
        System.out.println("EMAIL" + email);
    }

    public MainProfileFragment() {
    }

    public static MainProfileFragment newInstance(String param1, String param2) {
        MainProfileFragment fragment = new MainProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_main, container, false);
        userImageView = view.findViewById(R.id.user_image_view);
        nameTextView = view.findViewById(R.id.user_name_text_view);
        nickTextView = view.findViewById(R.id.user_nick_text_view);
        descriptionTextView = view.findViewById(R.id.user_description_text_view);
        locationTextView = view.findViewById(R.id.user_location_text_view);
        followingCountTextView = view.findViewById(R.id.following_count_text_view);
        followersCountTextView = view.findViewById(R.id.followers_count_text_view);
        initRecyclerView();
        loadUserInfo();


        final Button plus = view.findViewById(R.id.b_plus);
        plus.setOnClickListener(v -> startActivity(new Intent(view.getContext(), MapActivity.class)));

        final Button settings = view.findViewById(R.id.edit_profile);
        settings.setOnClickListener(v -> startActivity(new Intent(view.getContext(), SettingsProfileActivity.class)));

        final Button followersButton = view.findViewById(R.id.followers_count_text_view);
        followersButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Followers()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });
        // TODO: redirect to followings
        final Button followingButton = view.findViewById(R.id.following_count_text_view);
        followingButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Following()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        //    ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
          //  viewPager.setCurrentItem(1);
        });
        return view;
    }

    private void loadPosts(long userId) {
        Collection<Post> postsList = getPosts(userId);
        postAdapter.setItems(postsList);
    }

    private Collection<Post> getPosts(long userID) {
        Collection<Post> lst = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://84.252.137.106:9090")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GetPostsApi getPostsApi = retrofit.create(GetPostsApi.class);
        Call<List<PostCreateRequest>> call = getPostsApi.getPosts(userID);
        call.enqueue(new Callback<List<PostCreateRequest>>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<PostCreateRequest>> call, Response<List<PostCreateRequest>> response) {
                if (response.isSuccessful()) {
                    if(response.body() != null) {
                        for (PostCreateRequest postCreateRequest : response.body()) {
                            lst.add(postCreateRequest.getPost());
                        }
                    } else {
                        Log.i("error", "response body is null");
                    }
                } else {
                    Log.i("error", "error");
                }
            }

            @Override
            public void onFailure(Call<List<PostCreateRequest>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return lst;
    }

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view);
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == postAdapter.postsList.size() - 1) {
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postAdapter.postsList.remove(postAdapter.postsList.size() - 1);
                int scrollPosition = postAdapter.postsList.size();
                postAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                while (currentSize - 1 < nextLimit) {
               //     Post p = new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                 //           1L, ++currentLike, "https://www.w3schools.com/w3css/img_manarola.jpg");
                   // postAdapter.postsList.add(p);
                    currentSize++;
                }
                postAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 1000);
    }


    private void loadUserInfo() {
        new Api().getUser(email, user -> {
            loadPosts(user.getUserID());
            displayUserInfo(user);
        });
    }

    private void displayUserInfo(User user) {
        if(user.getAvatar() != null) {
            Picasso.get().load(user.getAvatar()).into(userImageView);
        } else {
            Picasso.get().load("https://www.w3schools.com/w3images/streetart2.jpg").into(userImageView);
        }
        nameTextView.setText(user.getFirstName());
        nickTextView.setText(user.getLastName());
//        descriptionTextView.setText(user.getDescription());
//        locationTextView.setText(user.getLocation());

        String followingCount = String.valueOf(user.getFollowingsNumber());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersNumber());
        followersCountTextView.setText(followersCount);
    }



}