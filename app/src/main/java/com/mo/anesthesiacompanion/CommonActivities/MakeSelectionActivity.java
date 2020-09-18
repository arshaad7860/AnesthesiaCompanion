package com.mo.anesthesiacompanion.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mo.anesthesiacompanion.R;

public class MakeSelectionActivity extends AppCompatActivity {

    Button btn_make_selection_phone;
    ImageView forgot_password_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_selection);

        //hooks

        btn_make_selection_phone=findViewById(R.id.btn_make_selection_phone);
        forgot_password_back_arrow=findViewById(R.id.forgot_password_back_arrow);

        btn_make_selection_phone.setOnClickListener(v -> startActivity(new Intent(MakeSelectionActivity.this,ForgotPasswordPhoneActivity.class)));
        forgot_password_back_arrow.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
    }
}