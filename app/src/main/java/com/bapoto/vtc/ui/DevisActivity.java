package com.bapoto.vtc.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.manager.UserManager;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Locale;

public class DevisActivity extends AppCompatActivity {
    private final UserManager userManager = UserManager.getInstance();
    private EditText editText;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button timeButton;
    int hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devis);
        initDatepicker();
        updateUIWithUserData();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());
        timeButton = findViewById(R.id.timeButton);


        Button button = findViewById(R.id.boutonNom);
        EditText saisieNom = findViewById(R.id.ChampNom);
        EditText saisieTel = findViewById(R.id.champTel);
        EditText saisieDestination = findViewById(R.id.ChampDestination);
        EditText saisieRdv = findViewById(R.id.ChampRDV);
        //Button saisieDate = findViewById(R.id.datePickerButton);
        //Button saisieHeure = findViewById(R.id.timeButton);
        EditText saisieInfos = findViewById(R.id.ChampInfos);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Champs Obligatoire
                String nom = saisieNom.getText().toString();
                String tel = saisieTel.getText().toString();
                String desti = saisieDestination.getText().toString();
                String rdv = saisieRdv.getText().toString();
                String date = dateButton.getText().toString();
                String heure = timeButton.getText().toString();
                String infos = saisieInfos.getText().toString();

                if (nom.isEmpty()) {
                    presentModal("Veuillez saisir votre Nom");
                    return;
                }

                if (tel.isEmpty()) {
                    presentModal("Veuillez saisir votre Numéro de Téléphone");
                    return;
                }

                if (desti.isEmpty()) {
                    presentModal("Veuillez saisir votre Destination");
                    return;
                }
                if (rdv.isEmpty()) {
                    presentModal("Veuillez saisir le Lieu du RDV");
                    return;
                }
                if (date.isEmpty()) {
                    presentModal("Veuillez saisir la Date");
                    return;
                }
                if (heure.isEmpty()) {
                    presentModal("Veuillez saisir l'Heure");
                    return;
                }


                navigateToSummary(nom, tel, desti, rdv, date, heure, infos);


            }
        });

    }
    private void navigateToSummary(String nom, String tel, String destination, String rdv,
                                   String date, String heure, String infos) {
        Intent intent = new Intent(DevisActivity.this, BonDeDevis.class);

        intent.putExtra("nomprenom", nom);
        intent.putExtra("tel", tel);
        intent.putExtra("destination", destination);
        intent.putExtra("rdv", rdv);
        intent.putExtra("date", date);
        intent.putExtra("heure", heure);
        intent.putExtra("infos", infos);

        startActivity(intent);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatepicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int day, int month, int year) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_POSITIVE;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return day + "." + getMonthFormat(month) + "." + year;

    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "Janvier";
        if (month == 2)
            return "Février";
        if (month == 3)
            return "Mars";
        if (month == 4)
            return "Avril";
        if (month == 5)
            return "Mai";
        if (month == 6)
            return "Juin";
        if (month == 7)
            return "Juillet";
        if (month == 8)
            return "Août";
        if (month == 9)
            return "Septembre";
        if (month == 10)
            return "Octobre";
        if (month == 11)
            return "Novembre";
        if (month == 12)
            return "Décembre";
        // Défaut
        return "Janvier";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void presentModal(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setTitle("OUPS...");
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                hour, minute, true);

        timePickerDialog.setTitle("Heure du RDV");
        timePickerDialog.show();
    }

    private void setTextUserData(FirebaseUser user) {
        EditText saisieNom = findViewById(R.id.ChampNom);

        //Get username from User
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        saisieNom.setText(username);
    }

    private void updateUIWithUserData() {
        if (userManager.isCurrentUserLogged()) {
            FirebaseUser user = userManager.getCurrentUser();


            setTextUserData(user);
            //getUserData();
        }
    }
}


