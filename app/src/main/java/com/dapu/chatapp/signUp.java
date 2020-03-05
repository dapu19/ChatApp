package com.dapu.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// class implements a sign up page/activity
// user will enter in name , email and password before being allowed to proceed
public class signUp extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private EditText mail;
    private EditText pass;
    private EditText cpass;
    private EditText name;
    private String email;
    private String password;
    private String conPassword;
    private String fullname;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(android.view.View view) {
        //  get entered user details and place them in string variables
        mail = findViewById(R.id.email);
        email = mail.getText().toString();
        pass = findViewById(R.id.password);
        password = pass.getText().toString();

        cpass = findViewById(R.id.conPassword);
        conPassword = cpass.getText().toString();
        System.out.println(conPassword);
        System.out.println(password);
        name = findViewById(R.id.fullName);
        fullname = name.getText().toString();
        if(conPassword.equals(password)) {  // checks if entered passwords match
            //updateUI(null);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUser(user.getUid(), fullname );
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
    }

    // user will be directed back to main page
    public void goToLogin(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }

    // will add user and relevant details to database
    public void addUser(String uid, String name){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        //myRef.setValue("Hello, World!");
        //String id = myRef.push().getKey();
        myRef.child(uid).child("Name").setValue(name);
    }

    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
            startActivity(myIntent);
        }else{  // if user signs up they will be sent to page/activity where they can enter details
            Intent myIntent = new Intent(getBaseContext(), EnterDetails.class);
            startActivity(myIntent);
        }
    }

    public void limiter(View view) {
    }

    public void getInfo(View view) {
    }
}