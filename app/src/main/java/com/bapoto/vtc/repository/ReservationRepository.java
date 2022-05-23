package com.bapoto.vtc.repository;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bapoto.vtc.manager.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ReservationRepository {
    private static final String RESA_COLLECTION = "/reservations";
    private final UserManager userManager;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    {
        assert user != null;
        user.getUid();
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static volatile ReservationRepository instance;


    public static ReservationRepository getInstance() {
        ReservationRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ReservationRepository.class) {
            if (instance == null) {
                instance = new ReservationRepository();
            }
            return instance;
        }
    }

    private ReservationRepository() {
        this.userManager = UserManager.getInstance();}

    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(RESA_COLLECTION);
    }


    @SuppressLint("LongLogTag")
    public void getCurrentUserReservations(){
        db.collection("reservations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


    @SuppressLint("LongLogTag")
    public void createReservation(String nom, String tel, String desti, String rdv, String date,
                                  String hour, String infos) {

        userManager.getUserData().addOnSuccessListener(user -> {

            // Create the reservation Object
            Map<String, Object> reservationToCreate = new HashMap<>(); {

                reservationToCreate.put("name", nom);
                reservationToCreate.put("phone", tel);
                //reservationToCreate.put("email",);
                reservationToCreate.put("pickUp", rdv);
                reservationToCreate.put("destination", desti);
                reservationToCreate.put("hour", hour);
                reservationToCreate.put("date", date);
                reservationToCreate.put("info", infos);
                reservationToCreate.put("sender",user);

                //Store Reservation to firesteStore
               this.getUsersCollection()
                        .add(reservationToCreate)
                        .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
            }
        });
    }
}








