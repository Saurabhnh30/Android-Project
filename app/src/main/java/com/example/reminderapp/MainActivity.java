package com.example.reminderapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private EditText user,pass;
    private Button login;
    private TextView register;
    ProgressBar loginPrograssbar;

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser != null) {
            finish();
            startActivity(new Intent(MainActivity.this, home.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Login");

        user = findViewById(R.id.loginEmailEditText);
        pass = findViewById(R.id.loginpasswordEditText);
        login = findViewById(R.id.loginSubmitButton);
        register = findViewById(R.id.register);
        loginPrograssbar = findViewById(R.id.loginPrograssbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                loginPrograssbar.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    loginPrograssbar.setVisibility(View.GONE);
                                    register.setVisibility(View.VISIBLE);
                                    finish();
                                    startActivity(new Intent(MainActivity.this, home.class));
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("LOGIN_ACTIVITY ", e.getMessage());
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
                finish();
            }
        });

    }



//    public void loginUser(String user,String pass){
//
//        RoomDAO roomDAO = appDatabase.getRoomDAO();
//        UsernamePassword temp = roomDAO.getUserwithUsername(user);
//        //Toast.makeText(this, temp.getPassword(), Toast.LENGTH_SHORT).show();
//        if(temp==null){
//            Toast.makeText(MainActivity.this,"Invalid username",Toast.LENGTH_SHORT).show();
//        }
//        else{
//            if(temp.getPassword().equals(pass)){
//                temp.setIsloggedIn(1);
//                roomDAO.Update(temp);
//                AppDatabase.destroyInstance();
//                Intent intent = new Intent(MainActivity.this,home.class);
//                startActivity(intent);
//                finish();
//            }
//            else{
//                Toast.makeText(MainActivity.this,"Invalid Password",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
}
