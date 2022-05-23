package com.bapoto.vtc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityRecapBinding;
import com.bapoto.vtc.manager.ReservationManager;
import com.google.android.material.snackbar.Snackbar;

public class RecapActivity extends BaseActivity<ActivityRecapBinding> {


    @Override
    protected ActivityRecapBinding getViewBinding() {
        return ActivityRecapBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        animationAndGoBackToMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

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
            public void onClick(View view) {
                presentModal();
            }
        });

    }


    private void sendReservationMail() {
        Bundle extras = getIntent().getExtras();

        String n = extras.getString("nomprenom");
        String t = extras.getString("tel");
        String m = extras.getString("mail");
        String d = extras.getString("destination");
        String r = extras.getString("rdv");
        String da = extras.getString("date");
        String h = extras.getString("heure");
        String infos = extras.getString("infos");

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



            storeReservationInFirestore();

    }

    private void animationAndGoBackToMain() {

        Intent intent = new Intent(RecapActivity.this, SuccessReservationActivity.class);
        startActivity(intent);
        finish();



    }




    // Create a new reservation
    private void storeReservationInFirestore() {
        TextView resultNameFirstName = findViewById(R.id.arrive1);
        TextView resultPhone = findViewById(R.id.arrive2);
        //TextView resultatMail = findViewById(R.id.arrive3);
        TextView resultDropOff = findViewById(R.id.arrive5);
        TextView resultPickUp = findViewById(R.id.arrive4);
        TextView resultatDate = findViewById(R.id.arrive7);
        TextView resultHour = findViewById(R.id.arrive6);
        TextView resultInfos = findViewById(R.id.arrive8);

        String name = resultNameFirstName.getText().toString();
        String phone = resultPhone.getText().toString();
        String dropOff = resultDropOff.getText().toString();
        String pickUp = resultPickUp.getText().toString();
        String date = resultatDate.getText().toString();
        String hour = resultHour.getText().toString();
        String infos = resultInfos.getText().toString();


        ReservationManager reservationManager = new ReservationManager();
        reservationManager.createReservation(name, phone, dropOff, pickUp, date, hour, infos);
    }

    // Pop up for explain the send mail reservation
    public void presentModal () {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("OK", (dialog, id) ->
                    sendReservationMail());
            builder.setTitle("ENVOI RÉSERVATION");
            builder.setMessage("Tout est OK ? \n" +
                    "\nCliquez sur ENVOI puis choisissez votre service de messagerie habituel pour" +
                    " nous envoyer votre réservation par MAIL. On vous contacte au plus vite " +
                    "pour tout vous confirmer.");
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(binding.layoutRecap, message, Snackbar.LENGTH_SHORT).show();
    }

}

