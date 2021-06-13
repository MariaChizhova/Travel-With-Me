package com.example.travelwithme.map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.travelwithme.R;
import com.example.travelwithme.api.RouteApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.PolyUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class ViewingMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    MapData mapData;
    Map<Integer, ViewingMarkerDescription> description;
    LatLngBounds.Builder latLngBuilder;

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

        latLngBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < mapData.sizeMarkers(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(mapData.getMarker(i));
            //markerOptions.title(mapData.getText(i));
            Marker m = map.addMarker(markerOptions);
            latLngBuilder.include(mapData.getMarker(i));
            addImageMarker(m, mapData.getImages(i));
        }

        for (int i = 0; i < mapData.sizeMarkers() - 1; i++) {
            showRoute(mapData.getMarker(i), mapData.getMarker(i + 1));
        }

        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
        map.moveCamera(track);

        map.setOnMarkerClickListener(marker -> {
            Log.i("Click", "test");
            if (!description.containsKey(marker.hashCode())) {
                description.put(marker.hashCode(), new ViewingMarkerDescription());
            }
            Bundle bundle = new Bundle();
            int ind = mapData.getIndexOfMarker(marker);
            bundle.putString("text", mapData.getText(ind));
            bundle.putString("name", mapData.getName(ind));
            bundle.putParcelableArrayList("image", mapData.getImages(ind));
            description.get(marker.hashCode()).setArguments(bundle);
            description.get(marker.hashCode()).show(getSupportFragmentManager(), "example dialog");
            return true;
        });

    }

    public void addImageMarker(Marker marker, List<Bitmap> images) {
        if (images.size() > 0) {
            Bitmap bmpWithBorder = Bitmap.createBitmap(images.get(0).getWidth() + 20, images.get(0).getHeight() + 30, images.get(0).getConfig());
            Canvas canvas = new Canvas(bmpWithBorder);
            canvas.drawBitmap(images.get(0), 10, 10, null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            int height = images.get(0).getHeight();
            int width = images.get(0).getWidth();
            path.moveTo(10, images.get(0).getHeight() + 10);
            path.cubicTo(10, height + 25, width / 2 + 5, height + 10, width / 2 + 5, height + 25);
            path.cubicTo(width / 2 + 5, height + 10, width + 10, height + 25, width + 10, height + 10);
            canvas.drawPath(path, paint);

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bmpWithBorder));
        }
    }


    public void showRoute(LatLng fromMarker, LatLng toMarker) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        RouteApi routeService = restAdapter.create(RouteApi.class);

        //Вызов запроса на маршрут (асинхрон)
        String from = "" + fromMarker.latitude + "," + fromMarker.longitude;
        String to = "" + toMarker.latitude + "," + toMarker.longitude;
        routeService.getRoute(from, to, true, "ru", MapActivity.KEY, new Callback<RouteResponse>() {
            public void success(RouteResponse arg0, retrofit.client.Response arg1) {
                //Если прошло успешно, то декодируем маршрут в точки LatLng
                List<LatLng> mPoints = PolyUtil.decode(arg0.getPoints());
                //Строим полилинию
                PolylineOptions line = new PolylineOptions();
                for (int i = 0; i < mPoints.size(); i++) {
                    line.add(mPoints.get(i));
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

}