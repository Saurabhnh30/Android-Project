package com.example.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity
{
    FirebaseAuth firebaseAuth;

    Button rmd;
    Button addnote;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(home.this, MainActivity.class));
            }
        });

        rmd = findViewById(R.id.rmd);
        rmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(home.this,MainPage.class);
                startActivity(a);
            }
        });

        addnote =  findViewById(R.id.addnote);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(home.this,Note.class);
                startActivity(b);
            }
        });
    }
}
