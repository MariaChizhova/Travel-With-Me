package com.example.travelwithme;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.ChatsAdapter;
import com.example.travelwithme.fragments.ChatFragment;
import com.example.travelwithme.fragments.UsersChatSearchFragment;
import com.example.travelwithme.fragments.UsersSearchFragment;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;

import java.util.List;


public class AddChatFragment extends Fragment {

    private ChatsAdapter usersAdapter;
    private static String email;
    private User currentUser;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_chat_fragment, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        email = preferences.getString("user_email", "");
        Gson gson = new Gson();
        String json = preferences.getString("user", "");
        currentUser = gson.fromJson(json, User.class);
        initRecyclerView();

        long offset = 0;
        long count = 30;
        if (currentUser != null) {
            loadUsers(currentUser.getUserID(), offset, count);
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar_chats);
        Button searchButton = toolbar.findViewById(R.id.search_button_chats);
        EditText searchEditText = toolbar.findViewById(R.id.search_edit_text_chats);
        searchButton.setOnClickListener(v -> {
            String inputText = searchEditText.getText().toString();
            System.out.println(inputText);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UsersChatSearchFragment usersSearchFragment = new UsersChatSearchFragment(inputText);
            fragmentTransaction.replace(R.id.relative_layout_chats, usersSearchFragment);
            fragmentTransaction.commit();
        });
        return view;
    }

    private void loadUsers(long userId, long offset, long count) {
        new Api().getFollowing(userId, offset, count, followersList -> usersAdapter.setItems(followersList));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.addChatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ChatsAdapter.OnChatClickListener onChatClickListener = user -> new Api().getUser(email, curUser -> {
            Long id1 = user.getUserID();
            Long id2 = curUser.getUserID();
            System.out.println("id1 " + id1 + "id2 " + id2);
            if (id1 > id2) {
                Long tmp = id1;
                id1 = id2;
                id2 = tmp;
            }
            new Api().addChat(id1, id2);
            Fragment newFragment = new ChatFragment(id1.toString(), id2.toString());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        usersAdapter = new ChatsAdapter(onChatClickListener);
        recyclerView.setAdapter(usersAdapter);
    }
}