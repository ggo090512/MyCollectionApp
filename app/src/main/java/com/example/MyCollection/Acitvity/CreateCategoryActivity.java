package com.example.MyCollection.Acitvity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.MyCollection.Common.Container;
import com.example.MyCollection.Common.stringNoitce;
import com.example.MyCollection.Models.Category;
import com.example.foodnews.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class CreateCategoryActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText txtNameCategory;
    int REQUEST_CODE_CAMERA = 100;
    Button btnCategory;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    BottomSheetDialog bottomSheetDialog;
    List<String> list;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_category);
        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        camOpen();

        btnCategory = findViewById(R.id.btnCreateCategory);
        imageView = findViewById(R.id.imageViewCreateCata);
        if (ContextCompat.checkSelfPermission(CreateCategoryActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateCategoryActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 101);
        }

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show();
            }
        });
    }

    private void Show(){
        bottomSheetDialog = new BottomSheetDialog(
                CreateCategoryActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_cataloge_sheet,
                        findViewById(R.id.bottomSheetContainerCataloge)
                );

        txtNameCategory = bottomSheetView.findViewById(R.id.edit_Cataname);
        list = new ArrayList<>();
        bottomSheetView.findViewById(R.id.btnSaveCata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("img"+calendar.getTimeInMillis()+".png");

                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),stringNoitce.ERROR,Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoStringLink = uri.toString();
                                fAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = fAuth.getCurrentUser();
                                databaseReference.child(Container.CATEGORY).push().setValue( new Category(txtNameCategory.getText().toString(),photoStringLink,user.getEmail()), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error == null){
                                            Toast.makeText(getApplicationContext(),stringNoitce.SUCCESS,Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(),stringNoitce.ERROR,Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    private void camOpen() {
        Intent open_cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_cam, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get(stringNoitce.DATA);
            imageView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}