package com.bapoto.vtc.ui;

import static com.bapoto.vtc.utilities.Constants.KEY_PHONE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityReservationBinding;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ReservationActivity extends AppCompatActivity {
    private ActivityReservationBinding binding;
    private PreferenceManager preferenceManager;
    private DatePickerDialog datePickerDialog;
    Button timeButton;
    private int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        initDatepicker();
        loadUserDetails();
        setListeners();
        Button dateButton = binding.datePickerButton;
        dateButton.setText(getTodayDate());
        timeButton = binding.timeButton;
        timeButton.setHintTextColor(getResources().getColor(R.color.black));
    }

    private void loadUserDetails() {
        binding.inputName.setText(preferenceManager.getString(Constants.KEY_NAME));
    }

    private void setListeners() {
        binding.resaButton.setOnClickListener(view -> {
            if (isValidReservationDetails()) {
                navigateToSummary();
            }
        });
    }

    private Boolean isValidReservationDetails() {

        if (Objects.requireNonNull(binding.inputName.getText()).toString().isEmpty()) {
            presentModal("Veuillez saisir votre nom");
            return false;
        } else if (Objects.requireNonNull(binding.inputPhone.getText()).toString().isEmpty()) {
            presentModal("Veuillez saisir votre numéro de téléphone");
            return false;
        } else if (Objects.requireNonNull(binding.inputPickUp.getText()).toString().isEmpty()) {
            presentModal("Veuillez saisir le lieu du RDV");
            return false;
        } else if (Objects.requireNonNull(binding.inputDropOff.getText()).toString().isEmpty()) {
            presentModal("Veuillez saisir la destination");
            return false;
        } else if (binding.datePickerButton.getText().toString().isEmpty()) {
            presentModal("Veuillez saisir la date");
            return false;
        }else if (binding.datePickerButton.getText().toString().equals(getTodayDate())) {
                presentModal("Veuillez vérifier la date");
                return false;
        } else if (binding.timeButton.getText().toString().isEmpty()) {
            presentModal("Veuillez saisir l'heure");
            return false;
        } else {
            return true;
        }
    }

    private void navigateToSummary() {
        String nom = Objects.requireNonNull(binding.inputName.getText()).toString();
        String tel = Objects.requireNonNull(binding.inputPhone.getText()).toString();
        String desti = Objects.requireNonNull(binding.inputDropOff.getText()).toString();
        String rdv = Objects.requireNonNull(binding.inputPickUp.getText()).toString();
        String  date = binding.datePickerButton.getText().toString();
        String heure = binding.timeButton.getText().toString();
        String infos = Objects.requireNonNull(binding.inputInfo.getText()).toString();
        Intent intent = new Intent(ReservationActivity.this, SummaryActivity.class);

        intent.putExtra("nomprenom", nom);
        intent.putExtra("tel", tel);
        intent.putExtra("destination", desti);
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
        DatePickerDialog.OnDateSetListener dateSetListener = (view, day, month, year) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            binding.datePickerButton.setText(date);
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
        builder.setPositiveButton("OK", (dialog, id) -> {
            // User clicked OK button
        });
        builder.setTitle("OUPS...");
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                hour, minute, true);

        timePickerDialog.setTitle("Heure du RDV");
        timePickerDialog.show();
    }

}

