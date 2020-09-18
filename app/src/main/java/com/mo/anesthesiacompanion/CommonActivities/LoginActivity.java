package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.hbb20.CountryCodePicker;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Common.SessionManager;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.NewVerifyOtpActivity;
import com.mo.anesthesiacompanion.R;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton btn_login, btn_register, btn_forgot_password_login;
    private TextInputLayout txt_phone, txt_password;
    private TextInputEditText edt_phone, edt_password;
    private CountryCodePicker btn_login_ccp;
    FirebaseFirestore db;
    CollectionReference userRef;
    MaterialCheckBox chk_remember_me_login;
    SessionManager sessionManager;
    String _completePhoneNumber;
    String password;
    String login= "Login";

    FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");
        btn_forgot_password_login.setEnabled(false);

        createMyViews();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        /*sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBER_ME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            edt_phone.setText(rememberMeDetails.get(SessionManager.KEY_PHONE_REMEMBER_ME));
            edt_password.setText(rememberMeDetails.get(SessionManager.KEY_PASSWORD_REMEMBER_ME));
        }*/

        //btn click events
        btn_register.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });

        btn_login.setOnClickListener(view -> {
          /*  if (!isConnected(this)) {
                showCustomDialog();
            }
            else {
                //get data

                if (!validatePhone() | !validatePassword()) {
                    return;
                } else {
                    //get data
                    String _phoneNumber = txt_phone.getEditText().getText().toString().trim();
                    String _password = txt_password.getEditText().getText().toString();
                    String _countryCode = btn_login_ccp.getSelectedCountryCodeWithPlus();

                    _completePhoneNumber = _countryCode + _phoneNumber;
                    if (chk_remember_me_login.isChecked()) {
                        sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBER_ME);
                        sessionManager.createRememberMeSession(_phoneNumber, _password);
                    }

                    btn_login.setEnabled(false);
                    setCommonUser(_completePhoneNumber);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            _completePhoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LoginActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks

                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            setCommonUser(_completePhoneNumber);
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(LoginActivity.this, "Phone verification Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            btn_login.setEnabled(true);
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent otpIntent = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                                    otpIntent.putExtra("AuthCredentials", s);
                                    String comingActivity = "Login";
                                    otpIntent.putExtra("comingActivity", comingActivity);
                                    startActivity(otpIntent);
                                    finish();
                                }
                            }, 5000);
                        }
                    };

                }
            }*/
            String phoneNumber =btn_login_ccp.getSelectedCountryCodeWithPlus()+ txt_phone.getEditText().getText().toString().trim();
            password= txt_password.getEditText().getText().toString();
            if (!validateFields()){
                Toast.makeText(this, "Enter Valid Fields", Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                btn_login.setEnabled(false);
                sendVerCodeToUserPhone(phoneNumber);
            }
        });
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "Verification Failed Try Again", Toast.LENGTH_SHORT).show();
                btn_login.setEnabled(true);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent otpIntent = new Intent(LoginActivity.this, NewVerifyOtpActivity.class);
                        otpIntent.putExtra("AuthCredentials",s);
                        otpIntent.putExtra("password",password);
                        otpIntent.putExtra("fromIntent","Login");
                        otpIntent.putExtra("extraCheck","login");
                        startActivity(otpIntent);
                    }
                },3000);
            }
        };

        btn_forgot_password_login.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), MakeSelectionActivity.class)));
    }

    private void createMyViews() {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_forgot_password_login = findViewById(R.id.btn_forgot_password_login);
        txt_phone = findViewById(R.id.txt_phone);
        txt_password = findViewById(R.id.txt_password);
        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        btn_login_ccp = findViewById(R.id.btn_login_ccp);
        chk_remember_me_login = findViewById(R.id.chk_remember_me_login);
    }

    private void sendVerCodeToUserPhone(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                LoginActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = task.getResult().getUser();
                        Common.currentUser =new UserModel();
                        Common.currentUser.setUid(user.getPhoneNumber());
                        if (login.equals("Login"))
                            createOrEditFirsestoreUser(Common.currentUser.getUid(),true);

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
                    Toast.makeText(LoginActivity.this, "user found", Toast.LENGTH_SHORT).show();
                else
                {
                    UserModel u =new UserModel();
                    u.setUid(docId);
                    u.setPassword(password);
                    userRef.document(docId).set(u).addOnSuccessListener(aVoid -> Toast.makeText(LoginActivity.this, "new User added", Toast.LENGTH_SHORT).show());

                }
            })
                    .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else {
            Toast.makeText(this, "register", Toast.LENGTH_SHORT).show();
        }
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

    private boolean validateFields(){
        if (!validatePhone()|!validatePassword()){
            return false;
        }
        else
            return true;
    }

    private boolean validatePhone() {
        String val = txt_phone.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_phone.setError("Field cannot be empty");
            return false;
        } else if (!(val.length() == 9)) {
            txt_phone.setError("Please Enter A Valid 9 Digit Cellphone Number ");
            return false;
        } else {
            txt_phone.setError(null);
            txt_phone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = txt_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            txt_password.setError("Field Cannot Be Empty");
            return false;
        } else if (val.length() < 6) {
            txt_password.setError("Password must be greater than 6 characters");
            return false;
        } else {
            txt_password.setError(null);
            txt_password.setErrorEnabled(false);
            return true;
        }
    }
}