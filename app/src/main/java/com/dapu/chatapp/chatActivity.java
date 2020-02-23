package com.dapu.chatapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.firebase.client.Firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class chatActivity extends AppCompatActivity{

    private String messageText;
    private String messageUser;
    private long messageTime;
    private EditText editText;

    private FirebaseAuth mAuth;

    List<String> db = new ArrayList<String>();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        get_db();





    }



    private void get_db() {
        mAuth = FirebaseAuth.getInstance();
        final String partner_UID = getIntent().getStringExtra("Partner_UID");
        FirebaseUser user = mAuth.getCurrentUser();
        final String my_UID = user.getUid();


        Log.e("Partner_UID", partner_UID);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("rooms");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("snapshot", snapshot.toString());
                    Log.e("room", snapshot.getKey());
                    db.add(snapshot.getKey());
                    Log.e("rooms", db.toString());

                }
                String room = find_room();
                Log.e("Def_rooms", room);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Database Error");
            }
        });


    }
    public String find_room(){

        mAuth = FirebaseAuth.getInstance();
        final String partner_UID = getIntent().getStringExtra("Partner_UID");
        FirebaseUser user = mAuth.getCurrentUser();
        final String my_UID = user.getUid();

        String roomid = partner_UID + "-" + my_UID;
        Log.e("Finding room", roomid + " " + db.toString());

        if (db.contains(roomid)) {
            Log.e("Found Room", roomid);
            return roomid;
        }
        roomid = my_UID + "-" + partner_UID;
        if (db.contains(roomid)) {
            Log.e("Found Room", roomid);
            return roomid;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("rooms/" + roomid);
        Long time = System.currentTimeMillis() / 1000L;
        myRef.child(time.toString()).setValue(new Message("", my_UID));
        Log.e("created Room", roomid);

        return roomid;
    }


    public void sendMessage(View view){
        editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String UID = user.getUid();
        Log.e("message", message);
        if (message.length() > 0) {
            String roomid = find_room();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("rooms/" + roomid);
            Long time = System.currentTimeMillis() / 1000L;

            Message toSend = new Message(message, UID);
            myRef.child(time.toString()).setValue(toSend);

        }

    }







}