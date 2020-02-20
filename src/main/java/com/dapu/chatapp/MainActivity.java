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

public class MainActivity extends AppCompatActivity {//implements RoomListener{

    //private String channelID = "CHANNEL_ID_FROM_YOUR_SCALEDRONE_DASHBOARD";
    //private String roomName = "observable-room";
    public static final String TAG = "YOUR-TAG-NAME";
    private EditText mail;
    private EditText pass;
    private String email;
    private String password;
    private Button login;
    private Button signUp;
    private FirebaseAuth mAuth;
    //private Scaledrone scaledrone;

    ProgressBar splashProgress;
    int SPLASH_TIME = 2500; //This is 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

/*

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
*/
    public void Login(android.view.View view) {
        // Validate and sign in
        mail = findViewById(R.id.email);
        email = mail.getText().toString();

        pass = findViewById(R.id.password);
        password = pass.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void signUp(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), signUp.class);
        startActivity(myIntent);
    }


    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), signUp.class);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
            startActivity(myIntent);
        }
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editText = (EditText) findViewById(R.id.editText);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
    } */

    //@Override
    //public void onOpen(Room room) {
        //System.out.println("Connected to room");
    //}

    //@Override
    //public void onOpenFailure(Room room, Exception ex) {
        //System.err.println(ex);
    //}

    //@Override
    //public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        //TODO
    //}

}
