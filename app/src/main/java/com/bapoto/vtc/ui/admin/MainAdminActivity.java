package com.bapoto.vtc.ui.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityMainAdminBinding;
import com.bapoto.vtc.adapters.ReservationAdapter;
import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class MainAdminActivity extends AppCompatActivity {

    private ActivityMainAdminBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private ReservationAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setListeners() {

        binding.imageOpenChat.setOnClickListener(view -> {startChatActivity();});
        binding.rideAccepted.setOnClickListener(view -> {startRideAcceptedActivity();});
        binding.rideFinished.setOnClickListener(view -> {startRideFinishedActivity();});

    }

    private void startChatActivity(){
        Intent intent = new Intent(this,ChatAdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void startRideAcceptedActivity(){
        Intent intent = new Intent(this,RideAcceptedActivity.class);
        startActivity(intent);
        finish();
    }
    private void startRideFinishedActivity(){
        Intent intent = new Intent(this,RideFinishedActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        Query query = reservationRef
                .whereEqualTo(Constants.IS_ACCEPTED,false)
                .whereEqualTo(Constants.KEY_IS_FINISHED,false);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query,Reservation.class)
                .build();

        adapter = new ReservationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.reservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener((documentSnapshot, position) -> alertAcceptRide());

    }

    private void alertAcceptRide() {
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            AtomicReference<String> docId = new AtomicReference<>(documentSnapshot.getId());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            PreferenceManager preferenceManager = new PreferenceManager(this);
            // set title
            alertDialogBuilder.setTitle("RÉSERVATION");
            alertDialogBuilder.setIcon(R.drawable.ic_thumb_up);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Accepter cette réservation ?")
                    .setCancelable(false)
                    .setPositiveButton("Oui !", (dialog, id) -> {
                        {
                            docId.set(documentSnapshot.getId());
                            updateRide(docId);

                        }
                    })
                    .setNegativeButton("Non", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
    }

    // ASSIGNER RESERVATION COMME ACCEPTEE
    private void updateRide(AtomicReference<String> pathId) {

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(String.valueOf(pathId));
        documentReference.update(Constants.IS_ACCEPTED,true);
        documentReference.update(Constants.KEY_ACCEPTED_THE,new Date());
    }



}