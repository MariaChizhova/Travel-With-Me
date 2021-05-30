package com.example.travelwithme.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.travelwithme.MapData;
import com.example.travelwithme.MyMarker;
import com.example.travelwithme.pojo.Post;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PostCreateRequest {

    private final Long postId;
    private final Long authorId;
    private final String date;
    private final String description;
    private final String picture;
    private final Long numberLikes;

    private final List<MarkerCreateRequest> markers;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PostCreateRequest(Post post) {
        this.postId = post.getId();
        this.authorId = post.getUser();
        this.date = post.getCreationDate();
        this.description = post.getText();
        this.numberLikes = post.getFavouriteCount();

        Bitmap image = post.getImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        this.picture = Base64.getEncoder().encodeToString(bytes);
        this.markers = new ArrayList<>();
        for (MyMarker marker : post.getMapData().getMarkers()) {
            markers.add(new MarkerCreateRequest(marker));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Post getPost() {
        MapData mapData = new MapData(markers);
        byte[] image = Base64.getDecoder().decode(picture);
        return new Post(authorId, postId, date, description, numberLikes,
                BitmapFactory.decodeByteArray(image, 0, image.length), mapData);
    }

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

    public List<MarkerCreateRequest> getMarkers() {
        return markers;
    }
}
