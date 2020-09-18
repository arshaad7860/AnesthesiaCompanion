package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mo.anesthesiacompanion.Adapter.BookingAdapter;
import com.mo.anesthesiacompanion.Anesthetist.MyAppliedActivity;
import com.mo.anesthesiacompanion.Anesthetist.MyNewApprovedActivity;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Doctor.DoctorAddBookingActivity;
import com.mo.anesthesiacompanion.Doctor.DoctorMyBookingsActivity;
import com.mo.anesthesiacompanion.Model.BookingModel;
import com.mo.anesthesiacompanion.R;

public class EmergencyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseFirestore db;
    CollectionReference bookingsRef;
    BookingAdapter adapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    ImageView btn_my_menu;
    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        db =FirebaseFirestore.getInstance();
        bookingsRef= db.collection("Booking");
        setupRecyclerView();

        createMyMenu();

        if (Common.currentUser.getOccupation().equals("Anesthetist")) {
            invalidateOptionsMenu();

            MenuItem item = navigationView.getMenu().findItem(R.id.nav_add_booking);
            item.setVisible(false);
            item.setCheckable(false);
            MenuItem item2 = navigationView.getMenu().findItem(R.id.nav_view_my_booking);
            item2.setVisible(false);
            item2.setCheckable(false);
        }
        else {
            invalidateOptionsMenu();
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_applied);
            item.setVisible(false);
            item.setCheckable(false);
            MenuItem item2 = navigationView.getMenu().findItem(R.id.nav_approved);
            item2.setVisible(false);
            item2.setCheckable(false);
        }

        adapter.startListening();
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id= documentSnapshot.getId();
            String path= documentSnapshot.getReference().getId();
            Intent intent = new Intent(getApplicationContext(),ViewBookingActivity.class);
            intent.putExtra("path",path);
            intent.putExtra("bookingId",id);
            startActivity(intent);
        });
    }
    private void setupRecyclerView() {
        Query query = bookingsRef.whereEqualTo("emergency",true).orderBy("surgeryDate");
        FirestoreRecyclerOptions<BookingModel> options =new FirestoreRecyclerOptions.Builder<BookingModel>()
                .setQuery(query, BookingModel.class)
                .build();
        adapter=new BookingAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recycler_emergency);


        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void createMyMenu() {
        btn_my_menu = findViewById(R.id.btn_my_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);

        navigationDrawerToggle();
    }

    private void navigationDrawerToggle() {
        //nav Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_emergency);
        btn_my_menu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (Common.currentUser.getOccupation().equals("Anesthetist")) {
            MenuItem item = menu.findItem(R.id.nav_add_booking);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.nav_view_my_booking);
            item2.setVisible(false);
        } else {
            MenuItem item = menu.findItem(R.id.nav_approved);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.nav_applied);
            item2.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (Common.currentUser.getOccupation().equals("Anesthetist")) {
            MenuItem item = menu.findItem(R.id.nav_add_booking);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.nav_view_my_booking);
            item2.setVisible(false);
        } else {
            MenuItem item = menu.findItem(R.id.nav_approved);
            item.setVisible(false);
            MenuItem item2 = menu.findItem(R.id.nav_applied);
            item2.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            navigationView.setCheckedItem(R.id.nav_emergency);
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_emergency);
            item.setChecked(true);
        } else {
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_home);
            navigationView.setCheckedItem(R.id.nav_home);
            item.setChecked(true);
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }


    @Override
    protected void onStart() {
        //adapter.startListening();
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
                break;

            case R.id.nav_add_booking:
                if (Common.currentUser.getOccupation().equals("Anesthetist"))
                    menuItem.setVisible(false);
                startActivity(new Intent(getApplicationContext(), DoctorAddBookingActivity.class));
                finish();
                break;

            case R.id.nav_view_my_booking:
                if (Common.currentUser.getOccupation().equals("Anesthetist"))
                    menuItem.setVisible(false);
                startActivity(new Intent(getApplicationContext(), DoctorMyBookingsActivity.class));
                finish();
                break;

            case R.id.nav_applied:
                if (!Common.currentUser.getOccupation().equals("Anesthetist"))
                    menuItem.setVisible(false);
                startActivity(new Intent(getApplicationContext(), MyAppliedActivity.class));
                finish();
                break;

            case R.id.nav_approved:
                if (!Common.currentUser.getOccupation().equals("Anesthetist"))
                    menuItem.setVisible(false);
                startActivity(new Intent(getApplicationContext(), MyNewApprovedActivity.class));
                finish();
                break;

            case R.id.nav_emergency:
                startActivity(new Intent(getApplicationContext(), EmergencyActivity.class));
                finish();
                break;

            case R.id.nav_edit_profile:
                startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                finish();
                break;

            case R.id.nav_logout:
                Common.currentUser = null;
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;

        }
        return true;
    }
}