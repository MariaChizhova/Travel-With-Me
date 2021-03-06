package com.example.travelwithme.map;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.RequiresApi;

import com.example.travelwithme.requests.MarkerCreateRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapData implements Parcelable {
    private final List<MyMarker> markers;

    public MapData() {
        markers = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MapData(List<MarkerCreateRequest> markers) {
        this.markers = new ArrayList<>();
        if (markers != null) {
            for (MarkerCreateRequest m : markers) {
                MyMarker marker = new MyMarker(m);
                this.markers.add(marker);
            }
        }
    }

    protected MapData(Parcel in) {
        markers = in.createTypedArrayList(MyMarker.CREATOR);
    }

    public static final Creator<MapData> CREATOR = new Creator<MapData>() {
        @Override
        public MapData createFromParcel(Parcel in) {
            return new MapData(in);
        }

        @Override
        public MapData[] newArray(int size) {
            return new MapData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(markers);
    }

    public void addToMarkers(LatLng latLng) {
        markers.add(new MyMarker(latLng));
    }

    public void addToMarkersDescription(int ind, String name, String comments, List<Bitmap> list) {
        markers.get(ind).description = comments;
        markers.get(ind).name = name;
        for (Bitmap i : list) {
            markers.get(ind).photos.add(i);
        }
    }

    public int sizeMarkers() {
        return markers.size();
    }

    public LatLng getMarker(int ind) {
        return markers.get(ind).latLng;
    }

    public ArrayList<Bitmap> getImages(int ind) {
        return (ArrayList<Bitmap>) markers.get(ind).photos;
    }

    public ArrayList<String> getPhotosURL(int ind) {
        return (ArrayList<String>) markers.get(ind).photosURL;
    }

    public String getIcon(int ind) {
        return markers.get(ind).icon;
    }

    public int getIndexOfMarker(com.google.android.gms.maps.model.Marker marker) {
        for (int i = 0; i < markers.size(); i++) {
            if (markers.get(i).latLng.equals(marker.getPosition())) {
                return i;
            }
        }
        return -1;
    }

    public String getText(int ind) {
        return markers.get(ind).description;
    }

    public String getName(int ind) {
        return markers.get(ind).name;
    }

    public List<MyMarker> getMarkers() {
        return markers;
    }
}
