package com.example.MyCollection.Acitvity;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.MyCollection.Models.ImageItem;
import com.example.MyCollection.Track.MapsActivity;
import com.example.MyCollection.Track.placeEditText;
import com.example.foodnews.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class ViewImageActivity extends Activity {
    static int pos;
    private ImageButton btnAdd, btnMap;
    private ViewPager2 viewPager2;
    private DatabaseReference dbRef;
    private ImageAdapter imageAdapter;
    private ArrayList<ImageItem> imagesList;
    private ImageView imageView;
    private ProgressBar progressBar;
    private List<ImageItem> pagerMList = new ArrayList<>();
    static String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);

        imageView = findViewById(R.id.imageViewCreate);
        viewPager2 = findViewById(R.id.image);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_circle);
        btnMap = findViewById(R.id.btn_map);
        imagesList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        imagesList = new ArrayList<>();
        clearAll();

        categoryId = HomeActivity.categorID;
        GetDataFromFirebase(categoryId);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent itemIntent = new Intent(ViewImageActivity.this, placeEditText.class);
                startActivity(itemIntent);
                Activity context = ViewImageActivity.this;
                context.finish();
            }
        });
        viewPager2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        ;
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager2.getCurrentItem();
                Log.e("index", viewPager2.getCurrentItem() + "");
                Log.e("pos", "onPageSelected: " + index);
                Log.e("temp", imagesList.get(index).getLat() + "");
                String latTemp = imagesList.get(index).getLat();
                String lonTemp = imagesList.get(index).getLon();
                Intent toTrack = new Intent(ViewImageActivity.this, MapsActivity.class);
                Log.e("check Cord", "" + latTemp + " " + lonTemp);
                toTrack.putExtra("latDes", latTemp);
                toTrack.putExtra("lonDes", lonTemp);
                startActivity(toTrack);
                finish();
            }
        });
    }



    private void GetDataFromFirebase(String categoryId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Images");
        Query query = dbRef.orderByChild("mCate").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearAll();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ImageItem image = new ImageItem();
                    image.setmImage(dataSnapshot.child("mImage").getValue().toString());
                    image.setmName(dataSnapshot.child("mName").getValue().toString());
                    image.setmAddress(dataSnapshot.child("mAddress").getValue().toString());
                    image.setLat(dataSnapshot.child("lat").getValue().toString());
                    image.setLon(dataSnapshot.child("lon").getValue().toString());

                    imagesList.add(image);
                }
                imageAdapter = new ImageAdapter(imagesList, getApplicationContext());
                viewPager2.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewImageActivity.this, "NO IMAGE", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void clearAll() {
        if (imagesList != null) {
            imagesList.clear();

            if (imageAdapter != null) {
                imageAdapter.notifyDataSetChanged();
            }
        }
        imagesList = new ArrayList<>();
    }

}
