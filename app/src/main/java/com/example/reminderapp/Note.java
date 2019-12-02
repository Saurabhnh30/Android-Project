package com.example.reminderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Notes;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Note extends AppCompatActivity
{
    Button btnnote;
    RecyclerView recyclerView;
    ProgressBar noteProgrssBar;
    EditText postTitleET, postDescriptionET;
    SwipeRefreshLayout noteActivitySwipeRefresh;

    NoteAdapter noteAdapter;
    List<Notes> notesList;
    Notes undoNote;
    String LOGGED_IN_USER_ID;

    FirebaseFirestore firestore;
    CollectionNames collectionNames;

    boolean onCreated = true;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Log.d("NOTE LOG ", "On Created method called");

        setTitle("Notes");

        btnnote =  findViewById(R.id.btnote);
        recyclerView = findViewById(R.id.recyclerid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesList = new ArrayList<>();
        LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        noteProgrssBar = findViewById(R.id.noteProgrssBar);
        noteActivitySwipeRefresh = findViewById(R.id.noteActivitySwipeRefresh);

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

        noteActivitySwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noteAdapter = new NoteAdapter(Note.this, notesList);
                recyclerView.setAdapter(noteAdapter);
            }
        });
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

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);

                        recyclerView.setAdapter(noteAdapter);
                    }
                });


    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(Note.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                    .addSwipeRightActionIcon(R.drawable.ic_edit_white)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(Note.this, R.color.colorPrimary))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final String selectedNoteId = notesList.get(position).getNoteId();

            switch (direction) {
                case ItemTouchHelper.RIGHT: {
                    View prompt_view = LayoutInflater.from(Note.this).inflate(R.layout.edit_promt_layout, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Note.this);
                    alertDialogBuilder.setView(prompt_view);

                    postTitleET = prompt_view.findViewById(R.id.postTitleET);
                    postDescriptionET = prompt_view.findViewById(R.id.postDescriptionET);

                    postTitleET.setText(notesList.get(position).getNoteTitle());
                    postDescriptionET.setText(notesList.get(position).getNoteDetails());

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    notesList.get(position).setNoteTitle(postTitleET.getText().toString());
                                    notesList.get(position).setNoteDetails(postDescriptionET.getText().toString());
                                    notesList.get(position).setUserId(LOGGED_IN_USER_ID);

                                    firestore.collection(new CollectionNames().getNotes())
                                            .document(selectedNoteId)
                                            .set(notesList.get(position))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(Note.this, "Note updated SUCCESS", Toast.LENGTH_LONG).show();
                                                    noteAdapter = new NoteAdapter(Note.this, notesList);
                                                    recyclerView.setAdapter(noteAdapter);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    alertDialogBuilder.create().show();

                    break;
                }
                case ItemTouchHelper.LEFT: {
                    firestore.collection(new CollectionNames().getNotes())
                            .document(selectedNoteId)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        undoNote = new Notes();
                                        undoNote.setNoteTitle(notesList.get(position).getNoteTitle());
                                        undoNote.setNoteDetails(notesList.get(position).getNoteDetails());
                                        undoNote.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        Snackbar.make(recyclerView, "Note " + notesList.get(position).getNoteTitle()  + " deleted", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        notesList.add(undoNote);
                                                        firestore.collection(new CollectionNames().getNotes())
                                                                .add(undoNote);

                                                        noteAdapter = new NoteAdapter(Note.this, notesList);
                                                        recyclerView.setAdapter(noteAdapter);
                                                    }
                                                }).show();

                                        notesList.remove(position);

                                        noteAdapter = new NoteAdapter(Note.this, notesList);
                                        recyclerView.setAdapter(noteAdapter);

                                    }
                                }
                            });
                    break;
                }
            }
        }
    };
}
