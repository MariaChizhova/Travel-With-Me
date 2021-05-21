package com.example.travelwithme;


public class GetPostResponse {
    private Long postId;
    private Long authorId;
    private String date;
    private String description;
    private String picture;
    private Long numberLikes;

    private MapData mapData;


    public Long getPostId() {
        return postId;
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

    public Long getNumberLikes() {
        return numberLikes;
    }

    public MapData getMarkers() {
        return mapData;
    }
}
