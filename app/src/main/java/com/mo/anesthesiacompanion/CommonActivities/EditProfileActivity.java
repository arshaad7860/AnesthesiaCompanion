package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Anesthetist.MyAppliedActivity;
import com.mo.anesthesiacompanion.Anesthetist.MyNewApprovedActivity;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Doctor.DoctorAddBookingActivity;
import com.mo.anesthesiacompanion.Doctor.DoctorMyBookingsActivity;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    FirebaseFirestore db;
    CollectionReference usersRef;
    TextInputLayout txt_confirm_password, txt_password, txt_email, txt_first_name, txt_last_name, txt_address, txt_suburb,
            txt_city, txt_work_hospital;
    MaterialAutoCompleteTextView autoCompleteOccupation, autoCompleteQualification, autoCompleteExperience;
    MaterialButton btn_confirm;
    UserModel user = new UserModel();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    ImageView btn_my_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        createMyView();
        getUserFromFireStore();

        if (Common.currentUser.getOccupation().equals("Anesthetist")) {
            invalidateOptionsMenu();

            MenuItem item = navigationView.getMenu().findItem(R.id.nav_add_booking);
            item.setVisible(false);
            item.setCheckable(false);
            MenuItem item2 = navigationView.getMenu().findItem(R.id.nav_view_my_booking);
            item2.setVisible(false);
            item2.setCheckable(false);
        } else {
            invalidateOptionsMenu();
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_applied);
            item.setVisible(false);
            item.setCheckable(false);
            MenuItem item2 = navigationView.getMenu().findItem(R.id.nav_approved);
            item2.setVisible(false);
            item2.setCheckable(false);
        }

        btn_confirm.setOnClickListener(v -> {
            if (!validateFields()){
                Toast.makeText(getApplicationContext(), "please fill in fields", Toast.LENGTH_SHORT).show();
                return;
            }
            else {

                updateUserProfile();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });
    }

    private void updateUserProfile() {
        user.setPassword(txt_password.getEditText().getText().toString());
        user.setEmail(txt_email.getEditText().getText().toString());
        user.setFirstname(txt_first_name.getEditText().getText().toString());
        user.setLastname(txt_last_name.getEditText().getText().toString());
        user.setAddress(txt_address.getEditText().getText().toString());
        user.setSuburb(txt_suburb.getEditText().getText().toString());
        user.setCity(txt_city.getEditText().getText().toString());
        user.setHospital(txt_work_hospital.getEditText().getText().toString());
        user.setOccupation(autoCompleteOccupation.getText().toString());
        user.setQualification(autoCompleteQualification.getText().toString());
        user.setPassword(autoCompleteExperience.getText().toString());

        updateUserInFirebase(user);
    }

    private void updateUserInFirebase(UserModel user) {
        usersRef.document(Common.currentUser.getUid()).set(user).addOnSuccessListener(aVoid ->
        {
            Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            Common.currentUser=user;
        })
                .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "failed to update user in fireStore" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getUserFromFireStore() {
        usersRef.document(Common.currentUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                user = documentSnapshot.toObject(UserModel.class);
            } else
                Toast.makeText(EditProfileActivity.this, "User Does Not Exist", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "could not get data from firestore" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void createMyView() {
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txt_password = findViewById(R.id.txt_password);
        txt_email = findViewById(R.id.txt_email);
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_address = findViewById(R.id.txt_address);
        txt_suburb = findViewById(R.id.txt_suburb);
        txt_city = findViewById(R.id.txt_city);
        txt_work_hospital = findViewById(R.id.txt_work_hospital);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_my_menu = findViewById(R.id.btn_my_menu);

        autoCompleteOccupation = findViewById(R.id.autoCompleteOccupation);
        autoCompleteQualification = findViewById(R.id.autoCompleteQualification);
        autoCompleteExperience = findViewById(R.id.autoCompleteExperience);
        String[] optionOccupation = {"Doctor", "Surgeon", "Anesthetist"};
        String[] optionQualification = {"MBChB/MBBCh", "DA (SA)", "FCA (SA)", "etc"};
        String[] optionExperience = {"0-1 Year", "2-5 Year", "6-10 Year", "11+"};

        ArrayAdapter<String> arrayAdapterOccupation = new ArrayAdapter<String>(this, R.layout.options_spinner_item, optionOccupation);
        ArrayAdapter<String> arrayAdapterQualification = new ArrayAdapter<String>(this, R.layout.options_spinner_item, optionQualification);
        ArrayAdapter<String> arrayAdapterExperience = new ArrayAdapter<String>(this, R.layout.options_spinner_item, optionExperience);
        autoCompleteOccupation.setAdapter(arrayAdapterOccupation);
        autoCompleteQualification.setAdapter(arrayAdapterQualification);
        autoCompleteExperience.setAdapter(arrayAdapterExperience);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.content);

        navigationDrawerToggle();
    }

    private void navigationDrawerToggle() {
        //nav Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_edit_profile);
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
            navigationView.setCheckedItem(R.id.nav_edit_profile);
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_edit_profile);
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

    private boolean validateFields(){
        if (!validateFirstName()|!validateAddress()|!validateLastName()|!validateSuburb()|!validateCity()
                |!validateOccupation()|!validateQualification()|!validateExperience()|!validateEmail()|!validatePassword()|!validatePasswordConfirm())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean validateFirstName() {
        String val = txt_first_name.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_first_name.setError("Field cannot be empty");
            return false;
        } else {
            txt_first_name.setError(null);
            txt_first_name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateLastName() {
        String val = txt_last_name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            txt_last_name.setError("Field cannot be empty");
            return false;
        }
        else {
            txt_last_name.setError(null);
            txt_last_name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateAddress() {
        String val = txt_address.getEditText().getText().toString();
        if (val.isEmpty()) {
            txt_address.setError("Field cannot be empty");
            return false;
        }
        else {
            txt_address.setError(null);
            txt_address.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateSuburb() {
        String val = txt_suburb.getEditText().getText().toString();
        if (val.isEmpty()) {
            txt_suburb.setError("Field cannot be empty");
            return false;
        }
        else {
            txt_suburb.setError(null);
            txt_suburb.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateCity() {
        String val = txt_city.getEditText().getText().toString();
        if (val.isEmpty()) {
            txt_city.setError("Field cannot be empty");
            return false;
        }
        else {
            txt_city.setError(null);
            txt_city.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateOccupation() {
        String txt_occupation = autoCompleteOccupation.getText().toString();

        if (txt_occupation.isEmpty()) {
            autoCompleteOccupation.setError("Please select an option");
            return false;
        } else {
            autoCompleteOccupation.setError(null);
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
    private boolean validateExperience() {
        String txt_occupation = autoCompleteExperience.getText().toString();

        if (txt_occupation.isEmpty()) {
            autoCompleteExperience.setError("Please select an option");
            return false;
        } else {
            autoCompleteExperience.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = txt_email.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            txt_email.setError("Field Cannot Be Empty");
            return false;
        }
        else if (!emailValidator(val))
        {
            txt_email.setError("Invalid Email");
            return false;
        }
        else {
            txt_email.setError(null);
            txt_email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = txt_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            txt_password.setError("Field Cannot Be Empty");
            return false;
        }
        else if (val.length()<6)
        {
            txt_password.setError("Password must be greater than 6 characters");
            return false;
        }
        else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePasswordConfirm() {
        String val = txt_confirm_password.getEditText().getText().toString();
        String val2 = txt_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            txt_confirm_password.setError("Field Cannot Be Empty");
            return false;
        }
        else if (val.length()<6)
        {
            txt_confirm_password.setError("Password must be greater than 6 characters");
            return false;
        }
        else if (!(val.equals(val2))){
            txt_confirm_password.setError("Passwords Don't Match");
            return false;
        }
        else {
            txt_confirm_password.setError(null);
            txt_confirm_password.setErrorEnabled(false);
            return true;
        }
    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}