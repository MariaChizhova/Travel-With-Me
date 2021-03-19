package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelwithme.adapter.PostAdapter;
import com.squareup.picasso.Picasso;
import com.example.travelwithme.pojo.Post;
import com.example.travelwithme.pojo.User;


import java.util.Arrays;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImageView = view.findViewById(R.id.user_image_view);
        nameTextView = view.findViewById(R.id.user_name_text_view);
        nickTextView = view.findViewById(R.id.user_nick_text_view);
        descriptionTextView = view.findViewById(R.id.user_description_text_view);
        locationTextView = view.findViewById(R.id.user_location_text_view);
        followingCountTextView = view.findViewById(R.id.following_count_text_view);
        followersCountTextView = view.findViewById(R.id.followers_count_text_view);
        initRecyclerView();
        loadUserInfo();
        //loadPosts();


        final Button plus = view.findViewById(R.id.b_plus);
        plus.setOnClickListener(v -> {
            startActivity((new Intent(view.getContext(), MapActivity.class)));
        });

        return view;
    }


    public View getLocalView() {
        return view;
    }


    //    private void loadPosts() {
//        Collection<Post> posts = getPosts();
//        postAdapter.setItems(posts);
//    }

//    private Collection<Post> getPosts() {
//        return Arrays.asList(
//                new Post(getUser(), 1L, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
//                        10L, 15L, "https://www.w3schools.com/w3css/img_fjords.jpg"),
//                new Post(getUser(), 2L, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
//                        10L, 15L, "https://www.w3schools.com/w3images/lights.jpg"),
//                new Post(getUser(), 3L, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
//                        16L, 16L, "https://www.w3schools.com/css/img_mountains.jpg"),
//                new Post(getUser(), 4L, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
//                        26L, 63L, "https://www.w3schools.com/w3css/img_corniglia.jpg"),
//                new Post(getUser(), 5L, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
//                        25L, 55L, "https://www.w3schools.com/w3css/img_riomaggiore.jpg"),
//                new Post(getUser(), 6L, "Thu Apr 1 07:31:08 +0000 2017", "Описание поста",
//                        63L, 56L, "https://www.w3schools.com/w3css/img_manarola.jpg"),
//                new Post(getUser(), 7L, "Thu Apr 1 07:31:08 +0000 2017", "Описание поста",
//                        612L, 623L, "https://www.w3schools.com/css/img_mountains.jpg"),
//                new Post(getUser(), 8L, "Thu Apr 1 07:31:08 +0000 2017", "Описание поста",
//                        65L, 64L, "https://www.w3schools.com/w3css/img_5terre.jpg"),
//                new Post(getUser(), 9L, "Thu Apr 1 07:31:08 +0000 2017", "Описание поста",
//                        66L, 63L, "https://www.w3schools.com/w3images/streetart2.jpg"),
//                new Post(getUser(), 10L, "Thu Apr 1 07:31:08 +0000 2017", "Описание поста",
//                        64L, 63L, "https://www.w3schools.com/w3css/img_forest.jpg")
//        );
//    }



    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        //   postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this);
        postsRecyclerView.setAdapter(postAdapter);
    }

    private void loadUserInfo() {
        User user = getUser();
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

    public static User getUser() {
        return new User(
                1L,
                "https://www.w3schools.com/w3images/streetart2.jpg",
                "User name",
                "User nick",
                "Description",
                "Location",
                142,
                142
        );
    }
}