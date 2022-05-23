package com.bapoto.vtc.ui;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.adapters.ReservationAdapter;
import com.bapoto.vtc.manager.ReservationManager;
import com.bapoto.vtc.model.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AllReservation extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reservations");
    private  ReservationAdapter reservationAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String uId;
    {
        assert user != null;
        uId = user.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_all_reservation);

        setUpRecyclerView();
    }

    @SuppressLint("LongLogTag")
    private void setUpRecyclerView() {
        ReservationManager reservationManager = ReservationManager.getInstance();
        reservationManager.getAllUserReservation();


      Query query = db.collection("reservations");


      query.get()
              .addOnCompleteListener(task -> {
                  if (task.isSuccessful()) {
                      for (QueryDocumentSnapshot document : task.getResult()) {
                          Log.d(TAG, document.getId() + " => " + document.getData());
                      }
                  } else {
                      Log.d(TAG, "Error getting documents: ", task.getException());
                  }
              });

                  FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                          .setQuery(query, Reservation.class)
                          .build();

                  reservationAdapter = new ReservationAdapter(options);

                  RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new

                  LinearLayoutManager(this));
        recyclerView.setAdapter(reservationAdapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
        reservationAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        reservationAdapter.stopListening();
    }
}