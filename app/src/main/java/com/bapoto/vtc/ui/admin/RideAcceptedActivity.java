package com.bapoto.vtc.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityMainAdminBinding;
import com.bapoto.bapoto.databinding.ActivityRideAcceptedBinding;
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

public class RideAcceptedActivity extends AppCompatActivity {

    private ActivityRideAcceptedBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private ReservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRideAcceptedBinding.inflate(getLayoutInflater());
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


    private void setListeners() {
        binding.rideFinished.setOnClickListener(view -> {startRideFinishedActivity();});
        binding.imageOpenChat.setOnClickListener(view -> {startChatActivity();});
        binding.allRide.setOnClickListener(view -> {startMainActivity();});
    }

    private void startRideFinishedActivity(){
        Intent intent = new Intent(this,RideFinishedActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMainActivity(){
        Intent intent = new Intent(this,MainAdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void startChatActivity(){
        Intent intent = new Intent(this,ChatAdminActivity.class);
        startActivity(intent);
        finish();
    }


    private void setupRecyclerView() {

        Query query = reservationRef.whereEqualTo(Constants.IS_ACCEPTED,true)
                .whereEqualTo(Constants.KEY_IS_FINISHED,false);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query,Reservation.class)
                .build();

        adapter = new ReservationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.acceptedRideRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.markAsFinished(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener((documentSnapshot, position) -> alertFinishRide());
    }

    private void alertFinishRide() {
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            AtomicReference<String> docId = new AtomicReference<>(documentSnapshot.getId());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            PreferenceManager preferenceManager = new PreferenceManager(this);
            // set title
            alertDialogBuilder.setTitle("RÉSERVATION");
            alertDialogBuilder.setIcon(R.drawable.ic_thumb_up);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Course terminée ?")
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

    // UPDATE RESERVATION COMME TERMINEE
    private void updateRide(AtomicReference<String> pathId) {

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(String.valueOf(pathId));

        documentReference.update(Constants.KEY_ACCEPTED_THE,null);
        documentReference.update(Constants.KEY_IS_FINISHED,true);
        documentReference.update(Constants.KEY_FINISHED_THE,new Date());
    }

}