package com.example.MyCollection.Acitvity.FragmentAcitvity;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MyCollection.ViewHolder.MenuViewHolder;
import com.example.MyCollection.Common.Container;
import com.example.MyCollection.Interface.ItemClickListener;
import com.example.MyCollection.Models.Category;
import com.example.foodnews.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {
    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;
    public static FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    public static RecyclerView recyclerview;
    public static RecyclerView.LayoutManager layoutManager;
    public static File file;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.activity_home,container,false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Container.CATEGORY);
        databaseReference.keepSynced(true);

        recyclerview = view.findViewById(R.id.recyclerview_menu);
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        layoutManager = new LinearLayoutManager(getActivity());
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerview.getContext(),R.anim.anim_layout_fall_down);
        recyclerview.setLayoutAnimation(controller);

        LoadMenu();
        loadMenu2();

        return view;
    }

    public void LoadMenu(){
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(databaseReference, Category.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, int position,
                                            @NonNull final Category model) {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                storageRef.child(model.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uri.toString();
                        file = new File(String.valueOf(uri.toString()));
                        model.setImage(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });

                Picasso.get().load(model.getImage()).placeholder(R.drawable.icons8_hamburger_100).fit().into(MenuViewHolder.imageViiew);
                MenuViewHolder.txtMenuTitle.setText(model.getName());
                final Category clicked = model;
                System.out.println(clicked.toString());

                MenuViewHolder.setOnClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), ""+ adapter.getRef(position).getKey() ,Toast.LENGTH_SHORT).show();
                    }

                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
                return new MenuViewHolder(view);
            }
        };
        Paper.init(getActivity());
    }

    private void loadMenu2() {
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
        recyclerview.getAdapter().notifyDataSetChanged();
        recyclerview.scheduleLayoutAnimation();
    }

}
