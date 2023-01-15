package com.bapoto.vtc.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityInvoiceBinding;
import com.bapoto.vtc.utilities.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        TextView resultName = binding.tvInvName;
        TextView resultDate = binding.tvInvDate;
        TextView resultPrice = binding.tvTotalTTC;

        Bundle extras = getIntent().getExtras();

        String xName = extras.getString("name");
        String xDate = extras.getString("date");
        String xPrice = extras.getString("price");

        resultName.setText(xName);
        resultDate.setText(xDate);
        resultPrice.setText(xPrice);

    }
}