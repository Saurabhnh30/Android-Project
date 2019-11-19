package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class noteedit extends AppCompatActivity


{

  FirebaseFirestore firebaseFirestore;

    ProgressBar progressBar;
    CollectionNames collectionNames;

    Button addyournote;
    EditText addtitle, adddetalis;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteedit);

        addyournote = findViewById(R.id.addyournote);
        addtitle = findViewById(R.id.addtitle);
        adddetalis = findViewById(R.id.adddetalis);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.postprogressbar);

        collectionNames = new CollectionNames();


        addyournote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                progressBar.setVisibility(View.VISIBLE);
                addyournote.setVisibility(View.INVISIBLE);

                 String addt = addtitle.getText().toString();
                 String addd = adddetalis.getText().toString();

                 Posts postnotes = new Posts();
                 postnotes.setPosttitle(addt);
                 postnotes.setPostdetails(addd);
                 postnotes.setImagesList(new ArrayList<String>());
                 postnotes.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());


                 firebaseFirestore
                         .collection(collectionNames.getNotes())
                         .add(postnotes)
                         .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                             @Override
                             public void onComplete(@NonNull Task<DocumentReference> task) {

                                 Toast.makeText(noteedit.this,"Done",Toast.LENGTH_SHORT);
                                 progressBar.setVisibility(View.INVISIBLE);
                                 addyournote.setVisibility(View.VISIBLE);
                                 Intent intent = new Intent(noteedit.this,Note.class);
                                 startActivity(intent);
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(noteedit.this,"Fail",Toast.LENGTH_SHORT);
                             }
                         });
            }
        });


    }
}
