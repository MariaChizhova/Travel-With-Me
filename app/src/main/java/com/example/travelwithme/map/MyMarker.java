package com.example.travelwithme.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.example.travelwithme.requests.MarkerCreateRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MyMarker implements Parcelable {
    public String name;
    public String description;
    public LatLng latLng;
    public List<Bitmap> photos;

    public MyMarker(LatLng latLng) {
        this.latLng = latLng;
        photos = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MyMarker(MarkerCreateRequest m) {
        name = m.getName();
        description = m.getDescription();
        latLng = new LatLng(m.getLatitude(), m.getLongitude());
        photos = new ArrayList<>();
        if (m.getPhotos() != null) {
            for (String p : m.getPhotos()) {
                byte[] image = Base64.getDecoder().decode(p);
                photos.add(BitmapFactory.decodeByteArray(image, 0, image.length));
            }
        }
    }

    protected MyMarker(Parcel in) {
        name = in.readString();
        description = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        photos = in.createTypedArrayList(Bitmap.CREATOR);
    }

    public static final Creator<MyMarker> CREATOR = new Creator<MyMarker>() {
        @Override
        public MyMarker createFromParcel(Parcel in) {
            return new MyMarker(in);
        }

        @Override
        public MyMarker[] newArray(int size) {
            return new MyMarker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(latLng, flags);
        dest.writeTypedList(photos);
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getText() {
        return description;
    }

    public List<Bitmap> getImages() {
        return photos;
    }

}