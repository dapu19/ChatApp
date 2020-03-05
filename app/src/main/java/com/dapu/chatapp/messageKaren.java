package com.dapu.chatapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

// class handles messages being sent to Karen chat bot
class messageKaren extends AsyncTask<String, Void, String> {

    // hard coded values for ip and port where Karen is running
    private Exception exception;
    public static final String REMOTE_HOST = "54.152.68.22";
    public static final int REMOTE_PORT = 2346;
    private FirebaseAuth mAuth;
    String roomid;




    @Override
    protected String doInBackground(String... params) {
        try {

            mAuth = FirebaseAuth.getInstance();


            FirebaseUser user = mAuth.getCurrentUser();
            final String my_UID = user.getUid();
            roomid = my_UID + "-karen";

            Log.e("Trying", "sent message");
            String message = params[0];
            Log.e("Message", message);
            Socket socket = new Socket(REMOTE_HOST, REMOTE_PORT);

            // input and output streams from/to socket
            BufferedWriter sockOut = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader sockIn = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            for (;;) {

                sockOut.write(message, 0, message.length());
                sockOut.newLine();
                sockOut.flush();

                Log.e("response", "Flushed");
                String response = sockIn.readLine();
                Log.e("response", response);
                if (response == null) {
                    Log.e("Network Error", "Remote process closed the connection.");
                    break;
                } else {
                    Log.e("response", response);


                    if (response.length() > 0) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Log.e("roomid",roomid);
                        DatabaseReference myRef = database.getReference("rooms/" + roomid);
                        Long time = System.currentTimeMillis() / 1000L;

                        Message toSend = new Message(response, "karen", time);
                        Log.e("UID",toSend.getUID());
                        myRef.child(time.toString()).setValue(toSend);
                    }
                    break;

                }
            }

        } catch (Exception e) {
            this.exception = e;
            Log.e("Error", this.exception.toString());
            return null;
        } finally {
            Log.e("Done", "sent message");
        }
        Log.e("Hi", "sent message");
        return null;
    }



}
