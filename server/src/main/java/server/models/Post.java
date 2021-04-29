package server.models;

import server.requests.PostCreateRequest;

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
    private String pictureName;    // name in the amazon s3 database

    private int numberLikes = 0;

    public Post() {}

    public Post(PostCreateRequest post, String pictureName) {
        this.authorId = post.getAuthorId();
        this.date = post.getDate();
        this.description = post.getDescription();
        this.pictureName = pictureName;
    }

    public Long getId() {
        return id;
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

    public String getPictureName() {
        return pictureName;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public Post setDescription(String description) {
        this.description = description;
        return this;
    }

    public Post setPictureName(String pictureName) {
        this.pictureName = pictureName;
        return this;
    }

    public Post incNumberLikes() {
        this.numberLikes += 1;
        return this;
    }

    public Post decNumberLikes() {
        this.numberLikes -= 1;
        return this;
    }
}
