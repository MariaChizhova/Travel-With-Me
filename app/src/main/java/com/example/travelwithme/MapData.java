package com.example.travelwithme;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapData implements Parcelable {
    private List<Marker> markers;
    private List<LatLng> way;

    public MapData() {
        markers = new ArrayList<>();
        way = new ArrayList<>();
    }

    private static class Marker implements Parcelable {
        LatLng latLng;
        public String text;
        public List<Bitmap> images;

        public Marker(LatLng latLng) {
            this.latLng = latLng;
            images = new ArrayList<>();
        }

        protected Marker(Parcel in) {
            latLng = in.readParcelable(LatLng.class.getClassLoader());
            text = in.readString();
            images = in.createTypedArrayList(Bitmap.CREATOR);
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
            dest.writeParcelable(latLng,flags);
            dest.writeString(text);
            dest.writeTypedList(images);
        }
    }


    protected MapData(Parcel in) {
        markers = in.createTypedArrayList(MapData.Marker.CREATOR);
        way = in.createTypedArrayList(LatLng.CREATOR);
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
        dest.writeTypedList(way);
    }

    public void addToMarkers(LatLng latLng) {
        markers.add(new Marker(latLng));
    }

    public void addToMarkersDescription(int ind, String s, List<Bitmap> list) {
        markers.get(ind).text = s;
        for(Bitmap i : list) {
            markers.get(ind).images.add(i);
        }
    }

    public void addToWay(LatLng latLng) {
        way.add(latLng);
    }

    public int sizeMarkers() {
        return markers.size();
    }

    public LatLng getMarker(int ind) {
        return markers.get(ind).latLng;
    }

    public ArrayList<Bitmap> getImages(int ind) {
        return (ArrayList<Bitmap>) markers.get(ind).images;
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
        return markers.get(ind).text;
    }

    public int sizeWay() {
        return way.size();
    }

    public LatLng getWay(int ind) {
        return way.get(ind);
    }
}
