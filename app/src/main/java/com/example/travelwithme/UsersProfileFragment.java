package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelwithme.adapter.PostAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.example.travelwithme.pojo.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class UsersProfileFragment extends Fragment {

    private ImageView userImageView;
    private TextView nameTextView;
    private TextView nickTextView;
    private TextView descriptionTextView;
    private TextView locationTextView;
    private TextView followingCountTextView;
    private TextView followersCountTextView;
    private View view;
    private User user;


    public UsersProfileFragment(User user) {
       this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_users, container, false);
        userImageView = view.findViewById(R.id.user_image_view);
        nameTextView = view.findViewById(R.id.user_name_text_view);
        nickTextView = view.findViewById(R.id.user_nick_text_view);
        descriptionTextView = view.findViewById(R.id.user_description_text_view);
        locationTextView = view.findViewById(R.id.user_location_text_view);
        followingCountTextView = view.findViewById(R.id.following_count_text_view);
        followersCountTextView = view.findViewById(R.id.followers_count_text_view);

        loadUserInfo();
        //TODO: action on click
        final Button followersButton = view.findViewById(R.id.followers_count_text_view);
        final Button followingButton = view.findViewById(R.id.following_count_text_view);
        final Button isFollowingButton = view.findViewById(R.id.follow);
        isFollowingButton.setOnClickListener(v -> {
            if (isFollowingButton.getText() == "UNFOLLOW") {
                isFollowingButton.setText("FOLLOW");
                isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
            } else {
                isFollowingButton.setText("UNFOLLOW");
                isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
            }
        });
        return view;
    }

    private void loadUserInfo() {
        displayUserInfo(user);
    }

    private void displayUserInfo(User user) {
        // Picasso.with(this).load(user.getImageUrl()).into(userImageView);
        Picasso.get().load(user.getImageUrl()).into(userImageView);
        nameTextView.setText(user.getName());
        nickTextView.setText(user.getNick());
        descriptionTextView.setText(user.getDescription());
        locationTextView.setText(user.getLocation());

        String followingCount = String.valueOf(user.getFollowingCount());
        followingCountTextView.setText(followingCount);

        String followersCount = String.valueOf(user.getFollowersCount());
        followersCountTextView.setText(followersCount);
    }
}