package com.dapu.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

// class implements page/activity where user will enter details about themselves
// user details are stored on FireBase
public class userInfo extends AppCompatActivity {
    int SPLASH_TIME = 2500; //This is 2.5 seconds
    private FirebaseAuth mAuth;
    private ArrayList<String> intrests = new ArrayList<String>();
    private ArrayList<ToggleButton> buttons = new ArrayList<>();
    private ToggleButton tb;
    private ToggleButton tb1;
    private ToggleButton choice1;
    private ToggleButton choice2;
    private ToggleButton choice3;
    private ToggleButton choice4;
    private ToggleButton choice5;
    int count = 0; // used to limit number of choices
    boolean b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mAuth = FirebaseAuth.getInstance();
        buttons.add(choice1);
        buttons.add(choice2);
        buttons.add(choice3);
        buttons.add(choice4);
        buttons.add(choice5);
    }

    // implements fields which the user will chose their interests from
    public void getInfo(android.view.View view){
        for(int i = 0; i < 9; i++ ){
            String str = "field" + i;
            int id =getResources().getIdentifier(str,"id", getPackageName());
            tb =findViewById(id);
            if(tb.isChecked()){
                String s = tb.getText().toString();
                System.out.println(s);
                intrests.add(s);
            }
        }
        FirebaseUser user = mAuth.getCurrentUser();
        addUser(user.getUid(), intrests );
        updateUI(user);
    }

    // this implants the limit on number of interest , limit is 5
    public void limiter(android.view.View view){
        //count = 0;
        for(int i = 0; i < 9; i++ ){
            b = false;
            String str = "field" + i;
            int id =getResources().getIdentifier(str,"id", getPackageName());
            tb = findViewById(id);
            System.out.println(count);
            if(tb.isChecked()) {
                tb.setTextColor(Color.parseColor("#8D07A3"));
                if (count < 5) {
                    tb1 = buttons.get(count);
                    for(int j=0; j < 5; j++){
                        choice1 = buttons.get(j);
                        if (tb == choice1){
                            b = true;
                        }
                    }
                    if (b){
                        continue;
                    }
                    if(tb1 != null){
                        tb1.setChecked(false);
                    }
                    tb1 = tb;
                    buttons.set(count, tb1);
                    System.out.println("assigning button, inc count");
                    count ++;
                }else{
                    count = 0;
                }
            }else{
                tb.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    public void addUser(String uid, ArrayList interests){
        // Write a message to the database containing entered user details
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(uid).child("Interests").setValue(interests);

    }

    // if user is not logged in will be sent ot main page where they can sign in/up otherwise sends logged in user to the upload page
    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), upload.class);
            startActivity(myIntent);
        }
    }
}
