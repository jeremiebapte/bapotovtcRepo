package com.bapoto.vtc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bapoto.bapoto.R;

public class SuccessReservationActivity extends AppCompatActivity {
    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_reservation);
        loadAnimationAndOpenMainActivity();
    }

    private void loadAnimationAndOpenMainActivity() {

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        setContentView(R.layout.activity_recap);

        imageView = findViewById(R.id.bapologoIv);
        slogan = findViewById(R.id.sloganTv);


        imageView.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        Intent intent = new Intent(SuccessReservationActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}