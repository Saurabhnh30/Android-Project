package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class NoteDisplayActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionNames collNames;

    TextView docDisplayTitleTV, docDisplayDetailsTV;
    ImageView docDisplayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        firestore = FirebaseFirestore.getInstance();
        collNames = new CollectionNames();

        docDisplayDetailsTV = findViewById(R.id.docDisplayDetailsTV);
        docDisplayTitleTV = findViewById(R.id.docDisplayTitleTV);
        docDisplayImageView = findViewById(R.id.docDisplayImageView);


        firestore.collection(collNames.getNotes())
                .document(getIntent().getStringExtra(Posts.POST_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Posts post = task.getResult().toObject(Posts.class);

                        if (post.getPosttitle() == null) {
                            docDisplayTitleTV.setText(post.getAboutImage());
                        }
                        else {
                            docDisplayTitleTV.setText(post.getPosttitle());
                        }

                        if (post.getImagesList().size() > 0) {
                            Picasso.get().load(post.getImagesList().get(0)).into(docDisplayImageView);
                        }
                    }
                });
    }
}
