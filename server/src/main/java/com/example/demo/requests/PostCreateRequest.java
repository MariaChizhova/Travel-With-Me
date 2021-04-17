package com.example.demo.requests;


import com.example.demo.models.Post;

public class PostCreateRequest {

    private String author;    // email of author
    private String date;
    private String description;
    private String picture;

    public PostCreateRequest(String author, String date,
                             String description, String picture) {
        this.author = author;
        this.date = date;
        this.description = description;
        this.picture = picture;
    }

    public PostCreateRequest(Post post) {
        this.author = post.getAuthor();
        this.date = post.getDate();
        this.description = post.getDescription();
        this.picture = post.getPicture();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
