package com.example.travelwithme.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelwithme.Api;
import com.example.travelwithme.MessageViewHolder;
import com.example.travelwithme.MyButtonObserver;
import com.example.travelwithme.MyScrollToBottomObserver;
import com.example.travelwithme.R;
import com.example.travelwithme.SignUpActivity;
import com.example.travelwithme.pojo.Message;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.travelwithme.databinding.FragmentChatsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private FragmentChatsBinding mBinding;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private View view;
    private final String userId1;
    private final String userId2;

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 2;
    private final String DIALOGS_CHILD = "dialogs";

    public ChatFragment(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        System.out.println("ID1 " + userId1);
        System.out.println("ID2 " + userId2);

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(view.getContext(), SignUpActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentChatsBinding.inflate(inflater, container, false);
        view = mBinding.getRoot();
        mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = mDatabase.getReference().child(DIALOGS_CHILD).child(userId1 + "_" + userId2);
        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public @NotNull MessageViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                System.err.println(111);
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NotNull MessageViewHolder viewHolder, int position, @NotNull Message message) {
                mBinding.progressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.bindMessage(message);
            }
        };

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mBinding.messageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.messageRecyclerView.setAdapter(mFirebaseAdapter);
        mFirebaseAdapter.registerAdapterDataObserver(new MyScrollToBottomObserver(mBinding.messageRecyclerView, mFirebaseAdapter, mLinearLayoutManager));
        mBinding.messageEditText.addTextChangedListener(new MyButtonObserver(mBinding.sendButton));

        mBinding.sendButton.setOnClickListener(view -> {
            onMessage(mBinding.messageEditText.getText().toString(), null, message -> mDatabase.getReference().child(DIALOGS_CHILD).child(userId1 + "_" + userId2).push().setValue(message));
            mBinding.messageEditText.setText("");
        });
        mBinding.addMessageImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);
        });
        return view;
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());
                final FirebaseUser user = mFirebaseAuth.getCurrentUser();
                onMessage(null, uri.toString(), tempMessage -> mDatabase.getReference().child(DIALOGS_CHILD).child(userId1 + "_" + userId2).push()
                    .setValue(tempMessage, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                            return;
                        }
                        String key = databaseReference.getKey();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getUid()).child(key).child(uri.getLastPathSegment());
                        putImageInStorage(storageReference, uri, key);
                    }));
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference
                .putFile(uri)
                .addOnSuccessListener((Activity) view.getContext(), taskSnapshot -> taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(uri1 -> onMessage(null, uri1.toString(), message -> mDatabase.getReference().child(DIALOGS_CHILD).child(userId1 + "_" + userId2).child(key).setValue(message))))
                .addOnFailureListener((Activity) view.getContext(), e -> Log.w(TAG, "Image upload task was not successful.", e));
    }

    private void onMessage(String messageText, String imageUrl, Consumer<Message> onReceived) {
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null) {
            new Api().getUser(currentUser.getEmail(), user -> {
                String firstName = user.getFirstName();
                String lastName = user.getLastName();
                String name = firstName + " " + lastName;
                System.out.println(name);
                String photo = user.getAvatar();

                Message message = new Message(messageText, name, photo, imageUrl);
                onReceived.accept(message);

            });
        } else {
            Message message = new Message(messageText, null, null, imageUrl);
            onReceived.accept(message);
        }
    }
}