package com.example.travelwithme.adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.Api;
import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> usersList = new ArrayList<>();
    private OnUsersClickListener onUsersClickListener;
    private final Fragment parent;
    private static final String FOLLOW = "FOLLOW";
    private static final String UNFOLLOW = "UNFOLLOW";

    public UsersAdapter(OnUsersClickListener onUserClickListener, Fragment parent) {
        this.onUsersClickListener = onUserClickListener;
        this.parent = parent;
    }

    @Override
    public @NotNull FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_view, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UsersAdapter.FriendViewHolder) holder).bind(usersList.get(position));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setItems(Collection<User> users) {
        usersList.addAll(users);
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;
        private long userId;

        public FriendViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            nickTextView = itemView.findViewById(R.id.user_nick_text_view);

            itemView.setOnClickListener(v -> {
                User user = usersList.get(getLayoutPosition());
                onUsersClickListener.onUserClick(user);
            });

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parent.getActivity());
            final String email = preferences.getString("user_email", "");

            final Button isFollowingButton = itemView.findViewById(R.id.is_following_button);

            Api api = new Api();
            api.getUser(email, user -> {
                api.existingSubscribe(user.getUserID(), userId, isFollowing -> {
                    if (isFollowing) {
                        isFollowingButton.setText(UNFOLLOW);
                        isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                    } else {
                        isFollowingButton.setText(FOLLOW);
                        isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                    }
                });
            });

            isFollowingButton.setOnClickListener(v -> {
                if (isFollowingButton.getText() == UNFOLLOW) {
                    api.getUser(email, user -> {
                        new Api().deleteSubscribe(user.getUserID(), userId);
                    });
                    isFollowingButton.setText(FOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                } else {
                    api.getUser(email, user -> {
                        new Api().addSubscribe(user.getUserID(), userId);
                    });
                    isFollowingButton.setText(UNFOLLOW);
                    isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                }
            });
        }

        public void bind(User user) {
            nameTextView.setText(user.getFirstName());
            nickTextView.setText(user.getLastName());
            Picasso.get().load(user.getAvatar()).into(userImageView);
            userId = user.getUserID();
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
