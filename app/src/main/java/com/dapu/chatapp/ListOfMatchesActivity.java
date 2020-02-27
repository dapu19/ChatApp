package com.dapu.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.HashMap;
import java.util.Map;

import com.firebase.client.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.ArrayList;
import java.util.List;

public class ListOfMatchesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    private FirebaseAuth mAuth;
    final HashMap<String, String> name_to_id = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_matches);
        list = (ListView)findViewById(R.id.listView);
        list.setOnItemClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        final ArrayList arrayList = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //Start of attempt to just list matches
        final ArrayList myList = new ArrayList();

        DatabaseReference myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.e("UID", user.getUid());
                //Log.e("Children count", dataSnapshot.child(""+user.getUid()).child("Interests").getChildren().toString());
                for (int i = 0; i < dataSnapshot.child(""+user.getUid()).child("Interests").getChildrenCount(); i ++) {
                    //Log.e("Value:", dataSnapshot.child(""+user.getUid()).child("Interests").child(""+i).getValue().toString());
                    myList.add(dataSnapshot.child(""+user.getUid()).child("Interests").child(""+i).getValue().toString());
                    //Log.e("myList", myList.toString());

                }
                Log.e("myList", myList.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!(user.getUid().equals(snapshot.getKey()))) {
                        //Log.e("User:", snapshot.child("Name").getValue().toString());
                        int count = 0;
                        for (int i = 0; i < snapshot.child("Interests").getChildrenCount(); i ++) {
                            //Log.e("Interest:", snapshot.child("Interests").child(""+i).getValue().toString());
                            if (myList.contains(snapshot.child("Interests").child(""+i).getValue().toString())) {
                                count ++;
                                //Log.e("Match:", "YES");
                            }
                        }
                        if (count >= 2) {
                            String fullName = snapshot.child("Name").getValue().toString();
                            String each_uid = snapshot.getKey();
                            name_to_id.put(fullName, each_uid);
                            arrayList.add(fullName);
                            //Log.e("List:", arrayList.toString());
                        }
                    }
                }
                Show(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Database Error");
            }
        });

        //We now have list of current users interests
        //Edited below code to only display people who have similar interests
        //Log.e("Success to here", "");

        /*
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(!(user.getUid().equals(snapshot.getKey()))) {
                        String fullName = snapshot.child("Name").getValue().toString();
                        String each_uid = snapshot.getKey();
                        name_to_id.put(fullName, each_uid);
                        arrayList.add(fullName);
                    }
                }

                Show(arrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "Database Error");            }
        });
         */
        /*
        DatabaseReference ref = database.getReference("users");
        Log.e("Success to here", "");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Success to here", "");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!(user.getUid().equals(snapshot.getKey()))) {
                        int count = 0;
                        for (int i = 0; i < snapshot.child("Interests").getChildrenCount(); i ++) {
                            if (myList.contains(snapshot.child("Interests").child(""+i).getValue())) {
                                count ++;
                            }
                        }
                        if (count >= 2) {
                            String fullName = snapshot.child("Name").getValue().toString();
                            arrayList.add(fullName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        String text = (list.getItemAtPosition(position).toString());
        String partner_UID = name_to_id.get(text);
        Log.i("List View", "You clicked item: " + id + " at position: " + position + " Text: " + text + " UID " + partner_UID);
        Intent intent = new Intent();
        intent.setClass(this, chatActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("id", id);
        Log.e("partner_UID", partner_UID);
        intent.putExtra("Partner_UID", partner_UID);
        startActivity(intent);
    }

    public void Show(ArrayList arrayList){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}