package com.mo.anesthesiacompanion.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.mo.anesthesiacompanion.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    MaterialButton btn_next, btn_login;
    ImageView btn_back_register;
    CountryCodePicker country_code_picker;
    TextInputLayout txt_confirm_password, txt_password, txt_email, txt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //hooks
        btn_login = findViewById(R.id.btn_login);
        btn_next = findViewById(R.id.btn_next);
        btn_back_register = findViewById(R.id.btn_back_register);
        country_code_picker = findViewById(R.id.country_code_picker);


        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txt_password = findViewById(R.id.txt_password);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phone);

        //click btn events
        btn_login.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
        btn_back_register.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    public void onNextBtnClick(View view) {

        if (!validatePhone()| !validateEmail()| !validatePassword()|!validatePasswordConfirm()){
            Toast.makeText(this, "Please fill in your credentials", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), RegisterActivity2.class);
            String txt_phone_val = txt_phone.getEditText().getText().toString().trim();
            String countrycode= country_code_picker.getSelectedCountryCodeWithPlus().toString();
            StringBuilder builder = new StringBuilder(countrycode).append(txt_phone_val);
            String txt_email_val = txt_email.getEditText().getText().toString().trim();
            String txt_password_val = txt_password.getEditText().getText().toString().trim();
            intent.putExtra("phone",builder.toString());
            intent.putExtra("email",txt_email_val);
            intent.putExtra("password",txt_password_val);
            startActivity(intent);

        }
    }

    //validate

    private boolean validatePhone() {
        String val = txt_phone.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            txt_phone.setError("Field cannot be empty");
            return false;
        } else {
            txt_phone.setError(null);
            txt_phone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = txt_email.getEditText().getText().toString().trim();
        String emailPattern = "^[_A-Za-z0-9-]+(\\\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)*(\\\\.[A-Za-z]{2,})$";
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

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
    //end validation


}