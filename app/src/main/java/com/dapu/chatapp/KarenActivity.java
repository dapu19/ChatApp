package com.dapu.chatapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class KarenActivity extends AppCompatActivity {

    private EditText editText;
    public static final String REMOTE_HOST = "10.241.164.6";
    public static final int REMOTE_PORT = 65432;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karen);
    }


    public void sendMessage(View view) throws IOException {
        editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        new messageKaren().execute(message);
        editText.setText("");
    }
}
