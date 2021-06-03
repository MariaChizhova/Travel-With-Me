package com.example.travelwithme;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.adapter.ChatsAdapter;
import com.example.travelwithme.pojo.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class MessagesFragment extends Fragment {

    public MessagesFragment() {
    }

    private ChatsAdapter chatsAdapter;
    private View view;
    Collection<User> chatsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        initRecyclerView();
        loadChats();
        return view;
    }

    private void loadChats() {
        chatsList = getChats();
        chatsAdapter.setItems(chatsList);
    }

    private Collection<User> getChats() {
        Collection<User> lst = new ArrayList<>();
        lst.add(new User(1L,
                "https://www.w3schools.com/w3images/streetart2.jpg",
                "Maria",
                "@maria",
                "maria@gmail.com",
                100,
                100));
        lst.add(new User(2L,
                "https://www.w3schools.com/w3images/streetart.jpg",
                "Anna",
                "@anna",
                "anna@gmail.com",
                110,
                110));
        lst.add(new User(3L,
                "https://www.w3schools.com/w3images/streetart3.jpg",
                "Katya",
                "@katya",
                "akatya@gmail.com",
                10,
                10));
        return lst;
    }

    private void initRecyclerView() {
        RecyclerView chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ChatsAdapter.OnChatClickListener onChatClickListener = user -> {
            Fragment newFragment = new ChatFragment(user);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        };

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