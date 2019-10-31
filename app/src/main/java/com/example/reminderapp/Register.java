package com.example.reminderapp;




import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.helpers.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText user,pass,email,phone,pin;
    private Button register;
    private TextView login;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextView ClickToGoLogin;

    ProgressBar signupProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.signupUsernameEditText);
        email = findViewById(R.id.signupEmailEditText);
        pass = findViewById(R.id.signupPasswordEditText);
        phone = findViewById(R.id.signupPhoneEditText);
        pin = findViewById(R.id.signupPinEditText);
        register =  findViewById(R.id.signupSubmitButton);
        login = findViewById(R.id.ClickToGoLogin);
        ClickToGoLogin = findViewById(R.id.ClickToGoLogin);
        signupProgressBar = findViewById(R.id.signupProgressBar);

        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user1 = user.getText().toString();
                final String email1 = email.getText().toString();
                final String pass1 = pass.getText().toString();
                final String pin1 = pin.getText().toString().trim();
                final String phone1 = phone.getText().toString();

                signupProgressBar.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);


                firebaseAuth.createUserWithEmailAndPassword(email1, pass1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("username", user1);
                                    userdata.put("email", email1);
                                    userdata.put("password", pass1);
                                    userdata.put("loginPin", pin1);
                                    userdata.put("phone", phone1);


                                    firebaseFirestore.collection(new CollectionNames().getUsersCollection())
                                            .document(task.getResult().getUser().getUid())
                                            .set(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        signupProgressBar.setVisibility(View.GONE);
                                                        register.setVisibility(View.VISIBLE);
                                                        startActivity(new Intent(Register.this, MainActivity.class));
                                                    }
                                                }
                                            });
                                }
                                else {
                                    Toast.makeText(
                                            Register.this,
                                            "Signup ERRORRR",
                                            Toast.LENGTH_LONG
                                    ).show();
                                    signupProgressBar.setVisibility(View.VISIBLE);
                                    register.setVisibility(View.GONE);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("SIGNUP_LOG ", e.getMessage());
                                Toast.makeText(
                                        Register.this,
                                        "Error in signup",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });
            }
        });

        ClickToGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }



}
