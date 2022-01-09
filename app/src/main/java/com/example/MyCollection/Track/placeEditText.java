package com.example.MyCollection.Track;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.MyCollection.Acitvity.CreateActivity;
import com.example.foodnews.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class placeEditText extends AppCompatActivity {
    AutocompleteSupportFragment autocompleteSupportFragment;
    private Track destination = new Track();
    private Track userLocation = new Track();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit_text);
        if (!Places.isInitialized()) {
            Places.initialize(placeEditText.this, this.getString(R.string.google_maps_key));
        }
        autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG));
        // ((EditText)autocompleteSupportFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
        // ((EditText)autocompleteSupportFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter address");
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.e("PlaceError", ""+ status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                destination.setPlaceAddress(place.getAddress());
                destination.setLatLng(place.getLatLng().toString());
                Log.i("wtf", "Place: " + destination.getPlaceAddress() + "Latlng: " + destination.getPlaceAddress()
                );
                Intent toCreate = new Intent(placeEditText.this, CreateActivity.class);
                toCreate.putExtra("nameAd",destination.getPlaceAddress());
                toCreate.putExtra("latlng",destination.getLatLng());
                startActivity(toCreate);
                finish();
//                destination.setPlaceAddress(place.getAddress().toString());
//                destination.setLatLng(place.getLatLng().toString());
//                destination.setLonLng("sth");
//                Log.d("testAutoPlace", "address is "+ destination.getPlaceAddress() + " Latitude is: "+ destination.getLatLng() +" Longtitude is: "+ destination.getLonLng());

            }
        });
    }

}