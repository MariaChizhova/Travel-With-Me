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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.ProfileFragment;
import com.example.travelwithme.ViewingMapActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import com.example.travelwithme.R;
import com.example.travelwithme.pojo.Post;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
    private static final String MONTH_DAY_FORMAT = "MMM d";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public List<Post> postsList = new ArrayList<>();

    ProfileFragment parent;

    private List<Post> postList = new ArrayList<>();

    public PostAdapter(ProfileFragment profileFragment){
        parent = profileFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_view, parent, false);
            return new PostViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            populateItemRows((PostViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) { }

    private void populateItemRows(PostViewHolder holder, int position) {
        ((PostViewHolder) holder).bind(postsList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return postsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return postsList.size();
    }



    public void setItems(Collection<Post> posts) {
        postsList.addAll(posts);
        notifyDataSetChanged();
    }

    public void clearItems() {
        postsList.clear();
        notifyDataSetChanged();
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
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
        private LikeButton heartButton;

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
            heartButton = itemView.findViewById(R.id.heart_button);
        }


        public void bind(Post post) {
            nameTextView.setText(post.getUser().getName());
            nickTextView.setText(post.getUser().getNick());
            contentTextView.setText(post.getText());
            repostsTextView.setText(String.valueOf(post.getRepostCount()));
            likesTextView.setText(String.valueOf(post.getFavouriteCount()));
            heartButton.setLiked(false);
            heartButton.setOnLikeListener(new OnLikeListener() {
                public void liked(LikeButton heartButton) {
                    long count = Long.parseLong(likesTextView.getText().toString());
                    String text = Long.toString(count + 1);
                    likesTextView.setText(text);
                }

                public void unLiked(LikeButton heartButton) {
                    long count = Long.parseLong(likesTextView.getText().toString());
                    String text = Long.toString(count - 1);
                    likesTextView.setText(text);
                }
            });


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