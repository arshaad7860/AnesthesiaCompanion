package com.mo.anesthesiacompanion.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;
;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mo.anesthesiacompanion.R;

public class RegisterActivity2 extends AppCompatActivity {
    MaterialButton btn_next, btn_login;
    ImageView btn_back_register;
    TextInputLayout txt_first_name,txt_last_name,txt_address,txt_suburb,txt_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //hooks
        btn_login =findViewById(R.id.btn_login);
        btn_next =findViewById(R.id.btn_next);
        btn_back_register =findViewById(R.id.btn_back_register);

        txt_first_name =findViewById(R.id.txt_first_name);
        txt_last_name =findViewById(R.id.txt_last_name);
        txt_address =findViewById(R.id.txt_address);
        txt_suburb =findViewById(R.id.txt_suburb);
        txt_city =findViewById(R.id.txt_city);

        //get values from prev activity
        Intent intentReg1 = getIntent();
        String phone_val = intentReg1.getStringExtra("phone");
        String email_val = intentReg1.getStringExtra("email");
        String password_val = intentReg1.getStringExtra("password");

        //click btn events
        btn_login.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
        btn_next.setOnClickListener(view -> {


            //validate
            if (!validateFirstName()|!validateLastName()|!validateAddress()|!validateSuburb()|!validateCity())
            {
                Toast.makeText(this, "Please fill in your credentials", Toast.LENGTH_SHORT).show();
            }
            else {

                String val = txt_first_name.getEditText().getText().toString().trim();
                String val1 = txt_last_name.getEditText().getText().toString().trim();
                String val2 = txt_address.getEditText().getText().toString();
                String val3 = txt_suburb.getEditText().getText().toString();
                String val4 = txt_city.getEditText().getText().toString();

                Intent intent = new Intent(getApplicationContext(),RegisterActivity3.class);
                intent.putExtra("phone",phone_val);
                intent.putExtra("email",email_val);
                intent.putExtra("password",password_val);
                intent.putExtra("firstname",val);
                intent.putExtra("lastname",val1);
                intent.putExtra("address",val2);
                intent.putExtra("suburb",val3);
                intent.putExtra("city",val4);
                startActivity(intent);

            }

        });

        btn_back_register.setOnClickListener(v -> {
               startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
               finish();
        });
    }

    //validate
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

    //end validation
}