package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.R;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class TestActivity extends AppCompatActivity {

    FirebaseFirestore db;
    CollectionReference userRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        getUserDetails();

        /*if (userModel!=null){
            Common.currentUser=userModel;
        }
        else
        {
            getUserDetails();
        }*/


        /*UserModel u =new UserModel();
        Common.currentUser=u;

            if (!Common.currentUser.getUid().equals(mCurrentUser.getPhoneNumber())) {

                userRef.document(Objects.requireNonNull(mCurrentUser.getPhoneNumber())).get().addOnSuccessListener(documentSnapshot -> Common.currentUser = documentSnapshot.toObject(UserModel.class));
                sendUserToHome();
                Toast.makeText(this, "current common user empty", Toast.LENGTH_SHORT).show();
            } else {
                userRef.document(Objects.requireNonNull(mCurrentUser.getPhoneNumber())).get().addOnSuccessListener(documentSnapshot -> Common.currentUser = documentSnapshot.toObject(UserModel.class));
                sendUserToHome();
            }
*/

    }

    private void getUserDetails() {

        userRef.document((mCurrentUser.getPhoneNumber())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();

                    if (documentSnapshot.exists());
                    {
                        userModel=documentSnapshot.toObject(UserModel.class);
                        Common.currentUser=userModel;
                        sendUserToHome();
                    }
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentUser == null) {
            sendUserToLogin();
        }
        else {
            if (userModel!=null){
                Common.currentUser=userModel;
                sendUserToHome();
            }
            else
            {
                getUserDetails();
            }
        }
    }

    public void sendUserToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    public void sendUserToHome() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}