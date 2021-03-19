package com.example.travelwithme;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapData implements Parcelable {
    private List<LatLng> markers;
    private List<LatLng> way;

    public MapData() {
        markers = new ArrayList<>();
        way = new ArrayList<>();
    }

    protected MapData(Parcel in) {
        markers = in.createTypedArrayList(LatLng.CREATOR);
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

    public void addToMarkers(LatLng latLng){
        markers.add(latLng);
    }

    public void addToWay(LatLng latLng){
        way.add(latLng);
    }

    public int sizeMarkers(){
        return markers.size();
    }

    public LatLng getMarkers(int ind){
        return markers.get(ind);
    }

    public int sizeWay(){
        return way.size();
    }

    public LatLng getWay(int ind){
        return way.get(ind);
    }
}
