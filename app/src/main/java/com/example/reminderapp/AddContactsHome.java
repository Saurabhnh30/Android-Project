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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Contacts;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddContactsHome extends AppCompatActivity {

    static final int PICK_IMAGE_REQUEST = 1;

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    Uri imgUri;

    ImageView contactAvatarIV;
    Button addcontactphotoBtn, contactSubmitBtn, contactUpdateBtn, contactUpdateCancelBtn;
    EditText contactFirstnameET, contactSirnameET, contactCompanyET, contactPhoneET, contactEmailET;
    ProgressBar contactSubmitPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts_home);

        setTitle(" Add Your Contacts");
        Intent intentUpdate = getIntent();

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("contactAvatars");

        contactAvatarIV = findViewById(R.id.contactAvatarIV);
        addcontactphotoBtn = findViewById(R.id.addcontactphotoBtn);
        contactSubmitBtn = findViewById(R.id.contactSubmitBtn);
        contactFirstnameET = findViewById(R.id.contactFirstnameET);
        contactSirnameET = findViewById(R.id.contactSirnameET);
        contactCompanyET = findViewById(R.id.contactCompanyET);
        contactPhoneET = findViewById(R.id.contactPhoneET);
        contactEmailET = findViewById(R.id.contactEmailET);
        contactSubmitPB = findViewById(R.id.contactSubmitPB);
        contactUpdateBtn = findViewById(R.id.contactUpdateBtn);
        contactUpdateCancelBtn = findViewById(R.id.contactUpdateCancelBtn);


        addcontactphotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        contactSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeContactImageToFirestorage();
            }
        });

        contactUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeContactImageToFirestorage();
            }
        });


        if (intentUpdate.getStringExtra(Contacts.CONTACT_ID) != null) {
            contactUpdateCancelBtn.setVisibility(View.VISIBLE);
            contactUpdateBtn.setVisibility(View.VISIBLE);
            contactSubmitBtn.setVisibility(View.GONE);

            contactEmailET.setText(intentUpdate.getStringExtra(Contacts.CONTACT_EMAIL));
            contactPhoneET.setText(intentUpdate.getStringExtra(Contacts.CONTACT_PHONE));
            contactFirstnameET.setText(intentUpdate.getStringExtra(Contacts.CONTACT_FIRSTNAME));
            contactSirnameET.setText(intentUpdate.getStringExtra(Contacts.CONTACT_SIRNAME));
            contactCompanyET.setText(intentUpdate.getStringExtra(Contacts.CONTACT_COMPANY));

            if (intentUpdate.getStringExtra(Contacts.CONTACT_AVATAR) != null) {
                Picasso.get().load(intentUpdate.getStringExtra(Contacts.CONTACT_AVATAR)).into(contactAvatarIV);
            }
        }


        contactUpdateCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(AddContactsHome.this, home.class));
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

            Picasso.get().load(imgUri).into(contactAvatarIV);
        }
    }

    public void storeContactImageToFirestorage() {
        contactSubmitPB.setVisibility(View.VISIBLE);
        contactSubmitBtn.setVisibility(View.GONE);

        final Contacts contact = new Contacts();
        contact.setContactCompany(contactCompanyET.getText().toString());
        contact.setContactEmail(contactEmailET.getText().toString());
        contact.setContactFirstname(contactFirstnameET.getText().toString());
        contact.setContactSirname(contactSirnameET.getText().toString());
        contact.setContactPhone(contactPhoneET.getText().toString());
        contact.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
                                Uri downloadUri = task.getResult();

                                contact.setContactAvatar(downloadUri.toString());

                                if (getIntent().getStringExtra(Contacts.CONTACT_ID) != null) updateContact(contact);
                                else insertContact(contact);
                            }
                        }
                    });
        }
        else {
            if (getIntent().getStringExtra(Contacts.CONTACT_ID) != null) {
                contact.setContactAvatar(getIntent().getStringExtra(Contacts.CONTACT_AVATAR));
                updateContact(contact);
            }
            else insertContact(contact);
        }
    }

    public void insertContact(Contacts contact) {
        firestore.collection(CollectionNames.CONTACTS).add(contact)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        contactSubmitPB.setVisibility(View.GONE);
                        contactSubmitBtn.setVisibility(View.VISIBLE);

                        imgUri = null;
                        Picasso.get().load(imgUri).into(contactAvatarIV);
                        contactEmailET.setText("");
                        contactCompanyET.setText("");
                        contactPhoneET.setText("");
                        contactFirstnameET.setText("");
                        contactSirnameET.setText("");

                        finish();
                        startActivity(new Intent(AddContactsHome.this, home.class));
                    }
                });
    }


    public void updateContact(Contacts contact) {
        WriteBatch batch = firestore.batch();

        DocumentReference doc = firestore.collection(CollectionNames.CONTACTS).document(getIntent().getStringExtra(Contacts.CONTACT_ID));

        batch.update(doc, Contacts.CONTACT_AVATAR, contact.getContactAvatar());
        batch.update(doc, Contacts.CONTACT_EMAIL, contact.getContactEmail());
        batch.update(doc, Contacts.CONTACT_FIRSTNAME, contact.getContactFirstname());
        batch.update(doc, Contacts.CONTACT_SIRNAME, contact.getContactSirname());
        batch.update(doc, Contacts.CONTACT_COMPANY, contact.getContactCompany());
        batch.update(doc, Contacts.CONTACT_PHONE, contact.getContactPhone());

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    contactSubmitPB.setVisibility(View.GONE);
                    contactSubmitBtn.setVisibility(View.VISIBLE);

                    imgUri = null;
                    Picasso.get().load(imgUri).into(contactAvatarIV);
                    contactEmailET.setText("");
                    contactCompanyET.setText("");
                    contactPhoneET.setText("");
                    contactFirstnameET.setText("");
                    contactSirnameET.setText("");

                    finish();
                    startActivity(new Intent(AddContactsHome.this, home.class));
                }
            }
        });
    }
}
