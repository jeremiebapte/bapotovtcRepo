package com.bapoto.bapoto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class invoiceActivity extends AppCompatActivity {
    FirebaseDatabase db =  FirebaseDatabase.getInstance();
    DatabaseReference myref = db.getReference("record");
    Button saveAndPrintBtn, printButton;
    EditText editTextName, editTextQty;
    Spinner spinner;
    String[] itemList;
    double[] itemPrice;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        callFindViewByIds();
    }

    private void callFindViewByIds() {
        saveAndPrintBtn = findViewById(R.id.btnSaveAndPrint);
        printButton = findViewById(R.id.btnPrint);
        editTextName = findViewById(R.id.editTextName);
        editTextQty = findViewById(R.id.editTextQty);
        spinner = findViewById(R.id.spinner);
        itemList = new String[]{"Petrol,Diesel"};
        itemPrice = new double[] {72.56, 36.56};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,itemList);
        spinner.setAdapter(adapter);
    }
}