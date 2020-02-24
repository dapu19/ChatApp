package com.dapu.chatapp;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class homeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private TextView email;
    private TextView fullName;
    private ImageView pic;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        pic = findViewById(R.id.imageView);


        //How to get id for potentially messaging
        //Log.e("Token", FirebaseInstanceId.getInstance().getInstanceId().toString());

        if(user != null){
            String email1 = user.getEmail();
            //String uid1 = user.getUid();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users/"+user.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DataSnapshot snapshot = dataSnapshot.child("Name");
                    //for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //if ((user.getUid()).equals(snapshot.getKey())){
                    String name = snapshot.getValue().toString();
                    fullName.setText(name);
                        //}
                    //}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //fullName.setText(name);
            email.setText(email1);
            try{
                url = user.getUid();
                final File localFile = File.createTempFile(url, "jpg");
                StorageReference Ref = mStorageRef.child(url+".jpg");
                Ref.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Successfully downloaded data to local file
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                pic.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...
                    }
                });
            }catch (Exception e){

            }
        }
    }

    public void message(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), ListOfMatchesActivity.class);
        startActivity(myIntent);
    }

    public void logout(android.view.View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }


}