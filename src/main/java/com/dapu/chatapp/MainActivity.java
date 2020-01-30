package com.dapu.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {//implements RoomListener{

    //private String channelID = "CHANNEL_ID_FROM_YOUR_SCALEDRONE_DASHBOARD";
    //private String roomName = "observable-room";
    private EditText editText;
    private Button login;
    private Button signUp;
    //private Scaledrone scaledrone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editText = (EditText) findViewById(R.id.editText);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
    }

    public void Login(android.view.View view) {
        // Validate and sign in
    }

    public void signUp(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), signUp.class);
        startActivity(myIntent);
    }
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
