package com.example.travelwithme.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> chatsList = new ArrayList<>();
    private OnChatClickListener onChatClickListener;
    private final Fragment parent;

    public ChatsAdapter(OnChatClickListener onUserClickListener, Fragment parent) {
        this.onChatClickListener = onUserClickListener;
        this.parent = parent;
    }

    @Override
    public @NotNull ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatsAdapter.ChatViewHolder) holder).bind(chatsList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public void setItems(Collection<User> chats) {
        chatsList.addAll(chats);
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            nickTextView = itemView.findViewById(R.id.user_nick_text_view);

            itemView.setOnClickListener(v -> {
                User user = chatsList.get(getLayoutPosition());
                onChatClickListener.onChatClick(user);
            });
        }

        public void bind(User user) {
            nameTextView.setText(user.getFirstName());
            nickTextView.setText(user.getLastName());
            Picasso.get().load(user.getAvatar()).into(userImageView);
        }
    }

    public interface OnChatClickListener {
        void onChatClick(User user);
    }

    public void clearItems() {
        chatsList.clear();
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        chatsList.remove(position);
        notifyDataSetChanged();
    }
}
