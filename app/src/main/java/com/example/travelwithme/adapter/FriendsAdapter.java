package com.example.travelwithme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<User> friendsList = new ArrayList<>();

    @Override
    public @NotNull FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_view, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FriendsAdapter.FriendViewHolder) holder).bind(friendsList.get(position));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public void setItems(Collection<User> friends) {
        friendsList.addAll(friends);
        notifyDataSetChanged();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ImageView userImageView;
        private final TextView nameTextView;
        private final TextView nickTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            nickTextView = itemView.findViewById(R.id.user_nick_text_view);
            final Button isFollowingButton = itemView.findViewById(R.id.is_following_button);
            isFollowingButton.setOnClickListener(v -> {
                if (isFollowingButton.getText() == "UNFOLLOW") {
                    isFollowingButton.setText("FOLLOW");
                    isFollowingButton.setBackgroundResource(R.drawable.unfollow_shape);
                } else {
                    isFollowingButton.setText("UNFOLLOW");
                    isFollowingButton.setBackgroundResource(R.drawable.follow_shape);
                }
            });
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
            nickTextView.setText(user.getNick());
            Picasso.get().load(user.getImageUrl()).into(userImageView);
        }
    }

}
