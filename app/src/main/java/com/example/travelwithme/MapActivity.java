package com.example.travelwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.PolyUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private List<LatLng> way;
    LatLngBounds.Builder latLngBuilder;
    StringBuilder markers;
    private static boolean firstMarker;
    StringBuilder pathMarkers;
    private static boolean firstPathMarker;

    private static final int COLOR_BLACK_ARGB = 0xff0066ff;
    private static final int POLYLINE_STROKE_WIDTH_PX = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        way = new ArrayList<>();
        latLngBuilder = new LatLngBounds.Builder();
        markers = new StringBuilder();
        firstMarker = true;
        pathMarkers = new StringBuilder();
        firstPathMarker = true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMapClickListener(latlng -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            //markerOptions.draggable(true);  // перетаскивание метки
            markerOptions.title("" + latlng.latitude + " " + latlng.longitude);
            way.add(latlng);
            if (!firstMarker) {
                markers.append("|");
            }
            markers.append(latlng.latitude).append(",").append(latlng.longitude);
            firstMarker = false;
            map.addMarker(markerOptions);
            latLngBuilder.include(latlng);
            if (way.size() >= 2) {
                showRoute();
            }
        });

        final Button save = findViewById(R.id.b_save);
        save.setOnClickListener(v -> {
            getStaticMap();
        });
    }

    public void showRoute() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RouteApi routeService = restAdapter.create(RouteApi.class);

        //Вызов запроса на маршрут (асинхрон)
        String from = "" + way.get(way.size() - 2).latitude + "," + way.get(way.size() - 2).longitude;
        String to = "" + way.get(way.size() - 1).latitude + "," + way.get(way.size() - 1).longitude;
        routeService.getRoute(from, to, true, "ru", "AIzaSyB__g8hXOSvYsgukwchDL-CeFTeROm9a58", new Callback<RouteResponse>() {
            public void success(RouteResponse arg0, retrofit.client.Response arg1) {
                //Если прошло успешно, то декодируем маршрут в точки LatLng
                List<LatLng> mPoints = PolyUtil.decode(arg0.getPoints());
                //Строим полилинию
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
        routeService.getRoute(size, markers.toString(), pathMarkers.toString(), "AIzaSyB__g8hXOSvYsgukwchDL-CeFTeROm9a58", new Callback<Response>() {

            @Override
            public void success(Response response, Response response2) {
                try {
                    InputStream inputStream = response2.getBody().in();
                    Intent i = new Intent(getApplicationContext(), StaticMapActivity.class);
                    i.putExtra("data", IOUtils.toByteArray(inputStream));
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
}
