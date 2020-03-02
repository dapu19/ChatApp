package com.dapu.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;

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

import java.util.ArrayList;

import java.util.List;
import java.util.Map;


public class chatActivity extends AppCompatActivity{
    LinearLayout layout;
    RelativeLayout layout_2;
    private EditText editText;
    private TextView name;
    ScrollView scrollView;
    String partner_UID;
    String roomid;

    private FirebaseAuth mAuth;

    List<Message> messages = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if(getIntent().hasExtra("Partner_UID")) {
            partner_UID = getIntent().getStringExtra("Partner_UID");
            //Log.e("uid", getIntent().getStringExtra("Partner_UID"));
        }

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        final String my_UID = user.getUid();
        if(0 < my_UID.compareTo(partner_UID)) {
            roomid = my_UID + "-" + partner_UID;
        }else{
            roomid = partner_UID + "-" + my_UID;
        }
        Log.e("roomid", roomid);
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
                Intent myIntent = new Intent(getBaseContext(), ListOfMatchesActivity.class);
                startActivity(myIntent);
                finish();
            }
        });





        DatabaseReference nameReference = database.getReference("users/" + partner_UID);
        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot.child("Name");
                String partnerName = snapshot.getValue().toString();
                name.setText(partnerName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Database Error");
            }
        });


        DatabaseReference ref = database.getReference("rooms/" + roomid);

        ref.addChildEventListener(new ChildEventListener() {
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

        public void addMessageBox(String message, int type){
            TextView textView = new TextView(chatActivity.this);
            textView.setTextSize(32);
            textView.setText(message);

            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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


    public void sendMessage(View view){
        editText = findViewById(R.id.editText);
        messages.clear();
        String message = editText.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        Log.e("message", message);
        if (message.length() > 0) {
            editText.setText("");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("rooms/" + roomid);
            Long time = System.currentTimeMillis() / 1000L;

            Message toSend = new Message(message, UID, time);
            Log.e("UID",toSend.getUID());
            myRef.child(time.toString()).setValue(toSend);
        }
    }

    }
