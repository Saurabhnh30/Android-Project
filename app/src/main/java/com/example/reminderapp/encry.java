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

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class encry extends AppCompatActivity
{
    FirebaseFirestore firestore;

    EditText encrypttitle;
    TextView decrypttitle;
    Button encryptbutton, decryptbutton;
    List<Byte> ed = new ArrayList<Byte>();

    private String publickey = " " ;
    private String privatekey = " " ;
    private byte[] encodeData = null;

    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;

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


//        try
//        {
//            Map<String , Object> keymap = rsa.initKey();
//            publickey =  rsa.getPublicKey(keymap);
//            privatekey = rsa.getPrivateKey(keymap);
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }


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
    }



    public void encrypt(View view)
    {



        String s = AESEncryptionMethod(encrypttitle.getText().toString());
        Log.d("MAIN_ACT_LOG", "encrypted data : " + s);

        encrypttitle.setText("");

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", s);

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

    // --
    public void deceypt(View view)
    {

        firestore.collection("encrypt").limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String encryptData = task.getResult().getDocuments().get(0).getString("title");
                        Log.d("ENCRYPT_LOG", encryptData);

                        try {
                            decrypttitle.setText(AESDecryptionMethod(encryptData));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

    }


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