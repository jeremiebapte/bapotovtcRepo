package com.bapoto.vtc.ui.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapoto.databinding.ActivityUsersBinding;
import com.bapoto.vtc.adapters.AdminAdapter;
import com.bapoto.vtc.listeners.AdminListener;
import com.bapoto.vtc.model.Admin;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements AdminListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        setListeners();
        getUsers();
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());

    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful()&&task.getResult() != null) {
                        List<Admin> admins = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            Admin admin = new Admin();
                            admin.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            admin.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            admin.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            admin.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            admin.id = queryDocumentSnapshot.getId();
                            admins.add(admin);
                        }
                        if (admins.size() > 0) {
                            AdminAdapter adminsAdapter = new AdminAdapter(admins,this);
                            binding.userrecyclerView.setAdapter(adminsAdapter);
                            binding.userrecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            showErrorMessage();
                        }
                    }
                });
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s","Pas d'utilisateur disponible"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClickedAdmin(Admin admin) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,admin);
        startActivity(intent);
        finish();
    }
}