package com.dapu.chatapp;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView email;
    private TextView fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        if(user != null){
            String email1 = user.getEmail();
            String fn1 = user.getDisplayName();
            fullName.setText(fn1);
            email.setText(email1);
        }
    }

    public void message(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), ListOfMatchesActivity.class);
        startActivity(myIntent);
    }

    public void logout(android.view.View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }
}