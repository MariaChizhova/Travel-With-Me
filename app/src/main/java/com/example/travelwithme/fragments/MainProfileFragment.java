package com.example.travelwithme.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.travelwithme.Api;
import com.example.travelwithme.Followers;
import com.example.travelwithme.Following;
import com.example.travelwithme.MapActivity;
import com.example.travelwithme.R;
import com.example.travelwithme.SettingsProfileActivity;
import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.api.EditAvatarApi;
import com.example.travelwithme.requests.AvatarEditRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.travelwithme.pojo.User;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog progressDialog;


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
            Log.i("Click", "avatar");
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
        return view;
    }

    private void loadPosts(long userId) {
        new Api().getPosts(userId, posts -> {
            postAdapter.setItems(posts);
            progressDialog.dismiss();
            view.setVisibility(View.VISIBLE);
        });
    }

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this, view, true);
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUserInfo() {
        new Api().getUser(email, user -> {
            loadPosts(user.getUserID());
            displayUserInfo(user);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_EDIT_USER) {
            new Api().getUser(email, this::displayUserInfo);
        }

        if (requestCode == RESULT_LOAD_IMAGE && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                Bitmap chosenImage = BitmapFactory.decodeStream(inputStream);
                chosenImage = Bitmap.createScaledBitmap(chosenImage, 400, 400, false);
                userImageView.setImageBitmap(chosenImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                chosenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();

                final Api api = new Api();
                api.getUser(email, user -> {

                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://84.252.137.106:9090")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    EditAvatarApi editAvatarApi = retrofit.create(EditAvatarApi.class);
                    AvatarEditRequest avatarEditRequest = new AvatarEditRequest();
                    avatarEditRequest.setUserId(user.getUserID());
                    avatarEditRequest.setAvatar(Base64.getEncoder().encodeToString(bytes));
                    Call<Void> call = editAvatarApi.editAvatar(avatarEditRequest);
                    call.enqueue(new Callback<Void>() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.i("succsess", "sucses to edit avavtar");
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
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayUserInfo(User user) {
        if (user.getAvatar() != null) {
            byte[] image = Base64.getDecoder().decode(user.getAvatar());
            Log.i("avatar", user.getAvatar());
            userImageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }
        if (user.getFirstName() != null) {
            nameTextView.setText(user.getFirstName());
        }
        if (user.getLastName() != null) {
            lastNameTextView.setText(user.getLastName());
        }
//        descriptionTextView.setText(user.getDescription());
//        locationTextView.setText(user.getLocation());

        String followingCount = String.valueOf(user.getFollowingsNumber());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersNumber());
        followersCountTextView.setText(followersCount);
    }


}