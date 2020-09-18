package com.mo.anesthesiacompanion.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.CommonActivities.DashboardActivity;
import com.mo.anesthesiacompanion.Model.AnesthetistModel;
import com.mo.anesthesiacompanion.R;

public class ApplicantDetailsActivity extends AppCompatActivity {
    MaterialButton btn_approve, btn_reject, btn_cancel;
    String bookingId, subCollection, path, anesthetistId;
    FirebaseFirestore db;
    DocumentReference bookingsRef;
    AnesthetistModel anesthetistModel;
    private TextView txt_first_name, txt_last_name, txt_phone, txt_email, txt_required_qualification, txt_experience, txt_occupation, txt_hospital_name, txt_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_details);
        db = FirebaseFirestore.getInstance();
        Intent intentFrmList = getIntent();
        bookingId = intentFrmList.getStringExtra("bookingId");
        subCollection = intentFrmList.getStringExtra("subCollection");
        path = intentFrmList.getStringExtra("path");
        anesthetistId = intentFrmList.getStringExtra("anesthetistId");

        anesthetistModel = new AnesthetistModel();
        if (bookingId != null) {
            bookingsRef = db.collection("Booking").document(bookingId);


        } else {
            Toast.makeText(this, "bookingID=null", Toast.LENGTH_SHORT).show();
        }
        loadApplicantDetails();

        btn_approve = findViewById(R.id.btn_approve);
        btn_reject = findViewById(R.id.btn_reject);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_approve.setOnClickListener(v -> {
            approveApplicant();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        });
        btn_reject.setOnClickListener(v -> {

            rejectApplicant();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        });

        btn_cancel.setOnClickListener(v -> {

            cancelApplicant();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        });
    }

    private void approveApplicant() {
        bookingsRef.collection("Anesthetists").document(anesthetistId).get().addOnSuccessListener(documentSnapshot -> {
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("accepted", true);
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("pending", true);
            AnesthetistModel anModel =documentSnapshot.toObject(AnesthetistModel.class);
            if (anModel!=null){
            db.collection("applied").document(anModel.getAppliedId()).update("approved",true);
                db.collection("applied").document(anModel.getAppliedId()).update("applied",true);
                db.collection("applied").document(anModel.getAppliedId()).update("pending",true);
            }
            loadApplicantDetails();
            Toast.makeText(ApplicantDetailsActivity.this, "Applicant Approved", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "cant approve applicant " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void rejectApplicant() {
        bookingsRef.collection("Anesthetists").document(anesthetistId).get().addOnSuccessListener(documentSnapshot -> {
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("accepted", false);
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("pending", false);
            AnesthetistModel anModel =documentSnapshot.toObject(AnesthetistModel.class);
            if (anModel!=null){
                db.collection("applied").document(anModel.getAppliedId()).update("approved",false);
                db.collection("applied").document(anModel.getAppliedId()).update("applied",true);
                db.collection("applied").document(anModel.getAppliedId()).update("pending",false);
            }
            loadApplicantDetails();
            Toast.makeText(ApplicantDetailsActivity.this, "Applicant Approved", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "cant approve applicant " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    private void cancelApplicant() {
        bookingsRef.collection("Anesthetists").document(anesthetistId).get().addOnSuccessListener(documentSnapshot -> {
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("accepted", true);
            bookingsRef.collection("Anesthetists").document(anesthetistId).update("pending", false);
            AnesthetistModel anModel =documentSnapshot.toObject(AnesthetistModel.class);
            if (anModel!=null){
                db.collection("applied").document(anModel.getAppliedId()).update("approved",false);
                db.collection("applied").document(anModel.getAppliedId()).update("applied",false);
                db.collection("applied").document(anModel.getAppliedId()).update("pending",false);
            }
            loadApplicantDetails();
            Toast.makeText(ApplicantDetailsActivity.this, "Applicant Approved", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "cant approve applicant " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadApplicantDetails() {
        bookingsRef.collection("Anesthetists").document(anesthetistId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        anesthetistModel = documentSnapshot.toObject(AnesthetistModel.class);
                        if (anesthetistModel != null) {
                            setTextViewDetails();
                            Toast.makeText(this, "got data class successfully", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "model =null: " + anesthetistModel.getEmail(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Doc doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ApplicantDetailsActivity.this, "failed to load applicant details " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    private void setTextViewDetails() {
        txt_first_name = findViewById(R.id.txt_first_name);
        txt_last_name = findViewById(R.id.txt_last_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_email = findViewById(R.id.txt_email);
        txt_required_qualification = findViewById(R.id.txt_required_qualification);
        txt_experience = findViewById(R.id.txt_experience);
        txt_occupation = findViewById(R.id.txt_occupation);
        txt_hospital_name = findViewById(R.id.txt_hospital_name);
        txt_status = findViewById(R.id.txt_status);
        AnesthetistModel newModel =anesthetistModel;
        txt_first_name.setText(newModel.getFirstName());
        txt_last_name.setText(newModel.getLastName());
        txt_phone.setText(newModel.getPhone());
        txt_email.setText(newModel.getEmail());
        txt_required_qualification.setText(newModel.getQualification());
        txt_experience.setText(newModel.getExperience());
        txt_occupation.setText(newModel.getOccupation());
        txt_hospital_name.setText(newModel.getHospital());
        if (newModel.isPending() && (!newModel.isAccepted())) {
            txt_status.setText("Pending");
            txt_status.setTextColor(Color.BLUE);
        } else if (newModel.isPending() && (newModel.isAccepted())) {
            txt_status.setText("Accepted");
            txt_status.setTextColor(Color.parseColor("#228B22"));
        } else if (!newModel.isPending() && !(newModel.isAccepted())){
            txt_status.setText("Rejected/Cancelled");
            txt_status.setTextColor(Color.RED);
        }
        else {
            txt_status.setText("Canceled");
            txt_status.setTextColor(Color.RED);
        }

    }
}