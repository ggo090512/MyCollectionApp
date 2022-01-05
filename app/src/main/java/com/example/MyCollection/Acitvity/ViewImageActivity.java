package com.example.MyCollection.Acitvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.example.MyCollection.Models.ImageItem;
import com.example.foodnews.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewimage);

        imageView = findViewById(R.id.imageViewCreate);
        viewPager2 = findViewById(R.id.image);
        btnAdd = findViewById(R.id.btn_add);
        progressBar = findViewById(R.id.progress_circle);

        imagesList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        imagesList = new ArrayList<>();
        clearAll();
        //GetDataFromFirebase();

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null){
            GetDataFromFirebase(categoryId);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent itemIntent = new Intent(ViewImageActivity.this, CreateActivity.class);
//                itemIntent.putExtra("CategoryId", categoryId);
                startActivity(itemIntent);
            }
        });
    }

    private void GetDataFromFirebase(String categoryId){
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
                    imagesList.add(image);
                }
                imageAdapter = new ImageAdapter(imagesList, getApplicationContext());
                viewPager2.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        pos = position;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewImageActivity.this, "NO IMAGE", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void clearAll(){
        if (imagesList != null){
            imagesList.clear();

            if (imageAdapter != null){
                imageAdapter.notifyDataSetChanged();
            }
        }
        imagesList = new ArrayList<>();
    }

}
