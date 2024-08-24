package com.example.myfitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class splashScreen extends AppCompatActivity {

    LottieAnimationView lottieView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottieView=findViewById(R.id.lottieView);
        Intent i = new Intent(splashScreen.this,com.example.myfitnessapp.authentication.registration.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(i);
            }
        },4000);
    }
}