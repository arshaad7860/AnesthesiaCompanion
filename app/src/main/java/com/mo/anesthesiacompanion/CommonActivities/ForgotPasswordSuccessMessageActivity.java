package com.mo.anesthesiacompanion.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.mo.anesthesiacompanion.R;

public class ForgotPasswordSuccessMessageActivity extends AppCompatActivity {

    MaterialButton btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_success_message);

        //hooks
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        });
    }
}