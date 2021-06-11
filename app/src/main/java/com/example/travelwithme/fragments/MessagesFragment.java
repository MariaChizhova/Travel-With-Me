package com.example.travelwithme.fragments;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.Api;
import com.example.travelwithme.ChatFragment;
import com.example.travelwithme.R;
import com.example.travelwithme.SwipeController;
import com.example.travelwithme.SwipeControllerActions;
import com.example.travelwithme.adapter.ChatsAdapter;
import com.example.travelwithme.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessagesFragment extends Fragment {

    public MessagesFragment() {
    }

    private ChatsAdapter chatsAdapter;
    private View view;
    private static String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        email = preferences.getString("user_email", "");
        initRecyclerView();
        loadChats();
        return view;
    }

    private void loadChats() {
        new Api().getUser(email, user -> {
            new Api().getChats(user.getUserID(), chats -> {
                chatsAdapter.setItems(chats);
            });
        });
    }

    private void initRecyclerView() {
        RecyclerView chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ChatsAdapter.OnChatClickListener onChatClickListener = user -> new Api().getUser(email, currentUser -> {
            Long id1 = user.getUserID();
            Long id2 = currentUser.getUserID();
            if (id1 > id2) {
                Long tmp = id1;
                id1 = id2;
                id2 = tmp;
            }
            Fragment newFragment = new ChatFragment(id1.toString(), id2.toString());
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        SwipeController swipeController;
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                chatsAdapter.deleteItem(position);
                chatsAdapter.notifyItemRemoved(position);
                chatsAdapter.notifyItemRangeChanged(position, chatsAdapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(chatsRecyclerView);
        chatsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, RecyclerView.@NotNull State state) {
                swipeController.onDraw(c);
            }
        });

        chatsAdapter = new ChatsAdapter(onChatClickListener, this);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

}