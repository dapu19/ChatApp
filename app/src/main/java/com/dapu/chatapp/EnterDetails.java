package com.dapu.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterDetails extends AppCompatActivity {

    private SeekBar ageSlider;
    private TextView displayAge;
    private FirebaseAuth mAuth;
    private EditText location;
    int SPLASH_TIME = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_enter_details);
                mAuth = FirebaseAuth.getInstance();
                ageSlider = findViewById(R.id.SeekBar);
                int ageInt = ageSlider.getProgress();
                String age = String.valueOf(ageInt);
                displayAge = findViewById(R.id.display);
                displayAge.setText(age);
                ageSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                    // function for handling user changing age on progress bar
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        int ageInt = ageSlider.getProgress();
                        String age = String.valueOf(ageInt);
                        displayAge = findViewById(R.id.display);
                        displayAge.setText(age);
                    }

                    // begin tracking user touch on progress bar
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        int ageInt = ageSlider.getProgress();
                        String age = String.valueOf(ageInt);
                        displayAge = findViewById(R.id.display);
                        displayAge.setText(age);
                    }

                    // stop tracking user touch on progress barr
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int ageInt = ageSlider.getProgress();
                        String age = String.valueOf(ageInt);
                        displayAge = findViewById(R.id.display);
                        displayAge.setText(age);
                    }
                });
            }
        }, SPLASH_TIME);

    }

    // function for handling pressing submit button will call addUser with details that have been entered
    public void submit(android.view.View view){

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioSex);


                    // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
        RadioButton selected = findViewById(selectedId);
        String gender = selected.getText().toString();

        location = findViewById(R.id.location);
        String loc = location.getText().toString();
        String age = String.valueOf(ageSlider.getProgress());
        FirebaseUser user = mAuth.getCurrentUser();
        addUser(user.getUid(), loc, age, gender);
        updateUI(user);
     }

    public void addUser(String uid, String location, String age, String gender){
        // Write a message to the database containing users entered details
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(uid).child("Gender").setValue(gender);
        myRef.child(uid).child("Age").setValue(age);
        myRef.child(uid).child("Location").setValue(location);
    }

    // updates UI based on if user is signed in or not.
    public void updateUI(com.google.firebase.auth.FirebaseUser user){
        if(user==null) {
            Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(myIntent);
        }else{
            Intent myIntent = new Intent(getBaseContext(), userInfo.class);
            startActivity(myIntent);
        }
    }

}

