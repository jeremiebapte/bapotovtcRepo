package com.bapoto.vtc.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityMainAdminBinding;
import com.bapoto.vtc.adapters.ReservationAdapter;
import com.bapoto.vtc.model.Admin;
import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class MainAdminActivity extends AppCompatActivity {

    private ActivityMainAdminBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private ReservationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Admin admin = new Admin();
        try {
            suscribeToTopic();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        setListeners();


        setupRecyclerView();
    }

    private void suscribeToTopic() throws JSONException, IOException {
        FirebaseMessaging.getInstance().subscribeToTopic("admin_channel")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Succès de l'abonnement aux événements d'inscription des chauffeurs");
                    } else {
                        System.out.println("Échec de l'abonnement aux événements d'inscription des chauffeurs");
                    }
                });
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