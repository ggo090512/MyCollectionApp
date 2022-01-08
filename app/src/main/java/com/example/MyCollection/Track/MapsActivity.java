package com.example.MyCollection.Track;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.foodnews.R;
import com.example.foodnews.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private MarkerOptions markerOptions1,markerOptions2;
    Button getDirection;
    private Polyline currentPollyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapsActivity.this).execute(getUrl(markerOptions1.getPosition(), markerOptions2.getPosition(), "driving"), "driving");
            }
        });
        if ((ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 10);
        }
        GPSTracker gps = new GPSTracker(MapsActivity.this);
        if (gps.canGetLocation()) {
            double gpslatitude = gps.getLatitude();
            double gpslongitude = gps.getLongitude();
            LatLng gpsLocation = new LatLng(gpslatitude, gpslongitude);
            LatLng desAdress = new LatLng(10.7781, 106.6801);
            markerOptions1 = new MarkerOptions();
            markerOptions2 = new MarkerOptions();
            markerOptions1.position(gpsLocation);
            markerOptions2.position(desAdress);
            markerOptions2.title("desAddress");
            // Setting titile of the infowindow of the marker
            markerOptions1.title("gpsLocation");
            String url = getUrl(markerOptions1.getPosition(),markerOptions2.getPosition(),"driving");
            Log.d("str", "getUrl: "+url);

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(markerOptions1);
        googleMap.addMarker(markerOptions2);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.7781, 106.6801), 14));
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        Log.d("str", "getUrl: "+str_origin);
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        Log.d("str", "getUrl: "+str_dest);
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    @Override
    public void onTaskDone(Object... values) {
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.color(Color.RED);
//        polylineOptions.geodesic(true);
//        polylineOptions.width(5);
//        polylineOptions.clickable(true);

//            Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
//                    .clickable(true)
//                    .add( gpsLocation, desAdress)
//                    .color(Color.RED)
//                    .geodesic(true)
//                    .width(5)
//
//            );
        if (currentPollyline != null)
            currentPollyline.remove();
        currentPollyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}