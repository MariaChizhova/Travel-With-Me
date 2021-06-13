package com.example.travelwithme.adapter;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travelwithme.R;
import com.example.travelwithme.pojo.User;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> chatsList = new ArrayList<>();
    private OnChatClickListener onChatClickListener;

    public ChatsAdapter(OnChatClickListener onUserClickListener) {
        this.onChatClickListener = onUserClickListener;
    }

    @Override
    public @NotNull ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
        return new ChatViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        private TextView lastNameTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.user_name_text_view);
            lastNameTextView = itemView.findViewById(R.id.user_nick_text_view);

            itemView.setOnClickListener(v -> {
                User user = chatsList.get(getLayoutPosition());
                onChatClickListener.onChatClick(user);
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
