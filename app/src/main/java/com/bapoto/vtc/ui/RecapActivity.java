package com.bapoto.vtc.ui;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bapoto.bapoto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RecapActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


     // TODO interface de cette activité // Lecture des réservations //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);


        Button boutonresa = findViewById(R.id.sndButtonR);
        TextView resultatNomPrenom = findViewById(R.id.arrive1);
        TextView resultatTel = findViewById(R.id.arrive2);
        //TextView resultatMail = findViewById(R.id.arrive3);
        TextView resultatDestination = findViewById(R.id.arrive5);
        TextView resultatRdv = findViewById(R.id.arrive4);
        TextView resultatDate = findViewById(R.id.arrive7);
        TextView resultatHeure = findViewById(R.id.arrive6);
        TextView resultatInfos = findViewById(R.id.arrive8);

        Bundle extras = getIntent().getExtras();

        String n = extras.getString("nomprenom");
        String t = extras.getString("tel");
        String m = extras.getString("mail");
        String d = extras.getString("destination");
        String r = extras.getString("rdv");
        String da = extras.getString("date");
        String h = extras.getString("heure");
        String infos = extras.getString("infos");

        resultatNomPrenom.setText(n);
        resultatTel.setText(t);
        //resultatMail.setText(m);
        resultatDestination.setText(d);
        resultatRdv.setText(r);
        resultatDate.setText(da);
        resultatHeure.setText(h);
        resultatInfos.setText(infos);

        boutonresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = resultatNomPrenom.getText().toString();
                String tel = resultatTel.getText().toString();
                String desti = resultatDestination.getText().toString();
                String rdv = resultatRdv.getText().toString();
                String date = resultatDate.getText().toString();
                String heure = resultatHeure.getText().toString();
                String infos = resultatInfos.getText().toString();

                Map<String, Object> reservationToCreate = new HashMap<>();
                {

                    reservationToCreate.put("name", nom);
                    reservationToCreate.put("phone", tel);
                    //reservationToCreate.put("email",);
                    reservationToCreate.put("pickUp", rdv);
                    reservationToCreate.put("destination", desti);
                    reservationToCreate.put("hour", heure);
                    reservationToCreate.put("date", date);
                    reservationToCreate.put("info", infos);

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
                }


                //envoi boite Mail
                boutonresa.setOnClickListener(view -> {
                    String message = String.format("Bonjour BAPOTO,\n" +
                            "%s souhaite une réservation pour le %s à %s.\n\n" +
                            "Lieu de prise en charge : %s\n" +
                            "Destination : %s\n" +
                            "Email : %s\n" +
                            "Téléphone : %s\n" +
                            "Infos : %s\n", n, da, h, r, d, m, t, infos);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact.bapoto@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "Reservation");
                    i.putExtra(Intent.EXTRA_TEXT, message);
                    try {
                        startActivity(Intent.createChooser(i, "Envoi Réservation"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(RecapActivity.this, "Aucun email paramétré sur" +
                                " votre téléphone", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        this.presentModal();
    }



    public void presentModal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK" , (dialog, id) -> {
            // User clicked OK button
        });
        builder.setTitle("ENVOI RÉSERVATION");
        builder.setMessage("Tout est OK ? \n" +
                "\nCliquez sur ENVOI puis choisissez votre service de messagerie habituel pour" +
                " nous envoyer votre réservation par MAIL. On vous contacte au plus vite " +
                "pour tout vous confirmer.");
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}

