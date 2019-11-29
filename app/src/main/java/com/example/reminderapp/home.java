package com.example.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.Instant;

public class home extends AppCompatActivity
{
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    Button rmd, addnote, logout ,encry, update , docbutton, addHomeContact;
    TextView usernameTV, emailTV, phoneTV;
    ImageView userAvatarHomeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        usernameTV = findViewById(R.id.usernameTV);
        phoneTV = findViewById(R.id.phoneTV);
        emailTV = findViewById(R.id.emailTV);
        encry = findViewById(R.id.encry);

        userAvatarHomeActivity = findViewById(R.id.userAvatarHomeActivity);

        logout = findViewById(R.id.logout);

        getLoggedInUserData();



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

        encry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(home.this, EncryptScreen.class);
                startActivity(c);

            }
        });

        update = findViewById(R.id.Update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent d = new Intent(home.this,Updateprofile.class);
                startActivity(d);
            }
        });


        docbutton =  findViewById(R.id.docbutton);
        docbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent e = new Intent(home.this ,DocumentListsScreenActivity.class);
                startActivity(e);
            }
        });


        addHomeContact = findViewById(R.id.addHomeContact);
        addHomeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent f = new Intent(home.this, AddContactsHome.class);
                startActivity(f);
            }
        });
    }


    public void getLoggedInUserData() {
        firestore.collection(new CollectionNames().getUsersCollection()).document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        usernameTV.setText(task.getResult().getString(Users.USERNAME));
                        emailTV.setText(task.getResult().getString(Users.EMAIL));
                        phoneTV.setText(task.getResult().getString(Users.PHONE));

                        Picasso.get().load(task.getResult().getString(Users.USER_AVATAR)).into(userAvatarHomeActivity);
                    }
                });
    }
}
