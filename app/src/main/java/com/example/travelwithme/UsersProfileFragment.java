package com.example.travelwithme;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private final User user;
    private static final String FOLLOW = "FOLLOW";
    private static final String UNFOLLOW = "UNFOLLOW";


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
        View view = inflater.inflate(R.layout.fragment_profile_users, container, false);
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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String email = preferences.getString("user_email", "");

        Api api = new Api();
        api.getUser(email, u -> {
            api.existingSubscribe(user.getUserID(), u.getUserID(), isFollowing -> {
                if (isFollowing) {
                    isFollowingButton.setText(UNFOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                } else {
                    isFollowingButton.setText(FOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                }
            });
        });

        isFollowingButton.setOnClickListener(v -> {
            if (isFollowingButton.getText() == UNFOLLOW) {
                api.getUser(email, u -> {
                    new Api().deleteSubscribe(user.getUserID(), u.getUserID());
                });
                isFollowingButton.setText(FOLLOW);
                isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
            } else {
                api.getUser(email, user -> {
                    new Api().addSubscribe(user.getUserID(), user.getUserID());
                });
                isFollowingButton.setText(UNFOLLOW);
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
        Picasso.get().load(user.getAvatar()).into(userImageView);
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