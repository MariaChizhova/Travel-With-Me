package com.example.travelwithme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelwithme.adapter.PostAdapter;
import com.example.travelwithme.adapter.ViewPagerAdapter;
import com.example.travelwithme.pojo.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
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
    private long currentId = 1;
    boolean isLoading = false;
    private long currentLike = 0;
    public static final String USER_ID = "userId";
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
            startActivity(new Intent(view.getContext(), MapActivity.class));
        });

        final Button settings = view.findViewById(R.id.edit_profile);
        settings.setOnClickListener(v -> {
            startActivity(new Intent(view.getContext(), SettingsProfileActivity.class));
        });

        final Button followersButton = view.findViewById(R.id.followers_count_text_view);
        followersButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Followers()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        });

        final Button followingButton = view.findViewById(R.id.following_count_text_view);
        followingButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
            ViewPager viewPager = getActivity().findViewById(R.id.view_pager);
            viewPager.setCurrentItem(1);
        });
        return view;
    }


    public View getLocalView() {
        return view;
    }

  /*  private void loadPosts() {
        Collection<Post> postsList = getPosts();
        postAdapter.setItems(postsList);
    }*/

  /*  private Collection<Post> getPosts() {
        Collection<Post> lst = new ArrayList<>();
        lst.add(new Post(getUser(), currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                10L, ++currentLike, "https://www.w3schools.com/w3css/img_fjords.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                10L, ++currentLike, "https://www.w3schools.com/w3images/lights.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                16L, ++currentLike, "https://www.w3schools.com/css/img_mountains.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                26L, ++currentLike, "https://www.w3schools.com/w3css/img_corniglia.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                25L, ++currentLike, "https://www.w3schools.com/w3css/img_riomaggiore.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                10L, ++currentLike, "https://www.w3schools.com/w3images/lights.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                16L, ++currentLike, "https://www.w3schools.com/css/img_mountains.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                26L, ++currentLike, "https://www.w3schools.com/w3css/img_corniglia.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                25L, ++currentLike, "https://www.w3schools.com/w3css/img_riomaggiore.jpg"));
        lst.add(new Post(getUser(), ++currentId, "Thu Apr 1 07:31:08 +0000 2021", "Описание поста",
                10L, ++currentLike, "https://www.w3schools.com/w3images/lights.jpg"));
        return lst;
    }*/

    private void initRecyclerView() {
        postsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(this);
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