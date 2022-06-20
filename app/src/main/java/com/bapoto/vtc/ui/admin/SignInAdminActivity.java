package com.bapoto.vtc.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivitySignInAdminBinding;
import com.bapoto.bapoto.databinding.ActivitySignInBinding;
import com.bapoto.vtc.ui.MainActivity;
import com.bapoto.vtc.ui.SignUpActivity;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInAdminActivity extends AppCompatActivity {

    private ActivitySignInAdminBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInAdminBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(this);

        if (preferenceManager.getBoolean(Constants.KEY_IS_ADMIN)) {
            Intent intent = new Intent(this, MainAdminActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(binding.getRoot());
        setListeners();
    }


    private  void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(view -> startActivity(new Intent(this, SignUpAdminActivity.class)));
        binding.buttonSignIn.setOnClickListener(view -> {
            if (isValidSignInDetails()) {
                signInAdmin();
            }
        });
    }

    private void signInAdmin(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_ADMIN)
                .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_ADMIN,true);
                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(this, MainAdminActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast("Enregistrement Impossible");
                    }
                });

    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility((View.VISIBLE));
        }else {
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().isEmpty()) {
            showToast("Entrez une adresse mail");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Entrez une adresse mail valide");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Entrez un mot de passe");
            return false;
        }else {
            return true;
        }
    }

}