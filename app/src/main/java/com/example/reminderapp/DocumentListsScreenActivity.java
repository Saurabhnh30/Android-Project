package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.reminderapp.adapters.DocumentListAdapter;
import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Documents;
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

public class DocumentListsScreenActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    DocumentListAdapter documentListAdapter;
    Button docAddButton;
    RecyclerView notesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_lists_screen);

        firestore = FirebaseFirestore.getInstance();

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        docAddButton =  findViewById(R.id.docAddButton);

        docAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent( DocumentListsScreenActivity.this,  Documentpage.class);
                startActivity(intent);
            }
        });


        getAllDocumentsData();
    }


    public void deleteDocument(String id) {
        firestore.collection(new CollectionNames().getDocumentCollection())
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getAllDocumentsData();
                    }
                });
    }


    public void getAllDocumentsData() {
        firestore.collection(new CollectionNames().getDocumentCollection())
                .whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Documents> docsList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Documents post = new Documents();
                            post.setDocImage(doc.getString(Documents.DOC_IMAGE));
                            post.setDocTitle(doc.getString(Documents.DOC_TITLE));
                            post.setDocId(doc.getId());

                            docsList.add(post);
                        }

                        documentListAdapter = new DocumentListAdapter(DocumentListsScreenActivity.this, docsList);
                        notesRecyclerView.setAdapter(documentListAdapter);

                    }
                });
    }
}
