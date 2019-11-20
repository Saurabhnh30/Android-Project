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
import com.example.reminderapp.models.Notes;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Note extends AppCompatActivity
{
    Button btnnote;
    RecyclerView recyclerView;
    ProgressBar noteProgrssBar;

    NoteAdapter noteAdapter;

    FirebaseFirestore firestore;
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

        firestore = FirebaseFirestore.getInstance();

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
        firestore.collection(new CollectionNames().getNotes())
                .whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Notes> notesList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Notes post = new Notes();
                            post.setNoteTitle(doc.getString(Notes.NOTE_TITLE));
                            post.setNoteId(doc.getId());
                            post.setNoteDetails(doc.getString(Notes.NOTE_DETAILS));

                            notesList.add(post);
                        }


                        noteAdapter = new NoteAdapter(Note.this, notesList);

                        noteProgrssBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        recyclerView.setAdapter(noteAdapter);
                    }
                });


    }
}
