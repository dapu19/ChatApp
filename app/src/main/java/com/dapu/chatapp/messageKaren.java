package com.dapu.chatapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class messageKaren extends AsyncTask<String, Void, String> {

    private Exception exception;
    public static final String REMOTE_HOST = "54.152.68.22";
    public static final int REMOTE_PORT = 2346;



    @Override
    protected String doInBackground(String... params) {
        try {
            Log.e("Trying", "sent message");
            String message = params[0];
            Log.e("Message", message);
            Socket socket = new Socket(REMOTE_HOST, REMOTE_PORT);


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
