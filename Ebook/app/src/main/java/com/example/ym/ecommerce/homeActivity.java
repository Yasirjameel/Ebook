package com.example.ym.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.example.ym.ecommerce.Model.Products;
import com.example.ym.ecommerce.Prevalent.Prevalent;
import com.example.ym.ecommerce.Viewholder.Productviewholder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class homeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewFlipper viewFlipper;
    private DatabaseReference productref;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String type = "";
    ShimmerFrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        container = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                container.hideShimmer();
            }
        }, 3000);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        productref = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView =(RecyclerView) findViewById(R.id.recyclermenu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        int images[]={R.drawable.slide1, R.drawable.slide3, R.drawable.slide4};

        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);

        for(int image : images){
            flipperimages(image);
        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!type.equals("Admin"))
                {
                    Intent intent = new Intent(homeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hearderview = navigationView.getHeaderView(0);
        TextView username = hearderview.findViewById(R.id.userprofilename);
        CircleImageView profileimageview = hearderview.findViewById(R.id.profile_image);
        username.setText(Prevalent.onlineuser.getName());

        if (!type.equals("Admin"))
        {
            username.setText(Prevalent.onlineuser.getName());
            Picasso.get().load(Prevalent.onlineuser.getImage()).placeholder(R.drawable.profile).into(profileimageview);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productref, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, Productviewholder> adapter =
                new FirebaseRecyclerAdapter<Products, Productviewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Productviewholder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "PKR");
                        Picasso.get().load(model.getImage()).into(holder.imageView);
//                        container.hideShimmer();


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                            Intent intent = new Intent(homeActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);

                    }
                });


            }

                    @NonNull
                    @Override
                    public Productviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                        Productviewholder holder = new Productviewholder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    public void flipperimages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


       // if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if (!type.equals("Admin"))
            {
                Intent intent = new Intent(homeActivity.this, CartActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_order)
        {Intent intent = new Intent(homeActivity.this, SearchProductsActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.nav_settings) {
            if (!type.equals("Admin"))
            {
                Intent intent = new Intent(homeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_logout) {
           Paper.book().destroy();

            Intent intent = new Intent(homeActivity.this,MainActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
