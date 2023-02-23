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

public class AllRideUserAdapter extends FirestoreRecyclerAdapter<Reservation, AllRideUserAdapter.AllRideUserHolder> {
    private OnTextViewClickListener listener;


    public AllRideUserAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllRideUserHolder holder, int position, @NonNull Reservation model) {
        holder.tvName.setText(model.getName());
        holder.tvPickUP.setText(model.getPickUp());
        holder.tvDestination.setText(model.getDropOff());
        holder.tvHour.setText(model.getHour());
        holder.tvDate.setText(model.getDate());

    }

    @NonNull
    @Override
    public AllRideUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_ride_user,
                parent,false);
        return new AllRideUserHolder(v);
    }

     class AllRideUserHolder extends RecyclerView.ViewHolder{
         final TextView  tvPickUP,tvDestination,tvDate,tvHour,tvName, tvGenerateBill;

        public AllRideUserHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPickUP = itemView.findViewById(R.id.tvpickUp);
            tvDestination = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvGenerateBill = itemView.findViewById(R.id.tvGenerateBill);

            tvGenerateBill.setOnClickListener(view -> {
                int positon = getBindingAdapterPosition();
                if (positon!=RecyclerView.NO_POSITION && listener != null) {
                    listener.onTextViewClick(getSnapshots().getSnapshot(positon),positon);
                }


            });


        }
    }
    public interface OnTextViewClickListener{
        void onTextViewClick(DocumentSnapshot documentSnapshot,int positon);
    }

    public  void setOnTextViewClickListener(OnTextViewClickListener listener) {
        this.listener = listener;
    }
}
