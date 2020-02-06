package com.dapu.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {//implements RoomListener{

    //private String channelID = "CHANNEL_ID_FROM_YOUR_SCALEDRONE_DASHBOARD";
    //private String roomName = "observable-room";
    private EditText email;
    private EditText password;
    private Button login;
    private Button signUp;
    //private Scaledrone scaledrone;



        ProgressBar splashProgress;
        int SPLASH_TIME = 2500; //This is 2.5 seconds

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.spash);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do any action here. Now we are moving to next page
                    //Intent mySuperIntent = new Intent(ActivitySplash.this, ActivityHome.class);
                    //startActivity(mySuperIntent);
                    setContentView(R.layout.activity_main);
                    //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                    //finish();

                }
            }, SPLASH_TIME);
        }

 /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editText = (EditText) findViewById(R.id.editText);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
    } */



    public void Login(android.view.View view) {
        // Validate and sign in
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Sender sender = new Sender();
        sender.execute(email.getText().toString(),password.getText().toString());
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
