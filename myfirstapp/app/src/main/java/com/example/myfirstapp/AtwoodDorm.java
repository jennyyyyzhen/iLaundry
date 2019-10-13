package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;


public class AtwoodDorm extends AppCompatActivity {

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    /*
        Display the layout when the new activity is created.
        Set all text displayed on all buttons
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atwood_dorm);
        setButtonDisplay("Atwood_dryer_1");
        setButtonDisplay("Atwood_dryer_2");
        setButtonDisplay("Atwood_dryer_3");
        setButtonDisplay("Atwood_dryer_4");
        setButtonDisplay("Atwood_dryer_5");
        setButtonDisplay("Atwood_dryer_6");
        setButtonDisplay("Atwood_washer_1");
        setButtonDisplay("Atwood_washer_2");
        setButtonDisplay("Atwood_washer_3");
        setButtonDisplay("Atwood_washer_4");
        setButtonDisplay("Atwood_washer_5");
    }


    /* This function is called when a button is clicked. It gets the ID of that button and
        then read the text of that button.

        If the button displays "true", it means the machine is available now. We will
        create a time stamp and show a dialog. Users can choose whether or not to start that machine

        If the button displays other text message, then it means the machine is occupied. If
        users click on the machine, it will show a alert dialog to users.
     */
    public void changeStatus(View view) {

        Button button = (Button) view;
        StringBuilder sb = new StringBuilder(button.getText());
        String status = sb.toString();

        if (status.equals("true")) {
            // if the machine is available then show an confirmation dialog
            createConfirmationDialog(button).show();
        } else {

            // if the machine is occupied, then show an alert dialog
            createAlertDialog().show();
        }
    }

    /*
        Start to countdown given amount of time when a button is clicked.
     */
    private void startTimer(View view, long timeInMili) {
        final Button button = (Button) view;
        CountDownTimer countDownTimer = new CountDownTimer(timeInMili, 1000) {

            @Override
            // update remaining time every mins.
            public void onTick(long l) {
                String seconds = Long.toString(l / 1000);
                button.setText(seconds + " sec");
            }

            // update text of button when finished
            public void onFinish() {
                button.setText("true");
            }
        }.start();
    }

    /*
        A helper function to set text message for a given button. If the end time is greater than
        current time, then start a countdown timer. Otherwise, display true.
     */
    private void setButtonDisplay(final String id) {
        // acquire machine information from its id
        String[] childNodes = id.split("_");
        String dorm = childNodes[0];
        String machine = childNodes[1];
        String num = childNodes[2];

        // find the corresponding button and node in the database
        int resID = getResources().getIdentifier(id, "id", getPackageName());
        final Button button = (Button) findViewById(resID);

        DatabaseReference node = database.child(dorm).child(machine).child(num).child("endTime");

        // read and write data to the database
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long endTime = dataSnapshot.getValue(long.class);
                long now = Instant.now().toEpochMilli();
                if (endTime > now) {
                    startTimer(button, endTime - now);
                } else {
                    button.setText("true");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /*
      This function returns a alert dialog when the user click on a machine that is in use.
    */
    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This machine is in use")
                .setTitle("Alert");

        AlertDialog dialog = builder.create();
        return dialog;
    }


   /*
   This function returns a alert dialog that allows user to choose whether to start a machine or
   not. If the user choose yes, then it will start a timer with a given amount of time.
    */

    private Dialog createConfirmationDialog(final Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Start this machine")
                .setItems(new String[]{"light", "regular"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == 0) {
                            setTime(button, 100 * 1000);
                            startTimer(button, 100 * 1000);
                        }
                        if (which == 1) {
                            setTime(button, 50 * 1000);
                            startTimer(button, 50 * 1000);
                        }
                    }});

        AlertDialog dialog = builder.create();
        return dialog;
    }

    /* update the ending time, status and student information in the database
     */
    private void setTime(View view, long time){
        int viewID = view.getId();
        String viewName = getResources().getResourceName(viewID);

        String[] childNodes = viewName.split("_");
        String dorm = childNodes[0].split("/")[1];
        String machine = childNodes[1];
        String num = childNodes[2];

        long now = Instant.now().toEpochMilli();
        long endTime = time+now;

        DatabaseReference node = database.child(dorm).child(machine).child(num);
        node.child("endTime").setValue(endTime);
        node.child("status").setValue(false);
        node.child("student").setValue(currentUser.getEmail());
    }
}
