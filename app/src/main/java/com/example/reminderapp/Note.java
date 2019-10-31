package com.example.reminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Note extends AppCompatActivity
{
    Button btnnote;
    RecyclerView recyclerView;
    ProgressBar noteProgrssBar;

    NoteAdapter noteAdapter;

    FirebaseFirestore firebaseFirestore;
    List<Posts> postsStack;
    CollectionNames collectionNames;

    boolean onCreated = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Log.d("NOTE LOG ", "On Created method called");

        btnnote =  findViewById(R.id.btnote);
        recyclerView = findViewById(R.id.recyclerid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteProgrssBar = findViewById(R.id.noteProgrssBar);

        collectionNames = new CollectionNames();

        firebaseFirestore = FirebaseFirestore.getInstance();
        postsStack = new ArrayList<>();

        btnnote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Note.this,noteedit.class);
                startActivity(intent);
            }
        });


        noteProgrssBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        loadDataFromFirebase();

        onCreated = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Toast.makeText(Note.this, "Note", Toast.LENGTH_LONG).show();


        if (onCreated) {
            loadDataFromFirebase();
            Log.d("NOTE LOG ", "On PostResume method called");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        onCreated = true;
    }

    public void loadDataFromFirebase() {
        if (postsStack.size() > 0) postsStack.clear();

        firebaseFirestore.collection(collectionNames.getNotes()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Posts posts = new Posts(
                                        doc.getString("posttitle"),
                                        doc.getString("postdetails"),
                                        doc.getId()
                                );

                                postsStack.add(posts);
                            }
                            Log.d("NOTE DATA", postsStack.toString());

                            noteAdapter = new NoteAdapter(Note.this, postsStack);

                            noteProgrssBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            recyclerView.setAdapter(noteAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("NOTE DATA ", e.getMessage());
                    }
                });
    }
}
