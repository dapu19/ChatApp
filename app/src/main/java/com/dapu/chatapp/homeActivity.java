package com.dapu.chatapp;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class homeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private TextView email;
    private TextView uid;
    private ImageView pic;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uid = findViewById(R.id.uid);
        email = findViewById(R.id.email);
        pic = findViewById(R.id.imageView);
        if(user != null){
            String email1 = user.getEmail();
            String uid1 = user.getUid();
            uid.setText(uid1);
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
        Intent myIntent = new Intent(getBaseContext(), messageActivity.class);
        startActivity(myIntent);
    }

    public void logout(android.view.View view){
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(myIntent);
    }


}