package com.example.travelwithme.adapter;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.Collection;
import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<User> usersList;
    private final OnUsersClickListener onUsersClickListener;
    private final Fragment parent;
    private static final String FOLLOW = "FOLLOW";
    private static final String UNFOLLOW = "UNFOLLOW";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public UsersAdapter(List<User> itemList, OnUsersClickListener onUserClickListener, Fragment parent) {
        this.onUsersClickListener = onUserClickListener;
        this.parent = parent;
        usersList = itemList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public  RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_view, parent, false);
            return new FriendViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UsersAdapter.FriendViewHolder) {
            populateItemRows((UsersAdapter.FriendViewHolder) holder, position);
        } else if (holder instanceof UsersAdapter.LoadingViewHolder) {
            showLoadingView((UsersAdapter.LoadingViewHolder) holder, position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void populateItemRows(FriendViewHolder holder, int position) {
        ((UsersAdapter.FriendViewHolder) holder).bind(usersList.get(position));
    }


    private void showLoadingView(UsersAdapter.LoadingViewHolder viewHolder, int position) {
    }


    @Override
    public int getItemCount() {
        return usersList == null ? 0 : usersList.size();
    }

    public void setItems(Collection<User> users) {
        usersList.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return usersList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ImageView userImageView;
        private final TextView nameTextView;
        private final TextView lastNameTextView;
        private long userId;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public FriendViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            lastNameTextView = itemView.findViewById(R.id.user_nick_text_view);
            itemView.setOnClickListener(v -> {
                User user = usersList.get(getLayoutPosition());
                onUsersClickListener.onUserClick(user);
            });

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parent.getActivity());
            final String email = preferences.getString("user_email", "");

            final Button isFollowingButton = itemView.findViewById(R.id.is_following_button);

            Api api = new Api();
            api.getUser(email, user -> api.existingSubscribe(userId, user.getUserID(), isFollowing -> {
                if (isFollowing) {
                    isFollowingButton.setText(UNFOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                } else {
                    isFollowingButton.setText(FOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                }
            }));

            isFollowingButton.setOnClickListener(v -> {
                if (isFollowingButton.getText() == UNFOLLOW) {
                    api.getUser(email, user -> {
                        new Api().deleteSubscribe(userId, user.getUserID());

                        user.decFollowingsNumber();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        preferences.edit().putString("user", json).apply();

                    });
                    isFollowingButton.setText(FOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                } else {
                    api.getUser(email, user -> {
                        new Api().addSubscribe(userId, user.getUserID());

                        user.incFollowingsNumber();
                        Gson gson = new Gson();
                        String json = gson.toJson(user);
                        preferences.edit().putString("user", json).apply();

                    });
                    isFollowingButton.setText(UNFOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(User user) {
            if (user.getFirstName() != null) {
               nameTextView.setText(user.getFirstName());
            }
            if (user.getLastName() != null) {
                lastNameTextView.setText(user.getLastName());
            }
            if (user.getAvatar() != null) {
                byte[] image = Base64.getDecoder().decode(user.getAvatar());
                userImageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }
            userId = user.getUserID();
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    public interface OnUsersClickListener {
        void onUserClick(User user);
    }

    public void clearItems() {
        usersList.clear();
        notifyDataSetChanged();
    }
}
