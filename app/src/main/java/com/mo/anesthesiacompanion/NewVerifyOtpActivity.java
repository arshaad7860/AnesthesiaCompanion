package com.mo.anesthesiacompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.CommonActivities.RegisterActivity3;
import com.mo.anesthesiacompanion.CommonActivities.TestActivity;
import com.mo.anesthesiacompanion.Model.UserModel;

public class NewVerifyOtpActivity extends AppCompatActivity {
    MaterialButton btn_verify_code;
    PinView otpTxt;
    TextView userPhone;
    String mAuthVerificationId,mAuthPassword,mAuthFromIntent,mAuthExtraCheck;
    String phone_val,email_val,password_val,first_name_val,last_name_val,
            address_val,suburb_val,city_val,txt_occupation,txt_qualification,
            txt_experience,txt_work_place;

    FirebaseFirestore db;
    CollectionReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_verify_otp);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        mAuthVerificationId =getIntent().getStringExtra("AuthCredentials");
        mAuthPassword =getIntent().getStringExtra("password");
        mAuthFromIntent =getIntent().getStringExtra("fromIntent");
        mAuthExtraCheck =getIntent().getStringExtra("extraCheck");
        createMyViews();

        btn_verify_code.setOnClickListener(v -> {
            String otp = otpTxt.getText().toString();
            if (otp.isEmpty())
            {
                Toast.makeText(this, "Please enter Otp", Toast.LENGTH_SHORT).show();
            }
            else
            {
                btn_verify_code.setEnabled(false);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = task.getResult().getUser();
                        Common.currentUser =new UserModel();
                        Common.currentUser.setUid(user.getPhoneNumber());
                        if (mAuthFromIntent.equals("Login"))
                            createOrEditFirsestoreUser(Common.currentUser.getUid(),true);

                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Error sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    btn_verify_code.setEnabled(true);
                });
    }

    private void createOrEditFirsestoreUser(String docId,boolean isLogin) {
        if (isLogin)
        {
            userRef.document(docId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists())
                    Toast.makeText(NewVerifyOtpActivity.this, "user found", Toast.LENGTH_SHORT).show();
                else
                {
                    UserModel u =new UserModel();
                    u.setUid(docId);
                    u.setPassword(mAuthPassword);
                    userRef.document(docId).set(u).addOnSuccessListener(aVoid -> Toast.makeText(NewVerifyOtpActivity.this, "new User added", Toast.LENGTH_SHORT).show());

                }
            })
                    .addOnFailureListener(e -> Toast.makeText(NewVerifyOtpActivity.this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else {
            if (mAuthExtraCheck.equals("forgotPassword"))
                userRef.document(docId).update("password",mAuthPassword);
            else {
                getIntentDataRegister();
                UserModel userModel =new UserModel(docId,phone_val,email_val,password_val,first_name_val,last_name_val,address_val,suburb_val,city_val,txt_qualification,txt_experience,txt_occupation,txt_work_place);
                userRef.document(docId).set(userModel);
                Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
                sendUserToHome();
            }

        }
    }

    private void getIntentDataRegister(){

        phone_val=getIntent().getStringExtra("phone");
        email_val=getIntent().getStringExtra("email");
        first_name_val=getIntent().getStringExtra("firstname");
        last_name_val=getIntent().getStringExtra("lastname");
        address_val=getIntent().getStringExtra("address");
        suburb_val=getIntent().getStringExtra("suburb");
        city_val=getIntent().getStringExtra("city");
        txt_occupation=getIntent().getStringExtra("occupation");
        txt_qualification=getIntent().getStringExtra("qualification");
        txt_experience=getIntent().getStringExtra("experience");
        txt_work_place=getIntent().getStringExtra("work");
        password_val=getIntent().getStringExtra("password");

    }
    private void createMyViews() {
        btn_verify_code = findViewById(R.id.btn_verify_code);
        otpTxt = findViewById(R.id.pin_view);
        userPhone = findViewById(R.id.txt_user_phone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser != null) {
            sendUserToHome();
        }
    }

    public void sendUserToHome() {
        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



}