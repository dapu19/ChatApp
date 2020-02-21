package com.dapu.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_matches);
        list = (ListView)findViewById(R.id.listView);
        list.setOnItemClickListener(this);

        final ArrayList arrayList = new ArrayList();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(!(user.getUid().equals(snapshot.getKey()))) {
                        String fullName = snapshot.getValue().toString();
                        Log.e("Full name", fullName);
                        arrayList.add(fullName);
                    }
                }
                Log.e("array", arrayList.toString());
                Show(arrayList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                arrayList.add("FAIL");
            }
        });
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("List View", "You clicked item: " + id + " at position: " + position);
        Intent intent = new Intent();
        intent.setClass(this, chatActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("id", id);
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