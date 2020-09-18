package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.NewVerifyOtpActivity;
import com.mo.anesthesiacompanion.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity3 extends AppCompatActivity {
    MaterialButton btn_next, btn_login;
    ImageView btn_back_register;
    TextInputLayout txt_work_hospital;

    MaterialAutoCompleteTextView autoCompleteOccupation, autoCompleteQualification, autoCompleteExperience;

    FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseFirestore db;
    CollectionReference userRef;

    String password;
    String login= "Register";
    String phone_val;
    String email_val;
    String password_val;
    String first_name_val;
    String last_name_val;
    String address_val;
    String suburb_val;
    String city_val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        createMyViews();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        getIntentDataFromReg2();


        //click btn events
        btn_login.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
        btn_next.setOnClickListener(view -> {

            if (!validateFields()){
                Toast.makeText(this, "Enter Valid Fields", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                btn_next.setEnabled(false);
                sendVerCodeToUserPhone(phone_val);
            }
        });
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity3.this, "Verification Failed Try Again", Toast.LENGTH_SHORT).show();
                btn_next.setEnabled(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String txt_occupation = autoCompleteOccupation.getText().toString();
                        String txt_qualification = autoCompleteQualification.getText().toString();
                        String txt_experience = autoCompleteExperience.getText().toString();
                        String txt_work_place = txt_work_hospital.getEditText().getText().toString();
                        Intent otpIntent = new Intent(RegisterActivity3.this, NewVerifyOtpActivity.class);
                        otpIntent.putExtra("phone",phone_val);
                        otpIntent.putExtra("email",email_val);
                        otpIntent.putExtra("password",password_val);
                        otpIntent.putExtra("firstname",first_name_val);
                        otpIntent.putExtra("lastname",last_name_val);
                        otpIntent.putExtra("address",address_val);
                        otpIntent.putExtra("suburb",suburb_val);
                        otpIntent.putExtra("city",city_val);
                        otpIntent.putExtra("occupation",txt_occupation);
                        otpIntent.putExtra("qualification",txt_qualification);
                        otpIntent.putExtra("experience",txt_experience);
                        otpIntent.putExtra("work",txt_work_place);
                        otpIntent.putExtra("AuthCredentials",s);
                        otpIntent.putExtra("fromIntent","Register");
                        otpIntent.putExtra("extraCheck","Register");
                        startActivity(otpIntent);
                    }
                },5000);
            }
        };

        btn_back_register.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        });
    }

    private void getIntentDataFromReg2() {
        //get values from prev activity
        Intent intentReg2 = getIntent();
         phone_val = intentReg2.getStringExtra("phone");
         email_val = intentReg2.getStringExtra("email");
         password_val = intentReg2.getStringExtra("password");
         first_name_val = intentReg2.getStringExtra("firstname");
         last_name_val = intentReg2.getStringExtra("lastname");
         address_val = intentReg2.getStringExtra("address");
         suburb_val = intentReg2.getStringExtra("suburb");
         city_val = intentReg2.getStringExtra("city");


    }

    private void createMyViews() {
        //hooks
        btn_login =findViewById(R.id.btn_login);
        btn_next =findViewById(R.id.btn_next);
        btn_back_register =findViewById(R.id.btn_back_register);
        txt_work_hospital =findViewById(R.id.txt_work_hospital);

        autoCompleteOccupation =findViewById(R.id.autoCompleteOccupation);
        autoCompleteQualification =findViewById(R.id.autoCompleteQualification);
        autoCompleteExperience =findViewById(R.id.autoCompleteExperience);
        String []  optionOccupation = {"Doctor","Surgeon","Anesthetist"};
        String []  optionQualification = {"MBChB/MBBCh","DA (SA)","FCA (SA)", "etc"};
        String []  optionExperience = {"0-1 Year","2-5 Year","6-10 Year","11+"};

        ArrayAdapter<String> arrayAdapterOccupation = new ArrayAdapter<String>(this,R.layout.options_spinner_item, optionOccupation);
        ArrayAdapter<String> arrayAdapterQualification = new ArrayAdapter<String>(this,R.layout.options_spinner_item, optionQualification);
        ArrayAdapter<String> arrayAdapterExperience = new ArrayAdapter<String>(this,R.layout.options_spinner_item, optionExperience);
        autoCompleteOccupation.setAdapter(arrayAdapterOccupation);
        autoCompleteQualification.setAdapter(arrayAdapterQualification);
        autoCompleteExperience.setAdapter(arrayAdapterExperience);
    }
    private void sendVerCodeToUserPhone(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                RegisterActivity3.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = task.getResult().getUser();
                        Common.currentUser =new UserModel();
                        Common.currentUser.setUid(user.getPhoneNumber());
                        if (login.equals("Register"))
                            createOrEditFirsestoreUser(Common.currentUser.getUid(),false);

                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Error sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    btn_login.setEnabled(true);
                });
    }

    private void createOrEditFirsestoreUser(String docId,boolean isLogin) {
        if (isLogin)
        {
            userRef.document(docId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists())
                    Toast.makeText(RegisterActivity3.this, "user found", Toast.LENGTH_SHORT).show();
                else
                {
                    UserModel u =new UserModel();
                    u.setUid(docId);
                    u.setPassword(password);
                    userRef.document(docId).set(u).addOnSuccessListener(aVoid -> Toast.makeText(RegisterActivity3.this, "new User added", Toast.LENGTH_SHORT).show());

                }
            })
                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity3.this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser != null) {
            sendUserToLogin();
        }
    }
    public void sendUserToLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private boolean validateFields(){
        if (!validateOccupation()|!validateQualification()|!validateExperience()){
            return false;
        }
        else
            return true;
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
}