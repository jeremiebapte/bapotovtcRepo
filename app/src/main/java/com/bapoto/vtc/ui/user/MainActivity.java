package com.bapoto.vtc.ui.user;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.bapoto.bapoto.R;
import com.bapoto.bapoto.databinding.ActivityMainBinding;
import com.bapoto.vtc.utilities.BaseActivity;
import com.bapoto.vtc.utilities.Constants;
import com.bapoto.vtc.utilities.PreferenceManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class MainActivity extends BaseActivity implements LocationListener {
    private LocationManager lm;
    private static final int PERMS_CALL = 7;
    private PreferenceManager preferenceManager;
    private ActivityMainBinding binding;
    private MapFragment mapFragment;
    private GoogleMap googleMap;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListeners();
        getToken();
        preferenceManager = new PreferenceManager(this);
        try {
            suscribeToTopic();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lm!=null) {
            lm.removeUpdates(this);
        }
    }

    private void setupListeners(){
        binding.fabProfile.setOnClickListener(view -> startProfileActivity());
        binding.fabResa.setOnClickListener(view -> startReservationActivity());

    }

    private void suscribeToTopic() throws JSONException, IOException {
        FirebaseMessaging.getInstance().subscribeToTopic("new_driver")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Succès de l'abonnement aux événements d'inscription des chauffeurs");
                    } else {
                        System.out.println("Échec de l'abonnement aux événements d'inscription des chauffeurs");
                    }
                });
    }


    // Launching Profile Activity
    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startReservationActivity(){
        Intent intent = new Intent(this, ReservationActivity.class);
        startActivity(intent);
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
            preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference documentReference =
                    database.collection(Constants.KEY_COLLECTION_USERS)
                            .document(preferenceManager.getString(Constants.KEY_USER_ID)
                            );
            documentReference.update(Constants.KEY_FCM_TOKEN, token)
                    .addOnFailureListener(e -> showToast());
        }

        private void showToast(){
        Toast.makeText(this, "Echec mis à jour du token", Toast.LENGTH_SHORT).show();
    }


    //-------------
    // MAP
    //-------------


    //Method for Check the permission for connect to GPS
    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },PERMS_CALL );
            return;
        }

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        }
        if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        }
        if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        }

        loadMap();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @Nonnull String[] permissions, @Nonnull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMS_CALL) {
            checkPermissions();
        }
    }

    @Override
    public void onProviderEnabled(@Nonnull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@Nonnull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onLocationChanged(@Nonnull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (googleMap != null) {
            LatLng googleLocation = new LatLng(latitude,longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(googleLocation));
            googleMap.getMaxZoomLevel();


        }

    }
    @SuppressWarnings("MissingPermission")
    private void loadMap() {
        mapFragment.getMapAsync(googleMap -> {
            MainActivity.this.googleMap = googleMap;
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            googleMap.setMyLocationEnabled(true);


        });
    }
}