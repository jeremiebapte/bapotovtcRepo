package com.bapoto.vtc.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityProfileBinding;
import com.bapoto.vtc.manager.ChatManager;
import com.bapoto.vtc.manager.UserManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding>{
        final Context context = this;
    @Override
    protected ActivityProfileBinding getViewBinding() {
        return ActivityProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
        updateUIWithUserData();

    }

    private void setupListeners() {
        // Sign out button
        UserManager userManager = UserManager.getInstance();
        binding.signOutButton.setOnClickListener(view -> {
            alertSignOut();
            });

        // all reservations button
        binding.allResaBtn.setOnClickListener(view -> {
            startAllResaActivity();
        });

        // chat button
        ChatManager chatManager = ChatManager.getInstance();
        binding.chatButton.setOnClickListener(view -> {
            startChatActivity();
        });
}



    private void startChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void startAllResaActivity() {
        Intent intent = new Intent(this, AllReservation.class);
        startActivity(intent);
    }

    private void updateUIWithUserData(){
        UserManager userManager = UserManager.getInstance();
        if(userManager.isCurrentUserLogged()){
            FirebaseUser user = userManager.getCurrentUser();

            if(user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView);
    }

    private void setTextUserData(FirebaseUser user){

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        binding.usernameEditText.setText(username);
        binding.emailTextView.setText(email);
    }

    //PopUp for confirm the signout
    public void alertSignOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title

        alertDialogBuilder.setTitle("DÉCONNEXION");
        alertDialogBuilder.setIcon(R.drawable.ic_sad);

        // set dialog message
        alertDialogBuilder
                .setMessage("Êtes vous sur de vouloir vous déconnecter?")
                .setCancelable(false)
                .setPositiveButton("Oui !",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        FirebaseAuth.getInstance().signOut();

                        showSnackBar(getString(R.string.logout_succeed));
                        backToMainAfterFewseconds();
                    }
                })
                .setNegativeButton("Non",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void backToMainAfterFewseconds() {

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProfileActivity.this.finish();
            }
        }, 1500);
    }

    private void showSnackBar( String message){
        Snackbar.make(binding.activityProfileDrawerLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}

