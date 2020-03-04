package com.dapu.chatapp;

// implements a splash page when app is started
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity{
    int SPLASH_TIME = 2500; //This is 2.5 seconds
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, SPLASH_TIME);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            }
        }, SPLASH_TIME);

    }

    // if user is not logged in sends them to main page where they can sign in/up otherwise sends the logged in user to their home page
    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
            startActivity(myIntent);
        }
    }
}
