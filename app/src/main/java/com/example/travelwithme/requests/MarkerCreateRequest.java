package com.example.travelwithme.requests;


import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.travelwithme.Marker;

import java.util.ArrayList;
import java.util.List;


public class MarkerCreateRequest {

    private final String name;
    private final String description;
    private final double latitude;
    private final double longitude;

    private final List<PhotoCreateRequest> photos;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public MarkerCreateRequest(Marker marker) {
        this.name = marker.getName();
        this.description = marker.getText();
        this.latitude = marker.getLatLng().latitude;
        this.longitude = marker.getLatLng().longitude;

        photos = new ArrayList<>();
        for (Bitmap image : marker.getImages()) {
            photos.add(new PhotoCreateRequest(image));
        }
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<PhotoCreateRequest> getPhotos() {
        return photos;
    }
}