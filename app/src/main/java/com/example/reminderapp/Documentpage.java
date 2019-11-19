package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Posts;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Documentpage extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public static final int PICK_IMAGE_REQUEST = 1;
    Uri imgUri;

    ImageView noteImageIV;
    Button getImageFromGalleryBtn, submitImageNoteBtn, goToDocListActivityBtn;
    TextView aboutImageTV;
    ProgressBar imgUploadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentpage);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("postUploads");

        noteImageIV = findViewById(R.id.noteImageIV);
        getImageFromGalleryBtn = findViewById(R.id.getImageFromGalleryBtn);
        submitImageNoteBtn = findViewById(R.id.submitImageNoteBtn);
        aboutImageTV = findViewById(R.id.aboutImageTV);
        imgUploadProgressBar = findViewById(R.id.imgUploadProgressBar);
        goToDocListActivityBtn =  findViewById(R.id.goToDocListActivityBtn);

        getImageFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        goToDocListActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Documentpage.this , DocumentListsScreenActivity.class);
                startActivity(intent);
            }
        });

        submitImageNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUploadProgressBar.setVisibility(View.VISIBLE);
                submitImageNoteBtn.setVisibility(View.GONE);
                uploadImagePost();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {

            imgUri = data.getData();

            Picasso.get().load(imgUri).into(noteImageIV);
        }
    }

    // allows you to select image from gallery
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // this function is returns the extension of the image
    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    public void uploadImagePost() {
        if (imgUri != null) {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadedImg = task.getResult();

                        List<String> uploadImgList = new ArrayList<>();
                        uploadImgList.add(downloadedImg.toString());

                        Posts post = new Posts();
                        post.setAboutImage(aboutImageTV.getText().toString());
                        post.setImagesList(uploadImgList);
                        post.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        firestore.collection(new CollectionNames().getNotes()).add(post)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        submitImageNoteBtn.setVisibility(View.VISIBLE);
                                        imgUploadProgressBar.setVisibility(View.GONE);

                                        aboutImageTV.setText("");
                                        imgUri = null;
                                        Picasso.get().load(imgUri).into(noteImageIV);
                                    }
                                });
                    }
                }
            });
        }
    }

}
