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
import com.google.firebase.firestore.DocumentSnapshot;

public class ReservationAdapter extends FirestoreRecyclerAdapter <Reservation, ReservationAdapter.ReservationHolder> {

    private OnItemClicklistener listener;

    public ReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationHolder holder, int position, @NonNull Reservation model) {
        holder.tvName.setText(model.getName());
        holder.tvPickUP.setText(model.getPickUp());
        holder.tvDestination.setText(model.getDropOff());
        holder.tvHour.setText(model.getHour());
        holder.tvDate.setText(model.getDate());

        if (model.getDayAccepted()!= null) {
            holder.tvResaAccepted.setText(String.format("%s","Réservation Acceptée!!"));
            holder.tvResaAccepted.setVisibility(View.VISIBLE);
        } else {
            holder.tvResaAccepted.setVisibility(View.INVISIBLE);
        }

    }



    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation,
                parent,false);
        return new ReservationHolder(v);

    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public class ReservationHolder extends RecyclerView.ViewHolder {
        TextView tvPickUP;
        TextView tvDestination;
        TextView tvDate;
        TextView tvHour;
        TextView tvName;
        TextView tvResaAccepted;


        public ReservationHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPickUP = itemView.findViewById(R.id.tvpickUp);
            tvDestination = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvResaAccepted = itemView.findViewById(R.id.tvIsAccepted);



            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position),position );

                }
            });

        }

    }

    public interface OnItemClicklistener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClicklistener listener) {
        this.listener = listener;
    }



}





