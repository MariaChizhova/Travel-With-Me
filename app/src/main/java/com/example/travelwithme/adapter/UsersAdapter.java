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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> usersList = new ArrayList<>();
    private OnUsersClickListener onUsersClickListener;

    public UsersAdapter(OnUsersClickListener onUserClickListener) {
        this.onUsersClickListener = onUserClickListener;
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

        public FriendViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            nickTextView = itemView.findViewById(R.id.user_nick_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = usersList.get(getLayoutPosition());
                    onUsersClickListener.onUserClick(user);
                }
            });
        }

        public void bind(User user) {
            nameTextView.setText(user.getName());
            nickTextView.setText(user.getNick());
            Picasso.get().load(user.getImageUrl()).into(userImageView);
        }
    }

    public interface OnUsersClickListener {
        void onUserClick(User user);
    }
}
