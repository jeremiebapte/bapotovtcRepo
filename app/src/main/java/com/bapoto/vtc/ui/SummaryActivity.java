package com.bapoto.vtc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapoto.databinding.ActivitySummaryBinding;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SummaryActivity extends AppCompatActivity {
    private ActivitySummaryBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onRestart() {
        super.onRestart();
        animationAndGoBackToMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTextViews();
        setListeners();
    }

    private void setListeners() {
        binding.buttonSendResa.setOnClickListener(view -> presentModal());
    }

    private void setTextViews() {
        TextView resultName = binding.containerName;
        TextView resultPhone = binding.containerPhonr;
        TextView resultDropOff = binding.containerDropOff;
        TextView resultPickUp = binding.containerPickUp;
        TextView resultDate = binding.containerDate;
        TextView resultHour = binding.containerHour;
        TextView resultInfos = binding.containerInfos;

        Bundle extras = getIntent().getExtras();

        String n = extras.getString("nomprenom");
        String t = extras.getString("tel");
        String d = extras.getString("destination");
        String r = extras.getString("rdv");
        String da = extras.getString("date");
        String h = extras.getString("heure");
        String infos = extras.getString("infos");

        resultName.setText(n);
        resultPhone.setText(t);
        resultDropOff.setText(d);
        resultPickUp.setText(r);
        resultDate.setText(da);
        resultHour.setText(h);
        resultInfos.setText(infos);
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
            Toast.makeText(SummaryActivity.this, "Aucun email paramétré sur" +
                    " votre téléphone", Toast.LENGTH_SHORT).show();
        }

        storeReservationInFirestore();
    }

    private void animationAndGoBackToMain() {
        Intent intent = new Intent(SummaryActivity.this, SuccessReservationActivity.class);
        startActivity(intent);
        finish();
    }

    // Create a new reservation
    private void storeReservationInFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> reservation = new HashMap<>();
        reservation.put(Constants.KEY_NAME,binding.containerName.getText().toString());
        reservation.put(Constants.KEY_PHONE,binding.containerPhonr.getText().toString());
        reservation.put(Constants.KEY_PICK_UP,binding.containerPickUp.getText().toString());
        reservation.put(Constants.KEY_DROP_OFF,binding.containerDropOff.getText().toString());
        reservation.put(Constants.KEY_DATE,binding.containerDate.getText().toString());
        reservation.put(Constants.KEY_HOUR,binding.containerHour.getText().toString());
        reservation.put(Constants.KEY_INFOS,binding.containerInfos.getText().toString());
        reservation.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
        db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                .add(reservation);
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
}
