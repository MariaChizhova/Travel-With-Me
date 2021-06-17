package com.example.travelwithme.fragments;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.AddChatFragment;
import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.SwipeController;
import com.example.travelwithme.SwipeControllerActions;
import com.example.travelwithme.adapter.ChatsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class MessagesFragment extends Fragment {

    public MessagesFragment() {
    }

    private ChatsAdapter chatsAdapter;
    private RecyclerView chatsRecyclerView;
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
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Fragment newFragment = new AddChatFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar_chats);
        Button searchButton = toolbar.findViewById(R.id.search_button_chats);
        EditText searchEditText = toolbar.findViewById(R.id.search_edit_text_chats);
        searchButton.setOnClickListener(v -> {
            chatsRecyclerView.setVisibility(View.GONE);
            String inputText = searchEditText.getText().toString();
            System.out.println(inputText);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ChatsSearchFragment usersSearchFragment = new ChatsSearchFragment(inputText);
            fragmentTransaction.replace(R.id.relative_layout_messages, usersSearchFragment);
            fragmentTransaction.commit();
        });
        return view;
    }

    private void loadChats() {
        new Api().getUser(email, user -> new Api().getChats(user.getUserID(), chats -> chatsAdapter.setItems(chats)));
    }

    private void initRecyclerView() {
        chatsRecyclerView = view.findViewById(R.id.chats_recycler_view);
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
                new Api().getUser(email, user -> {
                    Long id1 = user.getUserID();
                    Long id2 = chatsAdapter.getItem(position).getUserID();
                    if (id1 > id2) {
                        Long tmp = id1;
                        id1 = id2;
                        id2 = tmp;
                    }
                    System.out.println(id1 + " " + id2);
                    new Api().deleteChat(id1, id2);
                    FirebaseDatabase.getInstance().getReference("dialogs").child(id1 + "_" + id2).removeValue();
                    chatsAdapter.deleteItem(position);
                    chatsAdapter.notifyItemRemoved(position);
                    chatsAdapter.notifyItemRangeChanged(position, chatsAdapter.getItemCount());
                });
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

        chatsAdapter = new ChatsAdapter(onChatClickListener);
        chatsRecyclerView.setAdapter(chatsAdapter);
    }

}