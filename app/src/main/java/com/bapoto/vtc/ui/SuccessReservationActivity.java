package com.bapoto.vtc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapoto.R;

public class SuccessReservationActivity extends AppCompatActivity {
ImageView imageView;
Animation topAnim, bottomAnim,fadeIn,rotate;
TextView slogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_succes_reservation);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotate);

        imageView = findViewById(R.id.bapologoIv);
        slogan = findViewById(R.id.sloganTv);


        imageView.setAnimation(rotate);
        slogan.setAnimation(fadeIn);


        Runnable runnable = () -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        };


        new Handler().postDelayed(runnable,3000);
    }




    }
