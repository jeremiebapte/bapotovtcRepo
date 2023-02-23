package com.bapoto.vtc.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.databinding.ActivityAllRideUserBinding;
import com.bapoto.vtc.adapters.AllRideUserAdapter;
import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AllRideUserActivity extends AppCompatActivity {
    private ActivityAllRideUserBinding binding;
    private PreferenceManager preferenceManager;
    private AllRideUserAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRideUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        setListeners();
        setupRecyclerView();
    }


    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());

    }


    private void setupRecyclerView() {
        Query query = reservationRef.whereEqualTo(Constants.KEY_IS_FINISHED, true)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));


        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new AllRideUserAdapter(options);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = binding.allRideRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnTextViewClickListener(((documentSnapshot, positon) -> navigateToInvoice()));

    }

    private void navigateToInvoice() {
        adapter.setOnTextViewClickListener((documentSnapshot, positon) -> {
            AtomicReference<String> docid = new AtomicReference<>(documentSnapshot.getId());
            Reservation resa = documentSnapshot.toObject(Reservation.class);

            String name = Objects.requireNonNull(resa).getName();
            String date = resa.getDate();
            String price = String.valueOf(resa.getPrice());
            String pickUp = resa.getPickUp();
            String dropOff = resa.getDropOff();
            Intent intent = new Intent(AllRideUserActivity.this,InvoiceActivity.class);

            intent.putExtra("name",name);
            intent.putExtra("date",date);
            intent.putExtra("price",price);
            intent.putExtra("pickUp",pickUp);
            intent.putExtra("dropOff",dropOff);


            startActivity(intent);
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

}