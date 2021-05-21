package com.example.travelwithme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.UsersAdapter;
import com.example.travelwithme.pojo.User;

import java.util.ArrayList;
import java.util.Collection;

public class UsersSearchFragment extends Fragment {
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private View view;
    private String inputText;

    public UsersSearchFragment(String inputText) {
        this.inputText = inputText;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_users, container, false);
        initRecyclerView();
        searchUsers();
        return view;
    }

    private void searchUsers() {
        Collection<User> allUsers = getUsers();
        Collection<User> users = new ArrayList<>();
        for (User user: allUsers) {
            if (user.getFirstName().equals(inputText)){
                users.add(user);
            }
        }
        usersAdapter.clearItems();
        usersAdapter.setItems(users);
    }

    private Collection<User> getUsers() {
        Collection<User> allUsers = new ArrayList<>();
//        allUsers.add(new User(
//                1L,
//                "https://www.w3schools.com/w3images/streetart2.jpg",
//                "Andrew",
//                "@andrew",
//                "No description",
//                "Russia",
//                100,
//                100,
//                0
//        ));
//        allUsers.add(new User(
//                2L,
//                "https://sun9-6.userapi.com/impf/c845217/v845217483/8560e/LRcTo6l9VBE.jpg?size=1864x2048&quality=96&sign=b2f39df96c4d2fe3e6f16f6df6f528c5&type=album",
//                "Anna",
//                "@anchouls",
//                "I love teorver",
//                "Russia",
//                100,
//                100,
//                0
//        ));
//        allUsers.add(new User(
//                1L,
//                "https://www.w3schools.com/w3images/streetart2.jpg",
//                "Andrew",
//                "@andrew2",
//                "No description",
//                "Russia",
//                100,
//                100,
//                0
//        ));
//        allUsers.add(new User(
//                2L,
//                "https://sun9-6.userapi.com/impf/c845217/v845217483/8560e/LRcTo6l9VBE.jpg?size=1864x2048&quality=96&sign=b2f39df96c4d2fe3e6f16f6df6f528c5&type=album",
//                "Masha",
//                "@masha",
//                "I love teorver",
//                "Russia",
//                100,
//                100,
//                0
//        ));
//        allUsers.add(new User(
//                1L,
//                "https://www.w3schools.com/w3images/streetart2.jpg",
//                "Dasha",
//                "@dasha",
//                "No description",
//                "Russia",
//                100,
//                100,
//                0
//        ));
//        allUsers.add(new User(
//                2L,
//                "https://sun9-6.userapi.com/impf/c845217/v845217483/8560e/LRcTo6l9VBE.jpg?size=1864x2048&quality=96&sign=b2f39df96c4d2fe3e6f16f6df6f528c5&type=album",
//                "Katya",
//                "@katya",
//                "I love teorver",
//                "Russia",
//                100,
//                100,
//                0
//        ));
        return allUsers;
    }

    private void initRecyclerView() {
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        UsersAdapter.OnUsersClickListener onUsersClickListener = user -> {
            Fragment newFragment = new UsersProfileFragment(user);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };
        usersAdapter = new UsersAdapter(onUsersClickListener);
        usersRecyclerView.setAdapter(usersAdapter);
    }
}