package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

public class AtwoodDorm extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    int numWasher = 5;
    int numDryer = 6;

    @Override
    /*
        Display the layout when the new activity is created.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atwood_dorm);
        setButtonDisplay("Atwood_dryer_2");
        setButtonDisplay("Atwood_dryer_3");
    }


    /* This function is called when a button is clicked. It gets the ID of that button and
        the check the corresponding value stored in the database. If the status is currently
        true, then changed it to false. Otherwise, change it to true
     */
    public void changeStatus(View view){


        int viewID = view.getId();
        String viewName = getResources().getResourceName(viewID);

        String[] childNodes = viewName.split("_");
        String dorm = childNodes[0].split("/")[1];
        String machine = childNodes[1];
        String num = childNodes[2];


        Button button = (Button) view;
        DatabaseReference node = database.child(dorm).child(machine).child(num);

        StringBuilder sb = new StringBuilder(button.getText());
        String status = sb.toString();

        if (status.equals("true")) {
            long now = Instant.now().toEpochMilli();
            node.child("endTime").setValue(now+100*1000);
            startTimer(button,100*1000);
        }
    }

    public void startTimer(View view, long timeInMili) {
        final Button button = (Button) view;
        CountDownTimer countDownTimer = new CountDownTimer(timeInMili, 1000) {
            @Override
            public void onTick(long l) {
                button.setText("seconds" + l);
            }

            public void onFinish() {
                button.setText("true");
            }
        }.start();
    }

    private void setButtonDisplay(String id){
        String[] childNodes = id.split("_");
        String dorm = childNodes[0];
        String machine = childNodes[1];
        String num = childNodes[2];

        int resID = getResources().getIdentifier(id, "id", getPackageName());
        final Button button = ((Button) findViewById(resID));
        final DatabaseReference NODE = database.child(dorm).child(machine).child(num).child("endTime");

        NODE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long endtime = dataSnapshot.getValue(long.class);
                long now = Instant.now().toEpochMilli();
                if(endtime>now){
                    startTimer(button,endtime-now);
                }else{
                    button.setText("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
