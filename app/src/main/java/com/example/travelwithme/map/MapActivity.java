package com.example.travelwithme.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.Manifest;

import com.example.travelwithme.PermissionUtils;
import com.example.travelwithme.R;
import com.example.travelwithme.api.RouteApi;
import com.example.travelwithme.api.StaticApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MapActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap map;
    LatLngBounds.Builder latLngBuilder;
    StringBuilder markers;
    private boolean firstMarker;
    StringBuilder pathMarkers;
    private boolean firstPathMarker;
    Map<Integer, MarkerDescription> description;
    int currentDialog;
    Marker currentMarker;
    public final static String KEY = "";


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionDenied = false;

    PlacesClient placesClient;

    MapData mapData;

    private static final int COLOR_BLACK_ARGB = 0xff0066ff;
    private static final int POLYLINE_STROKE_WIDTH_PX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapData = new MapData();
        latLngBuilder = new LatLngBounds.Builder();
        markers = new StringBuilder();
        pathMarkers = new StringBuilder();
        firstPathMarker = true;
        firstMarker = true;
        description = new HashMap<>();


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), KEY);
        }
        placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(place.getLatLng());
                map.addMarker(markerOptions);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("Error", "An error occurred: " + status);
            }
        });
    }


    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();

        map.setOnMapClickListener(latlng -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            //markerOptions.draggable(true);  // перетаскивание метки
            //markerOptions.title("" + latlng.latitude + " " + latlng.longitude);
            mapData.addToMarkers(latlng);
            if (!firstMarker) {
                markers.append("|");
            }
            markers.append(latlng.latitude).append(",").append(latlng.longitude);
            firstMarker = false;
            map.addMarker(markerOptions);
            latLngBuilder.include(latlng);
            if (mapData.sizeMarkers() >= 2) {
                showRoute();
            }
        });

        map.setOnMarkerClickListener(marker -> {
            Log.i("Click", "test");
            if (!description.containsKey(marker.hashCode())) {
                description.put(marker.hashCode(), new MarkerDescription());
            }
            currentDialog = mapData.getIndexOfMarker(marker);
            currentMarker = marker;
            description.get(marker.hashCode()).show(getSupportFragmentManager(), "example dialog");
            return true;
        });

        final Button save = findViewById(R.id.b_save);
        save.setOnClickListener(v -> {
            getStaticMap();
        });
    }

    public void addDescriptionOnMarker(String comments, String name, List<Bitmap> image) {
        mapData.addToMarkersDescription(currentDialog, name, comments, image);
        if (image.size() > 0) {
            Bitmap bmpWithBorder = Bitmap.createBitmap(image.get(0).getWidth() + 20, image.get(0).getHeight() + 30, image.get(0).getConfig());
            Canvas canvas = new Canvas(bmpWithBorder);
            canvas.drawBitmap(image.get(0), 10, 10, null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            int height = image.get(0).getHeight();
            int width = image.get(0).getWidth();
            path.moveTo(10, image.get(0).getHeight() + 10);
            path.cubicTo(10, height + 25, width / 2 + 5, height + 10, width / 2 + 5, height + 25);
            path.cubicTo(width / 2 + 5, height + 10, width + 10, height + 25, width + 10, height + 10);
            canvas.drawPath(path, paint);

            currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bmpWithBorder));
        }
    }

    public void showRoute() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RouteApi routeService = restAdapter.create(RouteApi.class);

        String from = "" + mapData.getMarker(mapData.sizeMarkers() - 2).latitude + "," + mapData.getMarker(mapData.sizeMarkers() - 2).longitude;
        String to = "" + mapData.getMarker(mapData.sizeMarkers() - 1).latitude + "," + mapData.getMarker(mapData.sizeMarkers() - 1).longitude;
        routeService.getRoute(from, to, true, "ru", KEY, new Callback<RouteResponse>() {
            public void success(RouteResponse arg0, retrofit.client.Response arg1) {
                List<LatLng> mPoints = PolyUtil.decode(arg0.getPoints());
                PolylineOptions line = new PolylineOptions();
                for (int i = 0; i < mPoints.size(); i++) {
                    line.add(mPoints.get(i));
                    if (!firstPathMarker) {
                        pathMarkers.append("|");
                    }
                    pathMarkers.append(mPoints.get(i).latitude).append(",").append(mPoints.get(i).longitude);
                    firstPathMarker = false;
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
            }

            @Override
            public void failure(RetrofitError error) {

            }

        });
    }

    public void getStaticMap() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        StaticApi routeService = restAdapter.create(StaticApi.class);

        String size = "600x600";
        routeService.getRoute(size, markers.toString(), pathMarkers.toString(), KEY, new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                try {
                    InputStream inputStream = response2.getBody().in();
                    Intent i = new Intent(getApplicationContext(), StaticMapActivity.class);
                    i.putExtra("data", IOUtils.toByteArray(inputStream));
                    i.putExtra("mapData", mapData);
                    startActivity(i);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
//        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
//                .show();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
    }

    @Override
    public boolean onMyLocationButtonClick() {
//        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
//                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}
