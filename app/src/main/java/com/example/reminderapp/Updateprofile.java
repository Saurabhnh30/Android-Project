package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class Updateprofile extends AppCompatActivity
{
    FirebaseFirestore firestore;

    TextView updateUsernameTV, updateEmailTV, updatePasswordTV, updatePhoneTV, updatePinTV;
    EditText usernameupdate,phoneupdate,emailupdate, passwordupdate, pinupdate;
    Button updateprofilebuttonid;
    ImageButton usernameEditBtn, usernameCancelUpdateBtn
            , phoneEditBtn, phoneCancelUpdateBtn
            , pinEditBtn, pinCancelUpdateBtn
            , emailEditBtn, emailCancelUpdateBtn
            , passwordEditBtn, passwordCancelUpdateBtn;

    Users user;

    List<Users> usersList;

    WriteBatch batch;
    DocumentReference userDocRef;
    ProgressBar updateProfileProrgressBar;

    String LOGGED_IN_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        setTitle("Update profile");

        usersList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        batch = firestore.batch();
        userDocRef = firestore.collection(CollectionNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        updateProfileProrgressBar = findViewById(R.id.updateProfileProrgressBar);
        updateUsernameTV = findViewById(R.id.updateUsernameTV);
        updateEmailTV = findViewById(R.id.updateEmailTV);
        updatePasswordTV = findViewById(R.id.updatePasswordTV);
        updatePhoneTV = findViewById(R.id.updatePhoneTV);
        updatePinTV = findViewById(R.id.updatePinTV);

        usernameupdate = findViewById(R.id.usernameupdate);
        phoneupdate = findViewById(R.id.phoneupdate);
        emailupdate = findViewById(R.id.emailupdate);
        passwordupdate = findViewById(R.id.passwordupdate);
        pinupdate = findViewById(R.id.pinupdate);
        updateprofilebuttonid = findViewById(R.id.updateprofilebuttonid);

        usernameEditBtn = findViewById(R.id.usernameEditBtn);
        emailEditBtn = findViewById(R.id.emailEditBtn);
        passwordEditBtn = findViewById(R.id.passwordEditBtn);
        phoneEditBtn = findViewById(R.id.phoneEditBtn);
        pinEditBtn = findViewById(R.id.pinEditBtn);
        usernameCancelUpdateBtn = findViewById(R.id.usernameCancelUpdateBtn);
        emailCancelUpdateBtn = findViewById(R.id.emailCancelUpdateBtn);
        phoneCancelUpdateBtn = findViewById(R.id.phoneCancelUpdateBtn);
        pinCancelUpdateBtn = findViewById(R.id.pinCancelUpdateBtn);
        passwordCancelUpdateBtn = findViewById(R.id.passwordCancelUpdateBtn);



        firestore.collection(CollectionNames.USERS).document(LOGGED_IN_UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            user = new Users();
                            DocumentSnapshot doc = task.getResult();
                            user.setUsername(doc.getString(Users.USERNAME));
                            user.setPhone(doc.getString(Users.PHONE));
                            user.setLoginPin(doc.getString(Users.PIN));
                            user.setEmail(doc.getString(Users.EMAIL));
                            user.setPassword(doc.getString(Users.PASSWORD));
                            user.setUserAvatar(doc.getString(Users.USER_AVATAR));

                            updateUsernameTV.setText(user.getUsername());
                            updateEmailTV.setText(user.getEmail());
                            updatePinTV.setText(user.getLoginPin());
                            updatePasswordTV.setText(user.getPassword());
                            updatePhoneTV.setText(user.getPhone());
                        }
                    }
                });


        firestore.collection(CollectionNames.USERS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            usersList = task.getResult().toObjects(Users.class);
                            Log.d("UPDATE", usersList.toString());
                        }
                    }
                });


        updateprofilebuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileProrgressBar.setVisibility(View.VISIBLE);
                updateprofilebuttonid.setVisibility(View.GONE);

                if (!usernameupdate.getText().toString().equals("")) {
                    batch.update(userDocRef, Users.USERNAME, usernameupdate.getText().toString());
                }
                if (!emailupdate.getText().toString().equals("")) {
                    batch.update(userDocRef, Users.EMAIL, emailupdate.getText().toString());
                }
                if (!passwordupdate.getText().toString().equals("")) {
                    batch.update(userDocRef, Users.PASSWORD, passwordupdate.getText().toString());
                }
                if (!pinupdate.getText().toString().equals("")) {
                    batch.update(userDocRef, Users.PIN, pinupdate.getText().toString());
                }
                if (!phoneupdate.getText().toString().equals("")) {
                    batch.update(userDocRef, Users.PHONE, phoneupdate.getText().toString());
                }

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateProfileProrgressBar.setVisibility(View.GONE);
                        updateprofilebuttonid.setVisibility(View.VISIBLE);

                        startActivity(new Intent(Updateprofile.this, home.class));
                    }
                });
            }
        });



    }




    public void toggleUsernameUpdate(View v) {
        if (usernameEditBtn.getVisibility() == View.VISIBLE) {
            usernameEditBtn.setVisibility(View.GONE);
            updateUsernameTV.setVisibility(View.GONE);
            usernameCancelUpdateBtn.setVisibility(View.VISIBLE);
            usernameupdate.setVisibility(View.VISIBLE);
            usernameupdate.setText(user.getUsername());
        }
        else {
            usernameEditBtn.setVisibility(View.VISIBLE);
            updateUsernameTV.setVisibility(View.VISIBLE);
            usernameCancelUpdateBtn.setVisibility(View.GONE);
            usernameupdate.setVisibility(View.GONE);
            usernameupdate.setText("");
        }
    }

    public void toggleEmailUpdate(View v) {
        if (emailEditBtn.getVisibility() == View.VISIBLE) {
            emailEditBtn.setVisibility(View.GONE);
            updateEmailTV.setVisibility(View.GONE);
            emailCancelUpdateBtn.setVisibility(View.VISIBLE);
            emailupdate.setVisibility(View.VISIBLE);
            emailupdate.setText(user.getEmail());
        }
        else {
            emailEditBtn.setVisibility(View.VISIBLE);
            updateEmailTV.setVisibility(View.VISIBLE);
            emailCancelUpdateBtn.setVisibility(View.GONE);
            emailupdate.setVisibility(View.GONE);
            emailupdate.setText("");
        }
    }

}
