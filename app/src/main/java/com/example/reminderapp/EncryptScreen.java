package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reminderapp.helpers.CollectionNames;
import com.example.reminderapp.models.Encrypts;
import com.example.reminderapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class EncryptScreen extends AppCompatActivity
{
    FirebaseFirestore firestore;
    Button encryptadddatabutton;
    RecyclerView recyclerView;

    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    List<Encrypts> encryptsList;
    Encrypts undoEncrypt;

    AdapterEncrypt adapterEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_screen);

        setTitle("Encrypt Data");

        encryptadddatabutton = findViewById(R.id.encryptadddatabutton);
        encryptadddatabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EncryptScreen.this , encry.class);
                startActivity(intent);
            }
        });

        firestore = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.encryptview);

        encryptsList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(EncryptScreen.this));


        try {
            try {
                cipher = Cipher.getInstance("AES");
                decipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }

            secretKeySpec = new SecretKeySpec(encryptionKey, "AES");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        getAllData();
    }


//    public void deleteEncrypt(String id, final int position) {
//        firestore.collection(CollectionNames.ENCRYPT)
//                .document(id)
//                .delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
////                        getAllData();
//                        encryptsList.remove(position);
//                        adapterEncrypt.notifyItemRemoved(position);
//                    }
//                });
//
//    }


    public void getAllData() {
        firestore.collection(CollectionNames.ENCRYPT).whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Encrypts items = new Encrypts();
                            try {

                                items.setTitle(AESDecryptionMethod(doc.getString(Encrypts.TITLE)));
                                items._setEncryptId(doc.getId());
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            encryptsList.add(items);
                        }

                        adapterEncrypt = new AdapterEncrypt(EncryptScreen.this, encryptsList);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                        itemTouchHelper.attachToRecyclerView(recyclerView);
                        recyclerView.setAdapter(adapterEncrypt);
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
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(EncryptScreen.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_white)
                    .addSwipeRightActionIcon(R.drawable.ic_edit_white)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(EncryptScreen.this, R.color.colorPrimary))
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            final String selectedId = encryptsList.get(position)._getEncryptId();

            switch (direction) {
                case ItemTouchHelper.RIGHT: {
                    View prompt_view = LayoutInflater.from(EncryptScreen.this).inflate(R.layout.edit_promt_layout, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EncryptScreen.this);
                    alertDialogBuilder.setView(prompt_view);

                    final EditText postTitleET = prompt_view.findViewById(R.id.postTitleET);
                    final EditText postDescriptionET = prompt_view.findViewById(R.id.postDescriptionET);
                    postDescriptionET.setVisibility(View.GONE);

                    postTitleET.setText(encryptsList.get(position).getTitle());

                    alertDialogBuilder.setCancelable(false)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Encrypts updateEncryptData = new Encrypts();
                                    updateEncryptData.setTitle(AESEncryptionMethod(postTitleET.getText().toString()));
                                    updateEncryptData.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    firestore.collection(CollectionNames.ENCRYPT)
                                            .document(selectedId)
                                            .set(updateEncryptData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(EncryptScreen.this, "Note updated SUCCESS", Toast.LENGTH_LONG).show();
                                                    encryptsList.get(position).setTitle(postTitleET.getText().toString());
                                                    adapterEncrypt = new AdapterEncrypt(EncryptScreen.this, encryptsList);
                                                    recyclerView.setAdapter(adapterEncrypt);
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    recyclerView.setAdapter(adapterEncrypt);
                                }
                            });

                    alertDialogBuilder.create().show();
                    break;
                }
                case ItemTouchHelper.LEFT: {
                    firestore.collection(CollectionNames.ENCRYPT)
                            .document(selectedId)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        undoEncrypt = new Encrypts();
                                        undoEncrypt.setTitle(encryptsList.get(position).getTitle());
                                        undoEncrypt.setUserId(encryptsList.get(position).getUserId());
                                        undoEncrypt._setEncryptId(selectedId);
                                        Snackbar.make(recyclerView, "encrypt data " + encryptsList.get(position).getTitle()  + " deleted", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        encryptsList.add(undoEncrypt);
                                                        firestore.collection(CollectionNames.ENCRYPT).document(undoEncrypt._getEncryptId())
                                                                .set(undoEncrypt);

                                                        adapterEncrypt = new AdapterEncrypt(EncryptScreen.this, encryptsList);
                                                        recyclerView.setAdapter(adapterEncrypt);
                                                    }
                                                }).show();

                                        encryptsList.remove(position);

                                        adapterEncrypt = new AdapterEncrypt(EncryptScreen.this, encryptsList);
                                        recyclerView.setAdapter(adapterEncrypt);

                                    }
                                }
                            });
                    break;
                }
            }
        }
    };


    private String AESEncryptionMethod(String string){

        byte[] stringByte = string.getBytes();
        byte[] encryptedByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(stringByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        String returnString = null;

        try {
            returnString = new String(encryptedByte, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}
