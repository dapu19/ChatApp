package com.dapu.chatapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// implements messaging service for communicating with Karen ChatBot
public class KarenActivity extends AppCompatActivity {

    private EditText editText;
    LinearLayout layout;
    RelativeLayout layout_2;
    ScrollView scrollView;
    private TextView name;

    private FirebaseAuth mAuth;
    String roomid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karen);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        final String my_UID = user.getUid();
        roomid = my_UID + "-karen";

        DatabaseReference roomRef = database.getReference("users/" + roomid);

        roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("snapshot", dataSnapshot.toString());
                if (!(dataSnapshot.child(roomid).exists())) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("rooms/" + (roomid));
                    Long time = System.currentTimeMillis() / 1000L;
                    myRef.child(time.toString()).setValue(new Message("", my_UID, time));
                    Log.e("created Room", roomid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Log.e("Error", "Database Error");
            }

            }

        );

        layout = findViewById(R.id.layout1);
        layout_2 = findViewById(R.id.layout2);

        scrollView = findViewById(R.id.scrollView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.theirName);

        toolbar.setNavigationIcon(R.drawable.ic_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        name.setText("Karen");


        DatabaseReference ref = database.getReference("rooms/" + roomid);

        ref.addChildEventListener(new ChildEventListener() {
            // handles new messages by passing message text to assMessageBox function , type indicates message sent by Karen or user
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                String messageText = message.getText();
                String userName = message.getUID();
                if (messageText.length() > 0) {

                    if (userName.equals(mAuth.getCurrentUser().getUid())) {
                        addMessageBox(messageText, 1);
                        Log.e("my Message", messageText);
                    } else {
                        addMessageBox(messageText, 2);
                        Log.e("your Message", messageText);
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
    // function handles displaying message to user by creating wrapper for text, message type indicates if message is sent by user or Karen
    public void addMessageBox(String message, int type){
        TextView textView = new TextView(KarenActivity.this);
        textView.setTextSize(20);
        textView.setText(message);
        textView.setPadding(25,25,25,25);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(20,10,20,10);
        //textView.setLayoutParams(lp);
        textView.setTextColor(Color.parseColor("#ffffff"));
        //LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 2) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.their_message);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.my_message);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }



    // function handles sending a message by storing it in the FireBase database
    public void sendMessage(View view) throws IOException {
        editText = findViewById(R.id.editText);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        final String my_UID = user.getUid();
        String message = editText.getText().toString();
        roomid = my_UID + "-karen";
        // message is only sent/stored if it has content, (Has nonzero number of characters.)
        if (message.length() > 0) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Log.e("roomid",roomid);
            DatabaseReference myRef = database.getReference("rooms/" + roomid);
            Long time = System.currentTimeMillis() / 1000L;

            Message toSend = new Message(message, my_UID, time);
            Log.e("UID",toSend.getUID());
            myRef.child(time.toString()).setValue(toSend);
        }
        new messageKaren().execute(message);
        editText.setText("");
    }
}
