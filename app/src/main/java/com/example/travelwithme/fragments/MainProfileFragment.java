package com.example.travelwithme.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.preference.PreferenceManager;
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

import com.adamstyrc.cookiecutter.ImageUtils;
import com.example.travelwithme.Api;
import com.example.travelwithme.Followers;
import com.example.travelwithme.Following;
import com.example.travelwithme.map.MapActivity;
import com.example.travelwithme.R;
import com.example.travelwithme.SettingsProfileActivity;
import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.api.EditAvatarApi;
import com.example.travelwithme.requests.AvatarEditRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.travelwithme.pojo.User;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainProfileFragment extends Fragment {

    private static String email;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_EDIT_USER = 2;

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView lastNameTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;
    private View view;
    private long currentId = 1;
    boolean isLoading = false;
    private User currentUser;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public MainProfileFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static MainProfileFragment newInstance(String param1, String param2) {
        MainProfileFragment fragment = new MainProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString("user_email", "");

        Gson gson = new Gson();
        String json = preferences.getString("user", null);
        if (json != null) {
            currentUser = gson.fromJson(json, User.class);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_main, container, false);
        userImageView = view.findViewById(R.id.user_image_view);
        nameTextView = view.findViewById(R.id.user_name_text_view);
        lastNameTextView = view.findViewById(R.id.user_nick_text_view);
        descriptionTextView = view.findViewById(R.id.user_description_text_view);
        locationTextView = view.findViewById(R.id.user_location_text_view);
        followingCountTextView = view.findViewById(R.id.following_count_text_view);
        followersCountTextView = view.findViewById(R.id.followers_count_text_view);
        initRecyclerView();

        userImageView.setOnClickListener(v -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });


        final Button plus = view.findViewById(R.id.b_plus);
        plus.setOnClickListener(v -> startActivity(new Intent(view.getContext(), MapActivity.class)));

        final Button settings = view.findViewById(R.id.edit_profile);
        settings.setOnClickListener(v -> {
            Intent i = new Intent(view.getContext(), SettingsProfileActivity.class);
            i.putExtra("email", email);
            startActivityForResult(i, RESULT_EDIT_USER);
        });

        final Button followersButton = view.findViewById(R.id.followers_count_text_view);
        followersButton.setOnClickListener(v -> {
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mSettings.edit().putInt("followers_index", 0).apply();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Followers()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });
        final Button followingButton = view.findViewById(R.id.following_count_text_view);
        followingButton.setOnClickListener(v -> {
            SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            mSettings.edit().putInt("followers_index", 1).apply();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Following()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });

        if (currentUser != null) {
            loadPosts(currentUser.getUserID(), 0, 5);
            displayUserInfo(currentUser);
        } else {
            loadUserInfo();
        }

    //    initScrollListener();

        return view;
    }

    private void loadPosts(long userId, long offset, long count) {
        new Api().getPosts(userId, offset, count, posts -> {
            postAdapter.setItems(posts);
        });
    }

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view, true, email);
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
            new Api().getUser(email, u -> {
                postAdapter.postsList.remove(postAdapter.postsList.size() - 1);
                int scrollPosition = postAdapter.postsList.size();
                postAdapter.notifyItemRemoved(scrollPosition);
                loadPosts(u.getUserID(), scrollPosition, 5);
                postAdapter.notifyDataSetChanged();
                isLoading = false;
            });
        }, 1000);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUserInfo() {
        new Api().getUser(email, u -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            Gson gson = new Gson();
            String json = gson.toJson(u);
            preferences.edit().putString("user", json).apply();
            currentUser = u;

            loadPosts(u.getUserID(), 0, 5);
            displayUserInfo(u);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_EDIT_USER) {
            postAdapter.del();
            loadUserInfo();
        }

        if (requestCode == RESULT_LOAD_IMAGE && data != null) {
            Uri imageUri = data.getData();
            try {

                Point screenSize = ImageUtils.getScreenSize(getContext());
                Bitmap scaledBitmap = ImageUtils.decodeUriToScaledBitmap(getContext(), imageUri, screenSize.x, screenSize.y);
                userImageView.setImageBitmap(scaledBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://84.252.137.106:9090")
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                EditAvatarApi editAvatarApi = retrofit.create(EditAvatarApi.class);
                AvatarEditRequest avatarEditRequest = new AvatarEditRequest();
                avatarEditRequest.setUserId(currentUser.getUserID());
                avatarEditRequest.setAvatar(Base64.getEncoder().encodeToString(bytes));
                Call<Void> call = editAvatarApi.editAvatar(avatarEditRequest);
                call.enqueue(new Callback<Void>() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.i("succsess", "sucses to edit avavtar");
                            postAdapter.del();
                            loadUserInfo();
                        } else {
                            Log.i("error", response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.i("error", "error to edit avavtar 2");
                        t.printStackTrace();
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayUserInfo(User user) {
        if (user.getAvatar() != null) {
            Picasso.get().load(PostAdapter.S3IMAGES + user.getAvatar()).into(userImageView);
        }
        if (user.getFirstName() != null) {
            nameTextView.setText(user.getFirstName());
        }
        if (user.getLastName() != null) {
            lastNameTextView.setText(user.getLastName());
        }
        descriptionTextView.setText(user.getDescription());
        locationTextView.setText(user.getLocation());

        String followingCount = String.valueOf(user.getFollowingsNumber());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersNumber());
        followersCountTextView.setText(followersCount);
    }


}