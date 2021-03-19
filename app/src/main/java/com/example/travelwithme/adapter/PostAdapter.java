package com.example.travelwithme.adapter;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.ProfileFragment;
import com.example.travelwithme.ViewingMapActivity;
import com.squareup.picasso.Picasso;

import com.example.travelwithme.R;
import com.example.travelwithme.pojo.Post;

import org.apache.commons.io.IOUtils;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private static final String RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private static final String MONTH_DAY_FORMAT = "MMM d";

    ProfileFragment parent;

    private List<Post> postList = new ArrayList<>();

    public PostAdapter(ProfileFragment profileFragment){
        parent = profileFragment;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(postList.get(position));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setItems(Collection<Post> posts) {
        postList.addAll(posts);
        notifyDataSetChanged();
    }

    public void clearItems() {
        postList.clear();
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView nameTextView;
        private TextView nickTextView;
        private TextView creationDateTextView;
        private TextView contentTextView;
        private ImageView postImageView;
        private TextView repostsTextView;
        private TextView likesTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.profile_image_view);
            nameTextView = itemView.findViewById(R.id.author_name_text_view);
            nickTextView = itemView.findViewById(R.id.author_nick_text_view);
            creationDateTextView = itemView.findViewById(R.id.creation_date_text_view);
            contentTextView = itemView.findViewById(R.id.post_content_text_view);
            postImageView = itemView.findViewById(R.id.post_image_view);
            repostsTextView = itemView.findViewById(R.id.reposts_text_view);
            likesTextView = itemView.findViewById(R.id.likes_text_view);
        }

        public void bind(Post post) {
            nameTextView.setText(post.getUser().getName());
            nickTextView.setText(post.getUser().getNick());
            contentTextView.setText(post.getText());
            repostsTextView.setText(String.valueOf(post.getRepostCount()));
            likesTextView.setText(String.valueOf(post.getFavouriteCount()));

            String creationDateFormatted = getFormattedDate(post.getCreationDate());
            creationDateTextView.setText(creationDateFormatted);

            Picasso.get().load(post.getUser().getImageUrl()).into(userImageView);

//            String postPhotoUrl = post.getImageUrl();
//            Picasso.get().load(postPhotoUrl).into(postImageView);
//
//            postImageView.setVisibility(postPhotoUrl != null ? View.VISIBLE : View.GONE);

            postImageView.setImageBitmap(post.getImage());  // без сервера картинка делается из битов, потом нужно переделать Url

            postImageView.setOnClickListener(v -> {
                Intent i = new Intent(parent.getLocalView().getContext(), ViewingMapActivity.class);
                i.putExtra("mapData", post.getMapData());
                parent.startActivity(i);
            });
        }

        private String getFormattedDate(String rawDate) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(RESPONSE_FORMAT, Locale.ROOT);
            SimpleDateFormat displayedFormat = new SimpleDateFormat(MONTH_DAY_FORMAT, Locale.getDefault());
            try {
                Date date = utcFormat.parse(rawDate);
                return displayedFormat.format(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}