package com.example.demo.requests;


public class PostCreateRequest {

    private Long authorId;
    private String date;
    private String description;
    private String picture;

    public PostCreateRequest(Long author, String date,
                             String description, String picture) {
        this.authorId = author;
        this.date = date;
        this.description = description;
        this.picture = picture;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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
