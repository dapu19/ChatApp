package com.dapu.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// class implements main page/activity which is displayed to a user who is not signed in/up
// allows users to sign in to an existing account or create a new account
public class MainActivity extends AppCompatActivity {//implements RoomListener{

    public static final String TAG = "YOUR-TAG-NAME";
    private EditText mail;
    private EditText pass;
    private String email;
    private String password;
    private Button login;
    private Button signUp;
    private FirebaseAuth mAuth;


    ProgressBar splashProgress;
    int SPLASH_TIME = 2500; //This is 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set content view and get FireBase instance
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    //method which uses FireBase supports to implement a login system.
    public void Login(android.view.View view) {
        // Validate and sign in
        mail = findViewById(R.id.email);
        email = mail.getText().toString();

        // get password and cast it to string
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

    // function used to direct  a new user to sign up page
    public void signUp(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), signUp.class);
        startActivity(myIntent);
    }

    // method which will direct new user to signUp or bring a user to the home page
    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) { // if user is not signed in they will be directed to signUp page/activity
            Intent myIntent = new Intent(getBaseContext(), signUp.class);
            startActivity(myIntent);
        }else{  // if user has signed in they will be directed to their home page
            Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
            startActivity(myIntent);
        }
    }


}
