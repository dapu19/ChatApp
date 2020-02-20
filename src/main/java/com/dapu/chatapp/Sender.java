package com.dapu.chatapp;

import android.os.AsyncTask;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends AsyncTask<String, Void, Void>{

    Socket s;
    DataInputStream dof;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids){
        String email = voids[0];
        String password = voids[1];
        try{
            s = new Socket("111.11.11.11", 7000);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(email);
            pw.write(password);
            pw.flush();
            pw.close();
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
