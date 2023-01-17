package com.bapoto.vtc.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityRideFinishedBinding;
import com.bapoto.vtc.adapters.ReservationAdapter;
import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.utilities.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class RideFinishedActivity extends AppCompatActivity {

    private ActivityRideFinishedBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private ReservationAdapter adapter;
    private String m_Text = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRideFinishedBinding.inflate(getLayoutInflater());
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
        binding.imageOpenChat.setOnClickListener(view -> startChatActivity());
        binding.rideAccepted.setOnClickListener(view -> {
            startRideAcceptedActivity();
        });
    }

    private void startRideAcceptedActivity() {
        Intent intent = new Intent(this, RideAcceptedActivity.class);
        startActivity(intent);
        finish();
    }

    private void startChatActivity() {
        Intent intent = new Intent(this, ChatAdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        Query query = reservationRef.whereEqualTo(Constants.IS_ACCEPTED, true)
                .whereEqualTo(Constants.KEY_IS_FINISHED, true);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new ReservationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rideFinishedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> addPriceToTheRide());


    }

    private void addPriceToTheRide() {
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            AtomicReference<String> docid = new AtomicReference<>(documentSnapshot.getId());
            Reservation resa = documentSnapshot.toObject(Reservation.class);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Montant pour la réservation du: " + Objects.requireNonNull(resa).getDate());

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                m_Text = input.getText().toString();
                int result = Integer.parseInt(m_Text);

                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(String.valueOf(docid))
                        .update(Constants.KEY_PRICE, result);


            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }
}
