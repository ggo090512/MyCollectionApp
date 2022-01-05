package com.example.MyCollection.Acitvity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.MyCollection.Models.ImageItem;
import com.example.foodnews.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference databaseRef;
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://foodnews-ea4e2.appspot.com");
    ArrayAdapter<String> arrayAdapter;


    int REQUEST_CODE_CAMERA = 100;
    EditText email, name, address;
    ImageView image;
    ImageButton btnCam, btnShow;
    Spinner cataloge;
    BottomSheetDialog bottomSheetDialog;

    List<String> list;
    String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_main);

        UIinit();
        camOpen();


        if (ContextCompat.checkSelfPermission(CreateActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }
        //Nút camera
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_cam, REQUEST_CODE_CAMERA);
            }
        });
        //Nút hiện bottom sheet view
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(
                        CreateActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_background_sheet,
                                findViewById(R.id.bottomSheetContainer)
                        );

                name = bottomSheetView.findViewById(R.id.edit_name);
                address = bottomSheetView.findViewById(R.id.edit_address);
                list = new ArrayList<>();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String strEmail = user.getEmail();

                //Lấy dữ liệu cataloge dựa vào email
//                databaseRef = FirebaseDatabase.getInstance().getReference("Cataloge");
//                Query query = databaseRef.orderByChild("mEmail").equalTo(strEmail);
//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                            String spinnerName = dataSnapshot.child("mName").getValue(String.class);
//                            list.add(spinnerName);
//                        }
//                        arrayAdapter = new ArrayAdapter<>(CreateActivity.this, android.R.layout.simple_spinner_item, list);
//                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        cataloge.setAdapter(arrayAdapter);
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {}
//                });

                //Nút save
                bottomSheetView.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseRef = FirebaseDatabase.getInstance().getReference("Images");
                        Calendar calendar = Calendar.getInstance();

                        StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis()+".png");
                        image.setDrawingCacheEnabled(true);
                        image.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = mountainsRef.putBytes(data);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast.makeText(CreateActivity.this, "Fail", Toast.LENGTH_LONG).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String img = uri.toString();
                                        categoryId = HomeActivity.categorID;
                                        ImageItem image = new ImageItem(categoryId,name.getText().toString().trim(),address.getText().toString().trim(),user.getEmail(),img);
                                        String imageId = databaseRef.push().getKey();
                                        databaseRef.child(imageId).setValue(image);
                                        Toast.makeText(CreateActivity.this, "Success", Toast.LENGTH_LONG).show();
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }



    private void camOpen() {
        if (ContextCompat.checkSelfPermission(CreateActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }
        Intent open_cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_cam, REQUEST_CODE_CAMERA);
    }

    //Lấy data hình tới img view
    @Override
    protected void onActivityResult (int requestCode, int resultCode,@Nullable Intent data){
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void UIinit(){
        btnCam = findViewById(R.id.btnCam);
        btnShow = findViewById(R.id.btnCreate);
        image = findViewById(R.id.imageViewCreate);
    }
}
