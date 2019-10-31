package com.example.reminderapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_TIMEOUT = 1500;

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();

                if (firebaseUser == null) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }
                else {
                    startActivity(new Intent(SplashScreen.this, LoginPinActivity.class));
                }
            }
        },SPLASH_TIMEOUT);

    }
}
