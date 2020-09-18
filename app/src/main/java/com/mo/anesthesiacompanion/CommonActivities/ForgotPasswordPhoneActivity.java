package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;
import com.mo.anesthesiacompanion.R;

public class ForgotPasswordPhoneActivity extends AppCompatActivity {

    MaterialButton btn_next;
    ImageView forgot_password_back_arrow;
    CountryCodePicker btn_forgot_password_ccp;
    TextInputLayout txt_phone;
    FirebaseFirestore db;
    CollectionReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_phone);

        //hooks
        btn_next=findViewById(R.id.btn_next);
        forgot_password_back_arrow=findViewById(R.id.forgot_password_back_arrow);
        btn_forgot_password_ccp=findViewById(R.id.btn_forgot_password_ccp);
        txt_phone=findViewById(R.id.txt_phone);
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        btn_next.setOnClickListener(v -> {
            if (!validatePhone())
            {
                Toast.makeText(this, "Enter valid Phone number", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //get data
                String _phoneNumber = txt_phone.getEditText().getText().toString().trim();
                String _countryCode = btn_forgot_password_ccp.getSelectedCountryCodeWithPlus();

                String _completePhoneNumber = _countryCode + _phoneNumber;

                Query checkUserQuery = userRef.whereEqualTo("uid", _completePhoneNumber);
                checkUserQuery.get().addOnCompleteListener(task -> {
                    txt_phone.setError(null);
                    txt_phone.setErrorEnabled(false);
                    Toast.makeText(this, "user found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),VerifyOTPActivity.class);
                    intent.putExtra("phone",_completePhoneNumber);
                    intent.putExtra("whatToDo","updateData");
                    startActivity(intent);
                    finish();

                }).addOnFailureListener(e -> txt_phone.setError("No such user"));

                }

        });

        forgot_password_back_arrow.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),MakeSelectionActivity.class));
            finish();
        });

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
    
}