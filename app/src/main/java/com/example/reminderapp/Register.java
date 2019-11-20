package com.example.reminderapp;




import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText user,pass,email,phone,pin;
    private Button register;
    private TextView login;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    TextView ClickToGoLogin;
    Uri imgUri;

    public static final int PICK_IMAGE_REQUEST = 1;

    Button openGalleryBtn;
    ImageView userAvatarImageView;
    ProgressBar signupProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = findViewById(R.id.signupUsernameEditText);
        email = findViewById(R.id.signupEmailEditText);
        pass = findViewById(R.id.signupPasswordEditText);
        phone = findViewById(R.id.signupPhoneEditText);
        pin = findViewById(R.id.signupPinEditText);
        register =  findViewById(R.id.signupSubmitButton);
        login = findViewById(R.id.ClickToGoLogin);
        ClickToGoLogin = findViewById(R.id.ClickToGoLogin);
        signupProgressBar = findViewById(R.id.signupProgressBar);
        openGalleryBtn = findViewById(R.id.openGalleryBtn);
        userAvatarImageView = findViewById(R.id.userAvatarImageView);

        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("userProfiles");


        openGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupProgressBar.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                userRegister();
            }
        });

        ClickToGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            imgUri = data.getData();

            openGalleryBtn.setVisibility(View.GONE);
            userAvatarImageView.setVisibility(View.VISIBLE);

            Picasso.get().load(imgUri).into(userAvatarImageView);
        }
    }


    public void userRegister() {
        final String user1 = user.getText().toString();
        final String email1 = email.getText().toString();
        final String pass1 = pass.getText().toString();
        final String pin1 = pin.getText().toString().trim();
        final String phone1 = phone.getText().toString();


        if (imgUri != null) {
            firebaseAuth.createUserWithEmailAndPassword(email1, pass1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            final String REGISTERED_USER_ID = task.getResult().getUser().getUid();
                            if (task.isSuccessful()) {

                                final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

                                fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) throw task.getException();
                                        return fileRef.getDownloadUrl();                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Users userdata = new Users();
                                            userdata.setUsername(user1);
                                            userdata.setEmail(email1);
                                            userdata.setPassword(pass1);
                                            userdata.setLoginPin(pin1);
                                            userdata.setPhone(phone1);
                                            userdata.setUserAvatar(task.getResult().toString());


                                            firebaseFirestore.collection(new CollectionNames().getUsersCollection())
                                                    .document(REGISTERED_USER_ID)
                                                    .set(userdata)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                signupProgressBar.setVisibility(View.GONE);
                                                                register.setVisibility(View.VISIBLE);
                                                                startActivity(new Intent(Register.this, MainActivity.class));
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(
                                        Register.this,
                                        "Signup ERRORRR",
                                        Toast.LENGTH_LONG
                                ).show();
                                signupProgressBar.setVisibility(View.VISIBLE);
                                register.setVisibility(View.GONE);
                            }
                        }
                    });
        }
        else {
            Toast.makeText(Register.this, "User avatar cannot be empty", Toast.LENGTH_LONG).show();
        }
    }



}
