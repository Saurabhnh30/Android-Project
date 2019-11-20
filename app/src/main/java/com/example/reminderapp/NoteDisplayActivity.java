package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Documents;
import com.example.reminderapp.models.Notes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class NoteDisplayActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionNames collNames;

    TextView docDisplayTitleTV, docDisplayDetailsTV;
    ImageView docImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        firestore = FirebaseFirestore.getInstance();
        collNames = new CollectionNames();

        docDisplayDetailsTV = findViewById(R.id.docDisplayDetailsTV);
        docDisplayTitleTV = findViewById(R.id.docDisplayTitleTV);
        docImageView = findViewById(R.id.docImageView);

        if (getIntent().getExtras().getString("posttype").equals("DOC")) {
            docDisplayDetailsTV.setVisibility(View.GONE);
            docImageView.setVisibility(View.VISIBLE);

            firestore.collection(collNames.getDocumentCollection())
                    .document(getIntent().getExtras().getString("postid"))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Documents doc = task.getResult().toObject(Documents.class);
                            docDisplayTitleTV.setText(doc.getDocTitle());
                            Picasso.get().load(doc.getDocImage()).into(docImageView);
                        }
                    });
        }


//        firestore.collection(collNames.getNotes())
//                .document(getIntent().getStringExtra(Notes.NOTE_ID))
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Notes note = task.getResult().toObject(Notes.class);
//
//                        docDisplayTitleTV.setText(note.getNoteTitle());
//                        docDisplayDetailsTV.setText(note.getNoteDetails());
//                    }
//                });
    }
}
