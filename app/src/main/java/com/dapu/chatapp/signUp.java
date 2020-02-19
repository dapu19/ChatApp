package com.dapu.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

public class signUp extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private EditText mail;
    private EditText pass;
    private EditText cpass;
    private EditText name;
    private String email;
    private String password;
    private String conPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(android.view.View view) {

        mail = findViewById(R.id.email);
        email = mail.getText().toString();

        pass = findViewById(R.id.password);
        password = pass.getText().toString();

        cpass = findViewById(R.id.conPassword);
        conPassword = cpass.getText().toString();
        System.out.println(conPassword);
        System.out.println(password);

        if(conPassword.equals(password)) {
            //updateUI(null);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
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

    public void goToLogin(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }

    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), upload.class);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), upload.class);
            startActivity(myIntent);
        }
    }
}