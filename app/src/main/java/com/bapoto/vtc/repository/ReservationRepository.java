package com.bapoto.vtc.repository;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.model.Reservation;
import com.bapoto.vtc.model.User;
import com.bapoto.vtc.ui.RecapActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReservationRepository {
    private static final String COLLECTION_NAME = "reservations";
    private static final String DATE_FIELD = "Date";
    private static final String DESTINATION_FIELD = "Destination";
    private static final String RDV_FIELD = "RDV";
    private static final String HOUR_FIELD = "Heure";
    private static volatile ReservationRepository instance;



    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

   /* public void createReservation (String nom,String tel,String desti,String rdv,String date,
                                   String heure,String infos) {



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> reservationToCreate = new HashMap<>(); {

            reservationToCreate.put("name",nom);
            reservationToCreate.put("phone",tel);
            //reservationToCreate.put("email",);
            reservationToCreate.put("pickUp",rdv);
            reservationToCreate.put("destination",desti);
            reservationToCreate.put("hour",heure);
            reservationToCreate.put("date",date);
            reservationToCreate.put("info",infos);

            db.collection("reservations")
                    .add(reservationToCreate)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
*/

    }









