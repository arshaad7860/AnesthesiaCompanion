package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.R;

public class VerifyOTPActivity extends AppCompatActivity {
    MaterialButton btn_verify_code;
    PinView pinFromUser;
    TextView userPhone;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    FirebaseFirestore fStore;
    PhoneAuthProvider.ForceResendingToken mToken;
    UserModel user;
    String _userPhone, comingActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        btn_verify_code = findViewById(R.id.btn_verify_code);
        pinFromUser = findViewById(R.id.pin_view);
        userPhone = findViewById(R.id.txt_user_phone);

        fStore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        Intent intentfromreg = getIntent();
        userPhone.setText(intentfromreg.getStringExtra("AuthCredentials"));
        _userPhone = intentfromreg.getStringExtra("AuthCredentials");
        comingActivity = intentfromreg.getStringExtra("comingActivity");

        btn_verify_code.setOnClickListener(v -> {
            String otp = pinFromUser.getEditableText().toString();
            if (otp.isEmpty()) {
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                btn_verify_code.setEnabled(false);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(_userPhone, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOTPActivity.this, task -> {
                    if (task.isSuccessful()) {
                        if (comingActivity.equals("Login"))
                            sendUserToHome();
                        if (comingActivity.equals("Register"))
                            sendUserToLogin();
                        if (comingActivity.equals("ForgotPassword"))
                            sendUserToResetPassword();

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(VerifyOTPActivity.this, "error credentials error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    btn_verify_code.setEnabled(true);
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mCurrentUser != null && comingActivity.equals("Login")) {
            sendUserToHome();
        }
    }

    public void sendUserToHome(){
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void sendUserToLogin(){
        storeNewUsersToDatabase();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void sendUserToResetPassword(){
        updateOldUserData();
        Intent intent = new Intent(getApplicationContext(), SetNewPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void updateOldUserData() {
        Intent intent = new Intent(VerifyOTPActivity.this, SetNewPasswordActivity.class);
        intent.putExtra("phone", _userPhone);
        startActivity(intent);
        finish();
    }

    private void storeNewUsersToDatabase() {
        fStore.collection("users").document(_userPhone).set(createNewUser()).addOnSuccessListener(documentReference ->
                Toast.makeText(VerifyOTPActivity.this, "created new User", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(VerifyOTPActivity.this, "Failed to add to database", Toast.LENGTH_SHORT).show());

    }

    public UserModel createNewUser() {

        //get data from prev activity
        Intent intentReg2 = getIntent();
        String phone_val = intentReg2.getStringExtra("phone");
        String email_val = intentReg2.getStringExtra("email");
        String password_val = intentReg2.getStringExtra("password");
        String first_name_val = intentReg2.getStringExtra("firstname");
        String last_name_val = intentReg2.getStringExtra("lastname");
        String address_val = intentReg2.getStringExtra("address");
        String suburb_val = intentReg2.getStringExtra("suburb");
        String city_val = intentReg2.getStringExtra("city");
        String occupation_val = intentReg2.getStringExtra("occupation");
        String experience_val = intentReg2.getStringExtra("experience");
        String qualification_val = intentReg2.getStringExtra("qualification");
        String work_hospital_val = intentReg2.getStringExtra("work");

        user = new UserModel();
        user.setUid(phone_val);
        user.setPhone(phone_val);
        user.setEmail(email_val);
        user.setPassword(password_val);
        user.setFirstname(first_name_val);
        user.setLastname(last_name_val);
        user.setAddress(address_val);
        user.setSuburb(suburb_val);
        user.setCity(city_val);
        user.setOccupation(occupation_val);
        user.setExperience(experience_val);
        user.setQualification(qualification_val);
        user.setHospital(work_hospital_val);

        Common.currentUser = user;

        return user;

    }


}