package com.dapu.chatapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.UploadTask;

import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

// Allows the user to upload an image of themselves to the app
public class upload extends AppCompatActivity {
    ImageView img;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    public Uri imguri;
    private String url;
    FirebaseUser user;

    // set content view and use FireBase to get reference to user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        img = (ImageView) findViewById(R.id.Profile);
    }

    // code to handle suer choosing a file from their filesystem
    public void chooseFile(android.view.View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checks all conditions that indicate image has been selected correctly
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            img.setImageURI(imguri);
        }
    }

    // will upload the file to FireBase storage
    public void uploadFile(android.view.View view){
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        //StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
        url = user.getUid();
        System.out.println(url);
        StorageReference Ref = mStorageRef.child(url+"."+getExtension(imguri));

        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
        updateUI();
    }

    // get file extension of image
    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    // sends user to main page when upload is complete
    public void updateUI(){
        Intent myIntent = new Intent(getBaseContext(), homeActivity.class);
        startActivity(myIntent);
    }

    public void skip(android.view.View view){
        updateUI();
    }
}
