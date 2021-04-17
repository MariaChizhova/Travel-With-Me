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

    private String author;    // email of author
    private String date;
    private String description;
    private String picture;

    public Post() {
    }

    public Post(PostCreateRequest post) {
        this.author = post.getAuthor();
        this.date = post.getDate();
        this.description = post.getDescription();
        this.picture = post.getPicture();
    }

    public String getAuthor() {
        return author;
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
}
