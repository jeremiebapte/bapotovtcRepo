package com.bapoto.vtc.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.model.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class AllReservationFragment extends Fragment {
    private View AllReservationList;
    private RecyclerView myReservationList;
    private DatabaseReference reservationRef, userRef;
    private FirebaseAuth mAuth;
    private String currentUserID;


    public AllReservationFragment() {
        // Required empty public constructor
    }

   public static AllReservationFragment newInstance() {
        return (new AllReservationFragment());
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AllReservationList =  inflater.inflate(R.layout.fragment_all_reservation, container, false);

        RecyclerView myReservationList = new RecyclerView(this.getContext());
        myReservationList = (RecyclerView) myReservationList.findViewById(R.id.reservation_list);

        myReservationList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

         reservationRef = FirebaseDatabase.getInstance().getReference().child("reservations").child(currentUserID);
         userRef = FirebaseDatabase.getInstance().getReference().child("users");

        return AllReservationList;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                //.setQuery(reservationRef, Reservation.class)
                .build();

        FirestoreRecyclerAdapter<Reservation, ReservationViewHolder> adapter = new FirestoreRecyclerAdapter<Reservation, ReservationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReservationViewHolder holder, int position, @NonNull Reservation model) {

                String userIDs = reservationRef.getKey();
                userRef.child(userIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("pickUp")){
                            String userPickUp = snapshot.child("pickUp").getValue().toString();
                            String userDestination = snapshot.child("destination").getValue().toString();
                            String userHour = snapshot.child("hour").getValue().toString();
                            String userDate = snapshot.child("date").getValue().toString();

                            holder.pickUp.setText(userPickUp);
                            holder.destination.setText(userDestination);
                            holder.hour.setText(userHour);
                            holder.date.setText(userDate);


                        }else {
                            String userPickUp = snapshot.child("pickUp").getValue().toString();
                            String userDestination = snapshot.child("destination").getValue().toString();
                            String userHour = snapshot.child("hour").getValue().toString();
                            String userDate = snapshot.child("date").getValue().toString();

                            holder.pickUp.setText(userPickUp);
                            holder.destination.setText(userDestination);
                            holder.hour.setText(userHour);
                            holder.date.setText(userDate);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation,parent,false);
               ReservationViewHolder viewHolder = new ReservationViewHolder(v);
               return viewHolder;
            }
        };


        myReservationList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView pickUp,destination,hour,date;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);

            pickUp = itemView.findViewById(R.id.tvpickUp);
            destination = itemView.findViewById(R.id.tvDestination);
            hour = itemView.findViewById(R.id.tvHour);
            date = itemView.findViewById(R.id.tvDate);

        }
    }
}