package com.example.MyCollection.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.MyCollection.Interface.ItemClickListener;
import com.example.MyCollection.ViewHolder.MenuViewHolder;
import com.example.MyCollection.Common.Container;
import com.example.MyCollection.Models.Category;
import com.example.foodnews.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.paperdb.Paper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;
    public static FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;

    public static RecyclerView recyclerview;
    public static RecyclerView.LayoutManager layoutManager;
    public static File file;
    public static CardView cardView;
    String loginId = "";
    FloatingActionButton fab;
    TextView nameUser;
    FirebaseAuth fAuth;
    public static String categorID;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fab){
                Intent openMenu = new Intent(HomeActivity.this, CreateCategoryActivity.class);
                startActivity(openMenu);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        nameUser = (TextView)hView.findViewById(R.id.name_User);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();

        String[] name =user.getEmail().split("@");
        nameUser.setText(name[0]);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        cardView = findViewById(R.id.card_item);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(Container.CATEGORY);
        databaseReference.keepSynced(true);

        recyclerview = findViewById(R.id.recyclerview_menu);
        recyclerview.setLayoutManager(new GridLayoutManager(HomeActivity.this,2));
        layoutManager = new LinearLayoutManager(HomeActivity.this);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerview.getContext(),R.anim.anim_layout_fall_down);
        recyclerview.setLayoutAnimation(controller);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(onClickListener);

        if (getIntent() != null)
            loginId = getIntent().getStringExtra("LoginId");
        if (!loginId.isEmpty() && loginId != null){
            LoadMenu(loginId);
        }
        loadMenu2();
    }

    public void LoadMenu(String loginID){

        Query searchByUser = databaseReference.orderByChild("gmail").equalTo(loginID);
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(searchByUser, Category.class)
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
                        Toast.makeText(HomeActivity.this, ""+ adapter.getRef(position).getKey() ,Toast.LENGTH_SHORT).show();

                        Intent foodIntent = new Intent(HomeActivity.this, ViewImageActivity.class);
                        foodIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        categorID = adapter.getRef(position).getKey();
                        startActivity(foodIntent);
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
        Paper.init(this);
    }

    private void loadMenu2() {
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
        recyclerview.getAdapter().notifyDataSetChanged();
        recyclerview.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawable = findViewById(R.id.drawer_layout);
        drawable.closeDrawer(GravityCompat.START);
        return false;
    }
}