package com.example.demo.models;

import com.example.demo.requests.PostCreateRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long authorId;    // unchanged
    private String date;      // unchanged
    private String description;
    private String picture;

    private int numberLikes = 0;

    public Post() {
    }

    public Post(PostCreateRequest post) {
        this.authorId = post.getAuthorId();
        this.date = post.getDate();
        this.description = post.getDescription();
        this.picture = post.getPicture();
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public Post setDescription(String description) {
        this.description = description;
        return this;
    }

    public Post incNumberLikes() {
        this.numberLikes += 1;
        return this;
    }
}
