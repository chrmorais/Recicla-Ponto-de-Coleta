package com.aronbordin.reciclae;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aronbordin.reciclae.adapter.Ponto;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Ponto> pontos;
    private Location location;
    private Map<Marker, Ponto> makersMap = new HashMap<>();
    private SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pontos = getIntent().getParcelableArrayListExtra("PONTOS");
        location = getIntent().getParcelableExtra("LOCATION");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(false);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        LatLng my_location = null;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        if(location != null){
            my_location = new LatLng(location.getLatitude(), location.getLongitude());
            boundsBuilder.include(my_location);
        }

        for(Ponto p: pontos){
            LatLng maker = new LatLng(p.getLatitude(), p.getLongitude());
            Marker m = mMap.addMarker(new MarkerOptions().position(maker).title(p.getNome()).snippet(p.getEndereco()));
            boundsBuilder.include(maker);
            makersMap.put(m, p);

            if(my_location == null){
                my_location = maker;
            }
        }

        final LatLngBounds bounds = boundsBuilder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        } catch (IllegalStateException ise) {

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50), 500, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            mMap.getUiSettings().setAllGesturesEnabled(true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            });
        }
    }
}
