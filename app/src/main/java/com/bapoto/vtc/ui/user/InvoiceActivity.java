package com.bapoto.vtc.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityInvoiceBinding;
import com.bapoto.vtc.utilities.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class InvoiceActivity extends AppCompatActivity {
    private ActivityInvoiceBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTextViews();
    }


    public void setTextViews() {

        TextView containerName = binding.tvInvName;
        TextView containerDate = binding.tvInvDate;
        TextView containerFinalPriceTTC = binding.tvTotal;
        TextView containerTVA = binding.TVA;
        TextView containerPickUp = binding.tvPickUpInvoice;
        TextView containerDropOff = binding.tvDropOffInvoice;
        TextView containerpriceHT = binding.tvPriceHT;
        TextView containerTotalHT = binding.tvTotalHT;

        Bundle extras = getIntent().getExtras();

        String xName = extras.getString("name");
        String xDate = extras.getString("date");
        String xPrice = extras.getString("price");
        String xPickUp = extras.getString("pickUp");
        String xDropOff = extras.getString("dropOff");

        double tva =  0.1;
        int inputPrice = Integer.parseInt(xPrice);
        double tvaOfThePrice = (inputPrice*tva);

        double priceHT = (inputPrice - tvaOfThePrice);

        containerName.setText(xName);
        containerFinalPriceTTC.setText(xPrice);
        containerPickUp.setText(xPickUp);
        containerDropOff.setText(xDropOff);
        containerDate.setText(xDate);

        containerpriceHT.setText(String.valueOf(priceHT));
        containerTotalHT.setText(String.valueOf(priceHT));

        containerTVA.setText(String.valueOf(tvaOfThePrice));

        containerFinalPriceTTC.setText(String.valueOf(inputPrice));
    }
}