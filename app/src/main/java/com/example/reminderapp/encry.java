package com.example.reminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class encry extends AppCompatActivity
{
    FirebaseFirestore firestore;

    EditText encrypttitle;
    TextView decrypttitle;
    Button encryptbutton, decryptbutton;

    private String publickey = " " ;
    private String privatekey = " " ;
    private byte[] encodeData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encry);

        firestore = FirebaseFirestore.getInstance();

        encrypttitle = findViewById(R.id.encrypttitle);
        encryptbutton = findViewById(R.id.encryptbutton);
        decrypttitle = findViewById(R.id.decrypttitle);
        decryptbutton = findViewById(R.id.decryptbutton);


        try
        {
            Map<String , Object> keymap = rsa.initKey();
            publickey =  rsa.getPublicKey(keymap);
            privatekey = rsa.getPrivateKey(keymap);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void encrypt(View view)
    {

        byte[] rsaData = encrypttitle.getText().toString().getBytes();

        try {
            encodeData = rsa.encryptByPublicKey(rsaData,publickey);
            String encodeStr = new BigInteger(1, encodeData).toString();

            HashMap<String, Object> data = new HashMap<>();
            data.put("title", encodeStr);

            firestore.collection("encrypt").add(data)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(
                                    encry.this,
                                    "Saved to firestore",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void deceypt(View view)
    {

        firestore.collection("encrypt").limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("ENCRYPT_LOG", task.getResult().getDocuments().get(0).getString("title"));

                        byte[] ed = task.getResult().getDocuments().get(0).getString("title").getBytes();

                        try {
                            byte[] decodeData = rsa.decryptByPrivateKey(ed,privatekey);
                            String decodeStr = new String(decodeData);
                            decrypttitle.setText(decodeStr);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}