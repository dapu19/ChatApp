package com.dapu.chatapp;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
// class implements the home page for a signed in user.
// from this page a user can be directed to logout, Karen chat bot, messaging service or re-enter details.
public class homeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private TextView email;
    private TextView fullName;
    private ImageView pic;
    private String url;

    //  function which will get saved state and  take user info using FireBase and create variables for user info
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

        // if the user is signed in , will use FireBase to retrieve user data
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
    // function which is used to direct user to page which contains list of matches for messaging
    public void message(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), ListOfMatchesActivity.class);
        startActivity(myIntent);
    }

    // function which directs the user to the Karen ChatBot
    public void karen(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), KarenActivity.class);
        startActivity(myIntent);
    }

    // function directs user to userInfo which allows them to edit their info
    public void userInfo(android.view.View view) {
        Intent myIntent = new Intent(getBaseContext(), userInfo.class);
        startActivity(myIntent);
    }

    // function which makes use of FireBase to log the user out and send them to the Main page.
    public void logout(android.view.View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }


}