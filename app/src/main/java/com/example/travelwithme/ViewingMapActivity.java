package com.example.travelwithme;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.HashMap;
import java.util.Map;

public class ViewingMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    MapData mapData;
    Map<Integer,ViewingMarkerDescription> description;

    private static final int COLOR_BLACK_ARGB = 0xff0066ff;
    private static final int POLYLINE_STROKE_WIDTH_PX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapData = getIntent().getParcelableExtra("mapData");
        description = new HashMap<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < mapData.sizeMarkers(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mapData.getMarker(i));
            //markerOptions.title(mapData.getText(i));
            map.addMarker(markerOptions);
            latLngBuilder.include(mapData.getMarker(i));
        }

        PolylineOptions line = new PolylineOptions();
        for (int i = 0; i < mapData.sizeWay(); i++) {
            line.add(mapData.getWay(i));
        }
        Polyline polyline = map.addPolyline(line);
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(COLOR_BLACK_ARGB);
        polyline.setJointType(JointType.ROUND);
        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        map.moveCamera(track);

        map.setOnMarkerClickListener(marker -> {
            Log.i("Click", "test");
            if (!description.containsKey(marker.hashCode())) {
                description.put(marker.hashCode(),new ViewingMarkerDescription());
            }
            Bundle bundle = new Bundle();
            int ind = mapData.getIndexOfMarker(marker);
            bundle.putString("text", mapData.getText(ind));
            bundle.putParcelableArrayList("image", mapData.getImages(ind));
            description.get(marker.hashCode()).setArguments(bundle);
            description.get(marker.hashCode()).show(getSupportFragmentManager(), "example dialog");
            return true;
        });

    }

}