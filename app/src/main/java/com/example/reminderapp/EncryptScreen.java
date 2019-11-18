package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class EncryptScreen extends AppCompatActivity
{
    FirebaseFirestore firestore;
    Button encryptadddatabutton;
    RecyclerView recyclerView;

    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

    AdapterEncrypt adapterEncrypt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_screen);

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


    public void getAllData() {
        firestore.collection("encrypt").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<HashMap<String, String>> itemList = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            HashMap<String, String> items = new HashMap<>();
                            try {
                                items.put("title", AESDecryptionMethod(doc.getString("title")));
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            itemList.add(items);
                        }

                        adapterEncrypt = new AdapterEncrypt(EncryptScreen.this, itemList);
                        recyclerView.setAdapter(adapterEncrypt);
                    }
                });

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
