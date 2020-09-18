package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.mo.anesthesiacompanion.R;

import java.util.HashMap;
import java.util.Map;

public class SetNewPasswordActivity extends AppCompatActivity {

    MaterialButton btn_confirm;
    ImageView btn_set_pass_close;
    TextInputLayout txt_new_password,txt_confirm_password;
    String _newPassword;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //hooks
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_set_pass_close=findViewById(R.id.btn_set_pass_close);
        txt_new_password=findViewById(R.id.txt_new_password);
        txt_confirm_password=findViewById(R.id.txt_confirm_password);
        db = FirebaseFirestore.getInstance();

        btn_confirm.setOnClickListener(v -> {
            if (!validatePassword()|validatePasswordConfirm())
            {
                return;
            }
            else
            {
                _newPassword= txt_new_password.getEditText().getText().toString();
                String _phoneNumber=getIntent().getStringExtra("phone");
                Map<String,Object> newPass =new HashMap<>();
                newPass.put("password",_newPassword);
                //update data in firebase
                db.collection("users").document(_phoneNumber).set(newPass, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SetNewPasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),ForgotPasswordSuccessMessageActivity.class));
                        finish();
                        }
                }).addOnFailureListener(e -> Toast.makeText(SetNewPasswordActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show());

            }

        });
        btn_set_pass_close.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
    }

    private boolean validatePassword() {
        String val = txt_new_password.getEditText().getText().toString();

        if (val.isEmpty()) {
            txt_new_password.setError("Field Cannot Be Empty");
            return false;
        }
        else if (val.length()<6)
        {
            txt_new_password.setError("Password must be greater than 6 characters");
            return false;
        }
        else {
            txt_new_password.setError(null);
            txt_new_password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePasswordConfirm() {
        String val = txt_confirm_password.getEditText().getText().toString();
        String val2 = txt_new_password.getEditText().getText().toString();

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
}