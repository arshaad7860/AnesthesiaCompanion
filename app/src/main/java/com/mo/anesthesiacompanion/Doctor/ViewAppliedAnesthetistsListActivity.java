package com.mo.anesthesiacompanion.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mo.anesthesiacompanion.Adapter.AnesthetistsAdapter;
import com.mo.anesthesiacompanion.Model.AnesthetistModel;
import com.mo.anesthesiacompanion.R;

public class ViewAppliedAnesthetistsListActivity extends AppCompatActivity {

    AnesthetistsAdapter adapter;
    FirebaseFirestore db;
    CollectionReference bookingsRef;
    String bookingId;
    String subCollectionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applied_anesthetists_list);
        Intent bookingIntent=getIntent();
         bookingId=bookingIntent.getStringExtra("bookingId");
         subCollectionName=bookingIntent.getStringExtra("subCollection");
        db =FirebaseFirestore.getInstance();
        bookingsRef= db.collection("Booking");
        setupRecyclerView();

        adapter.startListening();
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            String id= documentSnapshot.getId();
            String path= documentSnapshot.getReference().getId();
            Intent intent = new Intent(getApplicationContext(), ApplicantDetailsActivity.class);
            String bId= bookingId;

            intent.putExtra("bookingId",bId);
            intent.putExtra("subCollection",subCollectionName);
            intent.putExtra("path",path);
            intent.putExtra("anesthetistId",id);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        Query query = bookingsRef.document(bookingId).collection(subCollectionName);
        FirestoreRecyclerOptions<AnesthetistModel> options =new FirestoreRecyclerOptions.Builder<AnesthetistModel>()
                .setQuery(query,AnesthetistModel.class)
                .build();
        adapter=new AnesthetistsAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recycler_anesthetist_list);


        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        //adapter.startListening();
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}