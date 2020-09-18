package com.mo.anesthesiacompanion.Anesthetist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Adapter.AppliedAdapter;
import com.mo.anesthesiacompanion.CommonActivities.DashboardActivity;
import com.mo.anesthesiacompanion.Model.AppliedModel;
import com.mo.anesthesiacompanion.R;

public class ViewMyAppliedActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference appliedRef;
    MaterialButton btn_cancel;
    String appliedId;
    private TextView txt_phone, txt_dr_name, txt_surgery_type, txt_hospital_name, txt_anesthesia_type, txt_surgery_time, txt_surgery_date, txt_emergency, txt_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_applied);
        btn_cancel = findViewById(R.id.btn_cancel);
        Intent prevIntent = getIntent();
        String approvedActivity = prevIntent.getStringExtra("prevActivity");
        appliedId = prevIntent.getStringExtra("appliedId");
        if (approvedActivity!=null) {
            if (approvedActivity.equals("true")) {
                btn_cancel.setVisibility(View.VISIBLE);
            } else if (approvedActivity.equals("false")) {
                btn_cancel.setVisibility(View.GONE);
            }
        }
        db = FirebaseFirestore.getInstance();
        appliedRef = db.collection("applied");

        loadMyViews();
        btn_cancel.setOnClickListener(v -> {
            cancelMyBooking();
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        });
    }

    private void loadMyViews() {
        txt_dr_name = findViewById(R.id.txt_dr_name);
        txt_phone = findViewById(R.id.txt_phone);
        txt_surgery_type = findViewById(R.id.txt_surgery_type);
        txt_hospital_name = findViewById(R.id.txt_hospital_name);
        txt_anesthesia_type = findViewById(R.id.txt_anesthesia_type);
        txt_status = findViewById(R.id.txt_status);
        txt_surgery_time = findViewById(R.id.txt_surgery_time);
        txt_surgery_date = findViewById(R.id.txt_surgery_date);
        txt_emergency = findViewById(R.id.txt_emergency);

        appliedRef.document(appliedId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                AppliedModel model = documentSnapshot.toObject(AppliedModel.class);
                if (model != null) {
                    txt_dr_name.setText(model.getDrName());
                    txt_phone.setText(model.getPhone());
                    txt_anesthesia_type.setText(model.getAnestheticType());
                    txt_hospital_name.setText(model.getHospitalName());

                    txt_surgery_date.setText(model.getSurgeryDate());
                    txt_surgery_time.setText(model.getSurgeryTime());
                    txt_surgery_type.setText(model.getSurgeryType());
                    if (model.getEmergency()) {
                        txt_emergency.setText("EMERGENCY");
                        txt_emergency.setTextColor(Color.RED);
                    } else {
                        txt_emergency.setText("No");
                        txt_emergency.setTextColor(Color.BLUE);
                    }

                    if (model.getPending() && (!model.getApproved()) && (model.getApplied())) {
                        txt_status.setText("Pending");
                        txt_status.setTextColor(Color.BLUE);
                    } else if (model.getPending() && (model.getApproved()) && (model.getApplied())) {
                        txt_status.setText("Accepted");
                        txt_status.setTextColor(Color.parseColor("#228B22"));
                    } else if (!model.getPending() && !(model.getApproved()) && (model.getApplied())) {
                        txt_status.setText("Rejected");
                        txt_status.setTextColor(Color.RED);
                    } else {
                        txt_status.setText("Canceled");
                        txt_status.setTextColor(Color.RED);
                    }
                } else {
                    Toast.makeText(this, "Error Model is Null", Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this, "document does not exist", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error could not populate views " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void cancelMyBooking() {
        appliedRef.document(appliedId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                AppliedModel model = documentSnapshot.toObject(AppliedModel.class);
                if (model != null) {
                    appliedRef.document(appliedId).update("applied", false);
                    appliedRef.document(appliedId).update("approved", false);
                    appliedRef.document(appliedId).update("pending", false);
                    db.collection("Booking").document(model.getBookingId()).collection("Anesthetists").document(model.getAnesthetistID()).update("accepted", true);
                    db.collection("Booking").document(model.getBookingId()).collection("Anesthetists").document(model.getAnesthetistID()).update("pending", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ViewMyAppliedActivity.this, "Cancellation completed successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "failed to update fields" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                } else
                    Toast.makeText(this, "Error Model is Null", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "document does not exist", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(ViewMyAppliedActivity.this, "Cancellation Failed Error " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}