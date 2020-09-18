package com.mo.anesthesiacompanion.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Anesthetist.MyAppliedActivity;
import com.mo.anesthesiacompanion.Anesthetist.MyNewApprovedActivity;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.CommonActivities.DashboardActivity;
import com.mo.anesthesiacompanion.CommonActivities.EditProfileActivity;
import com.mo.anesthesiacompanion.CommonActivities.EmergencyActivity;
import com.mo.anesthesiacompanion.CommonActivities.LoginActivity;
import com.mo.anesthesiacompanion.Model.BookingModel;
import com.mo.anesthesiacompanion.R;

import java.util.Calendar;

public class DoctorAddBookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

    MaterialAutoCompleteTextView autoCompleteQualification, autoCompleteAnesthetic;
    MaterialButton btn_add, btn_cancel;

    TextInputLayout txt_surgery_type,txt_hospital_name,txt_er_room,txt_surgery_description;
    MaterialButton btn_set_time,btn_set_date;
    TextView txt_time,txt_date;
    int timeHour,timeMinutes;
    MaterialCheckBox chk_emergency;

    String bookingId, drName, surgeryType,description, hospitalName, room, surgeryTime,surgeryDate,anestheticType,qualificationType;
    Boolean isEmergency;

    private FirebaseFirestore db;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    ImageView btn_my_menu;
    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_add_booking);

        createMyViews();
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

        btn_set_time.setOnClickListener(v -> {
            //set time
            TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorAddBookingActivity.this,
                    (view, hourOfDay, minute) -> {
                        //initalise hours and minutes
                        timeHour = hourOfDay;
                        timeMinutes=minute;
                        //initalize calander
                        Calendar calendar = Calendar.getInstance();
                        //set hour
                        calendar.set(0,0,0,timeHour,timeMinutes);
                        //set time on text View
                        txt_time.setText(DateFormat.format("hh:mm aa",calendar));
                    },12,0,false);
            //display previous selected time
            timePickerDialog.updateTime(timeHour,timeMinutes);
            //show dialog
            timePickerDialog.show();

        });

        btn_set_date.setOnClickListener(v -> showDatePickerDialog());

        btn_add.setOnClickListener(v -> {
            if (!validateFields()){
                Toast.makeText(DoctorAddBookingActivity.this, "please fill in fields", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                addBookingToFireStore();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });



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
        navigationView.setCheckedItem(R.id.nav_add_booking);
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
            navigationView.setCheckedItem(R.id.nav_add_booking);
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_add_booking);
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

    private void createMyViews() {
        db= FirebaseFirestore.getInstance();


        //hooks
        autoCompleteQualification =findViewById(R.id.autoCompleteQualification);
        autoCompleteAnesthetic =findViewById(R.id.autoCompleteAnesthetic);
        txt_surgery_type =findViewById(R.id.txt_surgery_type);
        txt_hospital_name =findViewById(R.id.txt_hospital_name);
        txt_er_room =findViewById(R.id.txt_er_room);
        btn_set_time =findViewById(R.id.btn_set_time);
        btn_set_date =findViewById(R.id.btn_set_date);
        txt_time =findViewById(R.id.txt_time);
        txt_date =findViewById(R.id.txt_date);
        btn_cancel =findViewById(R.id.btn_cancel);
        btn_add =findViewById(R.id.btn_add);
        chk_emergency =findViewById(R.id.chk_emergency);
        txt_surgery_description =findViewById(R.id.txt_surgery_description);


        String []  optionQualification = {"MBChB/MBBCh","DA (SA)","FCA (SA)", "etc"};
        String []  optionAnesthetic = {"General","Extreme","Other"};


        ArrayAdapter<String> arrayAdapterQualification = new ArrayAdapter<String>(this,R.layout.options_spinner_item, optionQualification);
        ArrayAdapter<String> arrayAdapterAnesthetic = new ArrayAdapter<String>(this,R.layout.options_spinner_item, optionAnesthetic);

        autoCompleteQualification.setAdapter(arrayAdapterQualification);
        autoCompleteAnesthetic.setAdapter(arrayAdapterAnesthetic);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date= "Date: " +dayOfMonth+"/"+month+"/"+year;
        txt_date.setText(date);
    }

    private BookingModel addNewBooking(){

         surgeryType = txt_surgery_type.getEditText().getText().toString();
         hospitalName = txt_hospital_name.getEditText().getText().toString();
         room = txt_er_room.getEditText().getText().toString();
         surgeryTime = txt_time.getText().toString();
         surgeryDate = txt_date.getText().toString();
         drName = "Dr "+Common.currentUser.getLastname().toString();
         anestheticType = autoCompleteAnesthetic.getText().toString();
         qualificationType = autoCompleteQualification.getText().toString();
         description =txt_surgery_description.getEditText().getText().toString();


        if (chk_emergency.isChecked()){
             isEmergency = true;
        }
        else{
            isEmergency=false;
        }
        return new BookingModel(bookingId, Common.currentUser.getUid(),  drName, Common.currentUser.getPhone(), surgeryType,  description,  hospitalName,  room,  surgeryTime,  surgeryDate,  anestheticType, qualificationType, isEmergency);
    }

    private void addBookingToFireStore(){
        BookingModel newBooking= addNewBooking();
        CollectionReference bookingRef =db.collection("Booking");
        bookingRef.add(newBooking).addOnSuccessListener(documentReference -> {
            newBooking.setBookingId(documentReference.getId());
            documentReference.set(newBooking);
            Toast.makeText(DoctorAddBookingActivity.this, "booking created successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(DoctorAddBookingActivity.this, "Failed to add new Booking"+e.getMessage(), Toast.LENGTH_SHORT).show());
        }

    private boolean validateFields(){
        if (!validateAnesthetic()|!validateHospitalName()|!validateQualification()|!validateRoom()|!validateSurgeryType()|!validateSurgeryTime()|!validateSurgeryDate())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean validateQualification() {
        String txt_occupation = autoCompleteQualification.getText().toString();

        if (txt_occupation.isEmpty()) {
            autoCompleteQualification.setError("Please select an option");
            return false;
        } else {
            autoCompleteQualification.setError(null);
            return true;
        }
    }

    private boolean validateAnesthetic() {
        String val = autoCompleteAnesthetic.getText().toString();

        if (val.isEmpty()) {
            autoCompleteAnesthetic.setError("Please select an option");
            return false;
        } else {
            autoCompleteAnesthetic.setError(null);
            return true;
        }
    }

    private boolean validateSurgeryType() {
        String val = txt_surgery_type.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_surgery_type.setError("Field cannot be empty");
            return false;
        } else {
            txt_surgery_type.setError(null);
            txt_surgery_type.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateHospitalName() {
        String val = txt_hospital_name.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_hospital_name.setError("Field cannot be empty");
            return false;
        } else {
            txt_hospital_name.setError(null);
            txt_hospital_name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateRoom() {
        String val = txt_er_room.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_er_room.setError("Field cannot be empty");
            return false;
        } else {
            txt_er_room.setError(null);
            txt_er_room.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateSurgeryTime() {
        String val = txt_time.getText().toString().trim();

        if (val.isEmpty()) {
            txt_time.setError("Field cannot be empty");
            return false;
        } else {
            txt_time.setError(null);
            return true;
        }
    }
    private boolean validateSurgeryDate() {
        String val = txt_date.getText().toString().trim();

        if (val.isEmpty()) {
            txt_date.setError("Field cannot be empty");
            return false;
        } else {
            txt_date.setError(null);
            return true;
        }
    }


}