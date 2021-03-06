package com.example.travelwithme.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class UsersProfileFragment extends Fragment {

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView lastNameTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private final User user;
    private static final String FOLLOW = "FOLLOW";
    private static final String UNFOLLOW = "UNFOLLOW";

    private View view;
    public PostAdapter postAdapter;
    ProgressDialog progressDialog;

    public UsersProfileFragment(User user) {
        this.user = user;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
            loadUserInfo();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_users, container, false);
        userImageView = view.findViewById(R.id.user_image_view);
        nameTextView = view.findViewById(R.id.user_name_text_view);
        lastNameTextView = view.findViewById(R.id.user_nick_text_view);
        descriptionTextView = view.findViewById(R.id.user_description_text_view);
        locationTextView = view.findViewById(R.id.user_location_text_view);
        followingCountTextView = view.findViewById(R.id.following_count_text_view);
        followersCountTextView = view.findViewById(R.id.followers_count_text_view);
        initRecyclerView();
        //TODO: action on click
        final Button followersButton = view.findViewById(R.id.followers_count_text_view);
        final Button followingButton = view.findViewById(R.id.following_count_text_view);
        final Button isFollowingButton = view.findViewById(R.id.follow);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String email = preferences.getString("user_email", "");

        Api api = new Api();
        api.getUser(email, u -> api.existingSubscribe(user.getUserID(), u.getUserID(), isFollowing -> {
            if (isFollowing) {
                isFollowingButton.setText(UNFOLLOW);
                isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
            } else {
                isFollowingButton.setText(FOLLOW);
                isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
            }
        }));

        isFollowingButton.setOnClickListener(v -> {
            if (isFollowingButton.getText() == UNFOLLOW) {
                api.getUser(email, u -> {
                    new Api().deleteSubscribe(user.getUserID(), u.getUserID());

                    u.decFollowingsNumber();
                    Gson gson = new Gson();
                    String json = gson.toJson(u);
                    preferences.edit().putString("user", json).apply();

                });
                isFollowingButton.setText(FOLLOW);
                isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
            } else {
                api.getUser(email, u -> {
                    new Api().addSubscribe(user.getUserID(), u.getUserID());

                    u.incFollowingsNumber();
                    Gson gson = new Gson();
                    String json = gson.toJson(u);
                    preferences.edit().putString("user", json).apply();

                });
                isFollowingButton.setText(UNFOLLOW);
                isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
            }
        });

        final Button writeMessage = view.findViewById(R.id.write_message);
        writeMessage.setOnClickListener(v -> api.getUser(email, u -> {
            Long id1 = u.getUserID();
            Long id2 = user.getUserID();
            System.out.println(u.getEmail() + " " + id1);
            System.out.println(user.getEmail() + " " + id2);
            if (id1 > id2) {
                Long tmp = id1;
                id1 = id2;
                id2 = tmp;
            }
            api.addChat(id1, id2);
            Fragment newFragment = new ChatFragment(id1.toString(), id2.toString());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }));
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUserInfo() {
        new Api().getUser(user.getEmail(), user -> {
            loadPosts(user.getUserID());
            displayUserInfo(user);
        });
    }

    private void loadPosts(long userId) {
        new Api().getPosts(userId, 0, 10, posts -> {
            postAdapter.setItems(posts);
            progressDialog.dismiss();
            view.setVisibility(View.VISIBLE);
        });
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

    private void initRecyclerView() {
        RecyclerView postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String email = preferences.getString("user_email", "");
        postAdapter = new PostAdapter(this, view, false, email);
        postsRecyclerView.setAdapter(postAdapter);
    }
}