package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.reminderapp.helpers.CollectionNames;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPinActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    ProgressBar pinprogressBar;

    EditText userPinEditText;
    Button userPinSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pin);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userPinEditText = findViewById(R.id.userPinEditText);
        userPinSubmitBtn = findViewById(R.id.userPinSubmitBtn);
        pinprogressBar  = findViewById(R.id.pinprogressBar);

        userPinSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pin = userPinEditText.getText().toString().trim();
                pinprogressBar.setVisibility(View.VISIBLE);
                userPinSubmitBtn.setVisibility(View.INVISIBLE);
                firestore.collection(new CollectionNames().getUsersCollection())
                        .document(firebaseUser.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (pin.equals(task.getResult().get("loginPin"))) {
                                        pinprogressBar.setVisibility(View.INVISIBLE);
                                        userPinSubmitBtn.setVisibility(View.VISIBLE);
                                        finish();
                                        startActivity(new Intent(LoginPinActivity.this, home.class));
                                    }
                                    else {
                                        pinprogressBar.setVisibility(View.INVISIBLE);
                                        userPinSubmitBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(
                                                LoginPinActivity.this,
                                                "Pin is WRONG",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("LOGINPIN_LOG ", e.getMessage());
                            }
                        });
            }
        });
    }
}
