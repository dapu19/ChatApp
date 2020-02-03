package com.dapu.chatapp;


import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class homeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void message(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), messageActivity.class);
        startActivity(myIntent);
    }
}