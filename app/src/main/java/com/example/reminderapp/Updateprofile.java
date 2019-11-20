package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Updateprofile extends AppCompatActivity
{
    EditText usernameupdate,phoneupdate,emailupdate, passwordupdate, pinupdate;
    Button updateprofilebuttonid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        usernameupdate = findViewById(R.id.usernameupdate);
        phoneupdate = findViewById(R.id.phoneupdate);
        emailupdate = findViewById(R.id.emailupdate);
        passwordupdate = findViewById(R.id.passwordupdate);
        pinupdate = findViewById(R.id.pinupdate);
        updateprofilebuttonid = findViewById(R.id.updateprofilebuttonid);

    }
}
