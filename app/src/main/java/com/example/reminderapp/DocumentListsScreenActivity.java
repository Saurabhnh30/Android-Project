package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.reminderapp.adapters.DocumentListAdapter;
import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DocumentListsScreenActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    DocumentListAdapter documentListAdapter;

    RecyclerView notesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_lists_screen);

        firestore = FirebaseFirestore.getInstance();

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        firestore.collection(new CollectionNames().getNotes())
                .whereEqualTo(Posts.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Posts> postsList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Posts post = new Posts();
                            post.setPosttitle(doc.getString(Posts.POST_TITLE));
                            post.setAboutImage(doc.getString(Posts.ABOUT_IMAGE));
                            post.setPosdid(doc.getId());

                            postsList.add(post);
                        }

                        documentListAdapter = new DocumentListAdapter(DocumentListsScreenActivity.this, postsList);
                        notesRecyclerView.setAdapter(documentListAdapter);

                    }
                });
    }
}
