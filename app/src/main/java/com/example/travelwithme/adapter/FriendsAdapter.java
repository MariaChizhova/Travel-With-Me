package com.example.travelwithme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> friendsList = new ArrayList<>();

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    public void clearItems() {
        friendsList.clear();
        notifyDataSetChanged();
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            nickTextView = itemView.findViewById(R.id.user_nick_text_view);
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
            nickTextView.setText(user.getNick());
            Picasso.get().load(user.getImageUrl()).into(userImageView);
        }
    }

    public interface OnFriendsClickListener {
        void onUserClick(User user);
    }
}
