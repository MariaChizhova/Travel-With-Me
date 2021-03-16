package com.example.travelwithme.pojo;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Post {
    private User user;
    private Long id;
    private String creationDate;
    private String text;
    private Long repostCount;
    private Long likedCount;
    private Bitmap image;

    public Post(User user, Long id, String creationDate, String text, Long repostCount, Long likedCount, Bitmap image) {
        this.user = user;
        this.id = id;
        this.creationDate = creationDate;
        this.text = text;
        this.repostCount = repostCount;
        this.likedCount = likedCount;
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getText() {
        return text;
    }

    public Long getRepostCount() {
        return repostCount;
    }

    public Long getFavouriteCount() {
        return likedCount;
    }

    public Bitmap getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!user.equals(post.user)) return false;
        if (!id.equals(post.id)) return false;
        if (!creationDate.equals(post.creationDate)) return false;
        if (!text.equals(post.text)) return false;
        if (!repostCount.equals(post.repostCount)) return false;
        if (!likedCount.equals(post.likedCount)) return false;
        return image != null ? image.equals(post.image) : post.image == null;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + repostCount.hashCode();
        result = 31 * result + likedCount.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}