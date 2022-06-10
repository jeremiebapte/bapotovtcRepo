package com.bapoto.vtc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.model.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ReservationAdapter extends FirestoreRecyclerAdapter <Reservation, ReservationAdapter.ReservationHolder> {


    public ReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationHolder holder, int position, @NonNull Reservation model) {
        holder.tvPickUP.setText(model.getPickUp());
        holder.tvDestination.setText(model.getDestination());
        holder.tvHour.setText(model.getHour());
        holder.tvDate.setText(model.getDate());
    }

    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation,
                parent,false);
        return new ReservationHolder(v);
    }

    static class ReservationHolder extends RecyclerView.ViewHolder {
        TextView tvPickUP;
        TextView tvDestination;
        TextView tvDate;
        TextView tvHour;


        public ReservationHolder(@NonNull View itemView) {
            super(itemView);

            tvPickUP = itemView.findViewById(R.id.tvpickUp);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
        }
    }
}
