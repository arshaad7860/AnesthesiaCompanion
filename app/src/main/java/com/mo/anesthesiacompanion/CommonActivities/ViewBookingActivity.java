package com.mo.anesthesiacompanion.CommonActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mo.anesthesiacompanion.Common.Common;
import com.mo.anesthesiacompanion.Doctor.ViewAppliedAnesthetistsListActivity;
import com.mo.anesthesiacompanion.Model.AnesthetistModel;
import com.mo.anesthesiacompanion.Model.AppliedModel;
import com.mo.anesthesiacompanion.Model.BookingModel;
import com.mo.anesthesiacompanion.Model.UserModel;
import com.mo.anesthesiacompanion.R;

public class ViewBookingActivity extends AppCompatActivity {

    FirebaseFirestore db;
    DocumentReference bookingsRef;
    MaterialButton btn_apply, btn_view_applied;
    BookingModel bookingModelPac;
    ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);
        Intent intentDash = getIntent();
        String bookingId = intentDash.getStringExtra("bookingId");

        bookingModelPac=intentDash.getParcelableExtra("bookingModel");

        db = FirebaseFirestore.getInstance();
        if (bookingId != null) {
            bookingsRef = db.collection("Booking").document(bookingId);
        }

        //hooks
        btn_apply = findViewById(R.id.btn_apply);
        btn_view_applied = findViewById(R.id.btn_view_applied);
        btn_back = findViewById(R.id.btn_back);

        if (!Common.currentUser.getOccupation().equals("Anesthetist")) {
            btn_apply.setVisibility(View.GONE);
            btn_view_applied.setVisibility(View.VISIBLE);
        } else {
            btn_apply.setVisibility(View.VISIBLE);
            btn_view_applied.setVisibility(View.GONE);
        }

        btn_apply.setOnClickListener(v -> {
            addNewAnesthetistToFireStore();
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        });

        btn_view_applied.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ViewAppliedAnesthetistsListActivity.class);
            intent.putExtra("bookingId", bookingId);
            String subCollectionName = "Anesthetists";
            intent.putExtra("subCollection", subCollectionName);
            startActivity(intent);
            finish();
        });

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        });

    }

    private void addNewAnesthetistToFireStore() {
        UserModel user = Common.currentUser;
        AnesthetistModel anesthetistModel = new AnesthetistModel(user.getUid(),user.getUid(),bookingModelPac.getBookingId()+user.getUid(), user.getPhone(), user.getEmail(), user.getFirstname(), user.getLastname(), user.getQualification(), user.getExperience(), user.getOccupation(), user.getHospital(), false,true);
        AppliedModel appliedModel = new AppliedModel(bookingModelPac.getBookingId(),user.getUid(),bookingModelPac.getBookingId()+user.getUid(),anesthetistModel.getaId(),bookingModelPac.getDrName(),bookingModelPac.getPhone(),bookingModelPac.getSurgeryType(),bookingModelPac.getDescription(),bookingModelPac.getHospitalName(),bookingModelPac.getRoom(),bookingModelPac.getSurgeryTime(),bookingModelPac.getSurgeryDate(),bookingModelPac.getAnestheticType(),bookingModelPac.getEmergency(),true,anesthetistModel.isAccepted(),anesthetistModel.isPending());
        bookingsRef.collection("Anesthetists").add(anesthetistModel).addOnSuccessListener(documentReference -> {
            bookingsRef.collection("Anesthetists").document(documentReference.getId()).update("aId", documentReference.getId());
            db.collection("applied").document(appliedModel.getAppliedId()).set(appliedModel).addOnSuccessListener(aVoid -> {
                db.collection("applied").document(appliedModel.getAppliedId()).update("anesthetistID",documentReference.getId());
                Toast.makeText(this, "applied successfully added", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> Toast.makeText(ViewBookingActivity.this, "applied failed to create"+e.getMessage(), Toast.LENGTH_SHORT).show());
            Toast.makeText(this, "Successfully applied for position", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e -> Toast.makeText(ViewBookingActivity.this, "could not add subCollection " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}