package com.example.travelwithme.pojo;

import android.graphics.Bitmap;

import com.example.travelwithme.map.MapData;

public class Post {
    private Long user;
    private Long id;
    private String creationDate;
    private String text;
    //private Long repostCount;
    private Long likedCount;
    private Bitmap image;
    private MapData mapData;
    private String imageURL;

    public Post(Long user, Long id, String creationDate, String text, Long likedCount, Bitmap image, MapData mapData) {
        this.user = user;
        this.id = id;
        this.creationDate = creationDate;
        this.text = text;
        //this.repostCount = repostCount;
        this.likedCount = likedCount;
        this.image = image;
        this.mapData = mapData;
    }

    public Post(Long user, Long id, String creationDate, String text, Long likedCount, String imageURL, MapData mapData) {
        this.user = user;
        this.id = id;
        this.creationDate = creationDate;
        this.text = text;
        this.likedCount = likedCount;
        this.imageURL = imageURL;
        this.mapData = mapData;
    }

    public Long getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long newId) {
        id = newId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getText() {
        return text;
    }

//    public Long getRepostCount() {
//        return repostCount;
//    }

    public Long getFavouriteCount() {
        return likedCount;
    }

    public Bitmap getImage() {
        return image;
    }

    public MapData getMapData() {
        return mapData;
    }

    public String getImageURL(){
        return imageURL;
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
        //if (!repostCount.equals(post.repostCount)) return false;
        if (!likedCount.equals(post.likedCount)) return false;
        return image != null ? image.equals(post.image) : post.image == null;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + text.hashCode();
        // result = 31 * result + repostCount.hashCode();
        result = 31 * result + likedCount.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}

