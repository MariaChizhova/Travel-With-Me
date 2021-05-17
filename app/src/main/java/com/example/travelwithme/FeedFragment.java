package com.example.travelwithme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.travelwithme.pojo.Post;
import com.example.travelwithme.requests.PostCreateRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RecyclerView postsRecyclerView;
    public static PostAdapter postAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
                .baseUrl("http://192.168.0.8:9090")
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