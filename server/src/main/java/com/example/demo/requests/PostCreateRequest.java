package com.example.demo.requests;

public class PostCreateRequest {

    private Long authorId;
    private String date;
    private String description;
    private String picture;

    private Iterable<MarkerCreateRequest> markers;

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

    public Iterable<MarkerCreateRequest> getMarkers() {
        return markers;
    }
}
