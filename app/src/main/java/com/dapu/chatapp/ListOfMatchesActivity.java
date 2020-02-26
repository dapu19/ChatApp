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
                arrayList.add("FAIL");
            }
        });
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        String text = (list.getItemAtPosition(position).toString());
        String partner_UID = name_to_id.get(text);
        Log.i("List View", "You clicked item: " + id + " at position: " + position + " Text: " + text + " UID " + partner_UID);
        Intent intent = new Intent();
        intent.setClass(this, chatActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("id", id);
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