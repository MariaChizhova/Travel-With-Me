package com.example.travelwithme;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MapData implements Parcelable {
    private final List<Marker> markers;

    public MapData() {
        markers = new ArrayList<>();
    }

    private static class Marker implements Parcelable {
        public String name;
        public String description;
        public LatLng latLng;
        public List<Bitmap> photos;

        public Marker(LatLng latLng) {
            this.latLng = latLng;
            photos = new ArrayList<>();
        }

        protected Marker(Parcel in) {
            name = in.readString();
            description = in.readString();
            latLng = in.readParcelable(LatLng.class.getClassLoader());
            photos = in.createTypedArrayList(Bitmap.CREATOR);
        }

        public static final Creator<MapData.Marker> CREATOR = new Creator<MapData.Marker>() {
            @Override
            public MapData.Marker createFromParcel(Parcel in) {
                return new MapData.Marker(in);
            }

            @Override
            public MapData.Marker[] newArray(int size) {
                return new MapData.Marker[size];
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
            dest.writeParcelable(latLng,flags);
            dest.writeTypedList(photos);
        }

        @Override
        public String toString() {
            final Gson gson = new Gson();
            return gson.toJson(this);
        }
    }


    protected MapData(Parcel in) {
        markers = in.createTypedArrayList(MapData.Marker.CREATOR);
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
        markers.add(new Marker(latLng));
    }

    public void addToMarkersDescription(int ind, String name, String comments, List<Bitmap> list) {
        markers.get(ind).description = comments;
        markers.get(ind).name = name;
        for(Bitmap i : list) {
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

    @Override
    public String toString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }
}
