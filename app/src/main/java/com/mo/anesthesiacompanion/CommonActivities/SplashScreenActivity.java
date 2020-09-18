package com.mo.anesthesiacompanion.CommonActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mo.anesthesiacompanion.R;

public class SplashScreenActivity extends AppCompatActivity {

    //duration of screen Visibility
    private static int SPLASH_TIMER= 4000;

    //variables
    TextView anesthesia_txt, companion_txt, slogan_txt;

    //Animation variables
    Animation sideAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        // hooks view
        anesthesia_txt = findViewById(R.id.anesthesia);
        companion_txt = findViewById(R.id.companion);
        slogan_txt = findViewById(R.id.slogan);

        // hooks animation
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //set animation to elements in views
        anesthesia_txt.setAnimation(bottomAnim);
        companion_txt.setAnimation(bottomAnim);
        slogan_txt.setAnimation(bottomAnim);

        doSplashScreenDuration();
    }

    private void doSplashScreenDuration() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIMER);
    }
}