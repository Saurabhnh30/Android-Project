package com.example.reminderapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.adapters.ContactsAdapter;
import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Contacts;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class home extends AppCompatActivity
{
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ContactsAdapter contactsAdapter;
    List<Contacts> contactsList;

    Button rmd, addnote, logout ,encry, update , docbutton, addHomeContact;
    TextView usernameTV, emailTV, phoneTV;
    ImageView userAvatarHomeActivity;
    RecyclerView contactRecyclerView;
    ProgressBar contactProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        contactsList = new ArrayList<>();

        usernameTV = findViewById(R.id.usernameTV);
        phoneTV = findViewById(R.id.phoneTV);
        emailTV = findViewById(R.id.emailTV);
        encry = findViewById(R.id.encry);
        contactRecyclerView = findViewById(R.id.contactRecyclerView);
        contactProgressBar = findViewById(R.id.contactProgressBar);
        contactRecyclerView.setHasFixedSize(true);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAvatarHomeActivity = findViewById(R.id.userAvatarHomeActivity);

        logout = findViewById(R.id.logout);

        getLoggedInUserData();
        getUserContacts();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(home.this, MainActivity.class));
            }
        });

        rmd = findViewById(R.id.rmd);
        rmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(home.this,MainPage.class);
                startActivity(a);
            }
        });

        addnote =  findViewById(R.id.addnote);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(home.this,Note.class);
                startActivity(b);
            }
        });

        encry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(home.this, EncryptScreen.class);
                startActivity(c);

            }
        });

        update = findViewById(R.id.Update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent d = new Intent(home.this,Updateprofile.class);
                startActivity(d);
            }
        });


        docbutton =  findViewById(R.id.docbutton);
        docbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent e = new Intent(home.this ,DocumentListsScreenActivity.class);
                startActivity(e);
            }
        });


        addHomeContact = findViewById(R.id.addHomeContact);
        addHomeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent f = new Intent(home.this, AddContactsHome.class);
                startActivity(f);
            }
        });

    }


    public void getLoggedInUserData() {
        firestore.collection(new CollectionNames().getUsersCollection()).document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        usernameTV.setText(task.getResult().getString(Users.USERNAME));
                        emailTV.setText(task.getResult().getString(Users.EMAIL));
                        phoneTV.setText(task.getResult().getString(Users.PHONE));

                        Picasso.get().load(task.getResult().getString(Users.USER_AVATAR)).into(userAvatarHomeActivity);
                    }
                });
    }

    public void getUserContacts() {
        firestore.collection(CollectionNames.CONTACTS).whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Contacts contact = new Contacts();

                                contact._setContactId(doc.getId());
                                contact.setUserId(doc.getString(Users.USER_ID));
                                contact.setContactAvatar(doc.getString(Contacts.CONTACT_AVATAR));
                                contact.setContactPhone(doc.getString(Contacts.CONTACT_PHONE));
                                contact.setContactEmail(doc.getString(Contacts.CONTACT_EMAIL));
                                contact.setContactFirstname(doc.getString(Contacts.CONTACT_FIRSTNAME));
                                contact.setContactSirname(doc.getString(Contacts.CONTACT_SIRNAME));
                                contact.setContactCompany(doc.getString(Contacts.CONTACT_COMPANY));

                                contactsList.add(contact);
                            }

                            contactProgressBar.setVisibility(View.GONE);

                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                            itemTouchHelper.attachToRecyclerView(contactRecyclerView);

                            contactsAdapter = new ContactsAdapter(home.this, contactsList);
                            contactRecyclerView.setAdapter(contactsAdapter);
                        }
                    }
                });
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(home.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                    .addSwipeRightActionIcon(R.drawable.ic_delete_white)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(home.this, R.color.red))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final String selectedContactId = contactsList.get(position)._getContactId();

            firestore.collection(CollectionNames.CONTACTS).document(selectedContactId)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                contactsList.remove(position);

                                contactsAdapter = new ContactsAdapter(home.this, contactsList);
                                contactRecyclerView.setAdapter(contactsAdapter);
                            }
                        }
                    });
        }
    };


}
