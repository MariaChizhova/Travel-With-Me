package com.example.travelwithme;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelwithme.pojo.User;
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

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {

    private FragmentChatsBinding mBinding;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> mFirebaseAdapter;
    private View view;
    private User user;

    private static final String TAG = "MainActivity";
    public static final String MESSAGES_CHILD = "messages";
    public static final String ANONYMOUS = "anonymous";
    private static final int REQUEST_IMAGE = 2;


    public ChatFragment(User user) {
        this.user = user;
    }

    public ChatFragment() {
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
        DatabaseReference messagesRef = mDatabase.getReference().child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(messagesRef, Message.class).build();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {
            @Override
            public @NotNull MessageViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
            }

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
            Message message = new Message(mBinding.messageEditText.getText().toString(), getUserName(), getUserPhotoUrl(), null);
            mDatabase.getReference().child(MESSAGES_CHILD).push().setValue(message);
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
                Message tempMessage = new Message(null, getUserName(), getUserPhotoUrl(), uri.toString());
                mDatabase.getReference().child(MESSAGES_CHILD).push()
                        .setValue(tempMessage, (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                                return;
                            }
                            String key = databaseReference.getKey();
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getUid()).child(key).child(uri.getLastPathSegment());
                            putImageInStorage(storageReference, uri, key);
                        });
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, final String key) {
        storageReference
                .putFile(uri)
                .addOnSuccessListener((Activity) view.getContext(), taskSnapshot -> taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(uri1 -> {
                            Message message = new Message(null, getUserName(), getUserPhotoUrl(), uri1.toString());
                            mDatabase.getReference().child(MESSAGES_CHILD).child(key).setValue(message);
                        }))
                .addOnFailureListener((Activity) view.getContext(), e -> Log.w(TAG, "Image upload task was not successful.", e));
    }

    @Nullable
    private String getUserPhotoUrl() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null && user.getPhotoUrl() != null) {
            return user.getPhotoUrl().toString();
        }
        return null;
    }

    private String getUserName() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return ANONYMOUS;
    }

}