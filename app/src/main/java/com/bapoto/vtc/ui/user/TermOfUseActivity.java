package com.bapoto.vtc.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bapoto.bapoto.R;
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