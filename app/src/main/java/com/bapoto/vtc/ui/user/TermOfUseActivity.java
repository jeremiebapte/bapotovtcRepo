package com.bapoto.vtc.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapoto.databinding.ActivityTermOfUseBinding;

public class TermOfUseActivity extends AppCompatActivity {
    private ActivityTermOfUseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTermOfUseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {

        binding.okbtn.setOnClickListener(view -> startActivity(new Intent(this,SignInActivity.class)));
    }

}