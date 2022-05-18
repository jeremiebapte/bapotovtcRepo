package com.bapoto.vtc.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bapoto.bapoto.R;
import com.bapoto.vtc.manager.UserManager;
import com.bapoto.vtc.ui.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    UserManager userManager;
    private Button signOutButton;
    private ImageView profileImageView;
    private EditText usernameEditText;
    private TextView emailTextView;


    public static ProfileFragment newInstance() {
        return (new ProfileFragment());
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUIWithUserData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        signOutButton = (Button) rootView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(signoutBtn);
        profileImageView = (ImageView) rootView.findViewById(R.id.profileImageView);
        usernameEditText = (EditText) rootView.findViewById(R.id.usernameEditText);
        emailTextView = (TextView) rootView.findViewById(R.id.emailTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        userManager = UserManager.getInstance();
        return rootView;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private final View.OnClickListener signoutBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertsignout();
                }
            };


    private void updateUIWithUserData() {
        userManager = UserManager.getInstance();
            if (userManager.isCurrentUserLogged()) {
                FirebaseUser user = userManager.getCurrentUser();

                if (user.getPhotoUrl() != null) {
                    setProfilePicture(user.getPhotoUrl());
                }
                //setTextUserData();
                getUserData();
            }
        }

        private void setProfilePicture(Uri profilePictureUrl) {
            Glide.with(ProfileFragment.newInstance().requireContext())
                    .load(profilePictureUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profileImageView);
        }

        private void setTextUserData() {

            userManager = UserManager.getInstance();
            FirebaseUser user = userManager.getCurrentUser();



            //Get email & username from User
            assert user != null;
            String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
            String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

            //Update views with data
           // usernameEditText.setText(username);
            emailTextView.setText(email);
        }

        private void getUserData() {
                userManager = UserManager.getInstance();
                userManager.getUserData().addOnSuccessListener(user -> {
                // Set the data with the user information
                String username = TextUtils.isEmpty(user.getUsername())
                        ? getString(R.string.info_no_username_found) : user.getUsername();
                usernameEditText.setText(username);
            });
        }

        //PopUp for confirm the signout
    public void alertsignout()
    {
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(this.requireContext());

        // Setting Dialog Title
        alertDialog2.setTitle("DÉCONNEXION");

        // Setting Dialog Message
        alertDialog2.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("Oui!",
                (dialog, which) -> {
                    // Write your code here to execute after dialog
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getContext(),
                            MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }


    }




