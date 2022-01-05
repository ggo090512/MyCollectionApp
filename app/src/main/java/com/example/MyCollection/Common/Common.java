package com.example.MyCollection.Common;

import androidx.recyclerview.widget.RecyclerView;

import com.example.MyCollection.ViewHolder.MenuViewHolder;
import com.example.MyCollection.Models.Category;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Common {

    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;

    public static FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    public static RecyclerView recyclerview;
    public static RecyclerView.LayoutManager layoutManager;

    public static FirebaseStorage storage;
    public static StorageReference storageRef;
}
