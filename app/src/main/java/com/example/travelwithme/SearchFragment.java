package com.example.travelwithme;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.travelwithme.adapter.FriendsAdapter;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FriendsAdapter friendsAdapter;
    private RecyclerView friendsRecyclerView;
    Collection<User> friendsList;
    private View view;
    private long currentId = 1;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initRecyclerView();
        loadFriends();
        return view;
    }

    private void loadFriends() {
        friendsList = getFriends();
        friendsAdapter.setItems(friendsList);
    }

    private void initRecyclerView() {
        friendsRecyclerView = view.findViewById(R.id.posts_recycler_view);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        friendsAdapter = new FriendsAdapter();
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    private Collection<User> getFriends() {
        Collection<User> lst = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lst.add(new User(
                    currentId,
                    "https://sun9-6.userapi.com/impf/c845217/v845217483/8560e/LRcTo6l9VBE.jpg?size=1864x2048&quality=96&sign=b2f39df96c4d2fe3e6f16f6df6f528c5&type=album",
                    "Anna",
                    "@anchouls",
                    "I love teorver",
                    "Russia",
                    100,
                    100
            ));
            lst.add(new User(
                    currentId,
                    "https://www.w3schools.com/w3images/streetart2.jpg",
                    "Andrew",
                    "@andrew",
                    "No description",
                    "Russia",
                    100,
                    100
            ));
        }
        return lst;
    }
}

