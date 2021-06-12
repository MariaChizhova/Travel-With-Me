package com.example.travelwithme.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelwithme.Api;
import com.example.travelwithme.fragments.UsersProfileFragment;
import com.example.travelwithme.map.ViewingMapActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;

import com.example.travelwithme.R;
import com.example.travelwithme.pojo.Post;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String RESPONSE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    private static final String MONTH_DAY_FORMAT = "MMM d";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;


    public List<Post> postsList = new ArrayList<>();


    private final Fragment parent;
    private final View parentView;
    private final boolean fromProfile;

    public PostAdapter(Fragment parent, View view, boolean fromProfile) {
        this.parent = parent;
        parentView = view;
        this.fromProfile = fromProfile;
    }

    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_view, parent, false);
            return new PostViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PostViewHolder) {
            populateItemRows((PostViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        postsList.addAll(0, posts);
        notifyDataSetChanged();
    }

    public void delItem(Post post) {
        postsList.remove(post);
        notifyDataSetChanged();
    }

    public void del() {
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
        private final ImageView userImageView;
        private final TextView nameTextView;
        private final TextView nickTextView;
        private final TextView creationDateTextView;
        private final TextView contentTextView;
        private final ImageView postImageView;
        private final TextView repostsTextView;
        private final TextView likesTextView;
        private final LikeButton heartButton;
        private final ImageButton delete;

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
            delete = itemView.findViewById(R.id.cross);
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Post post) {
            Api api = new Api();
            api.getUserByID(post.getUser(), user -> {
                nameTextView.setText(user.getFirstName());
                nickTextView.setText(user.getLastName());
                if (user.getAvatar() != null) {
                    byte[] image = Base64.getDecoder().decode(user.getAvatar());
                    userImageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                }

                api.likeExists(post.getId(), user.getUserID(), heartButton::setLiked);

                heartButton.setOnLikeListener(new OnLikeListener() {
                    public void liked(LikeButton heartButton) {
                        long count = Long.parseLong(likesTextView.getText().toString());
                        String text = Long.toString(count + 1);
                        likesTextView.setText(text);

                        api.incNumberLikes(post.getId(), user.getUserID());
                    }

                    public void unLiked(LikeButton heartButton) {
                        long count = Long.parseLong(likesTextView.getText().toString());
                        String text = Long.toString(count - 1);
                        likesTextView.setText(text);

                        api.decNumberLikes(post.getId(), user.getUserID());
                    }
                });
            });


            contentTextView.setText(post.getText());
            //repostsTextView.setText(String.valueOf(post.getRepostCount()));
            likesTextView.setText(String.valueOf(post.getFavouriteCount()));

            if (fromProfile) {
                delete.setVisibility(View.VISIBLE);
            }

            delete.setOnClickListener(v -> {
                if (fromProfile) {
                    new Api().deletePost(post.getId());
                    delItem(post);
                }
            });


            String creationDateFormatted = getFormattedDate(post.getCreationDate());
            creationDateTextView.setText(creationDateFormatted);

            postImageView.setImageBitmap(post.getImage());

            postImageView.setOnClickListener(v -> {
                Intent i = new Intent(parentView.getContext(), ViewingMapActivity.class);
                i.putExtra("mapData", post.getMapData());
                parent.startActivity(i);
            });


            CircleImageView circleImageView = (CircleImageView) itemView.findViewById(R.id.profile_image_view);
            circleImageView.setOnClickListener(v -> new Api().getUserByID(post.getUser(), user -> {
                Fragment newFragment = new UsersProfileFragment(user);
                FragmentTransaction transaction = parent.getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }));
        }

        private String getFormattedDate(String rawDate) {
            SimpleDateFormat utcFormat = new SimpleDateFormat(RESPONSE_FORMAT, Locale.ENGLISH);
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