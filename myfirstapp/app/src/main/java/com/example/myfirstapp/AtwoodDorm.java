package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

            // if the machine is available then show an confirmation dialog
            long now = Instant.now().toEpochMilli();
            node.child("endTime").setValue(now+100*1000);
            createConfirmationDialog(button,100*1000).show();
        } else{

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
                String mins = Long.toString(l/60/1000);
                button.setText(mins+" mins");
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
    private void setButtonDisplay(final String id){
        // acquire machine information from its id
        String[] childNodes = id.split("_");
        String dorm = childNodes[0];
        String machine = childNodes[1];
        String num = childNodes[2];

        // find the corresponding button and node in the database
        int resID = getResources().getIdentifier(id, "id", getPackageName());
        final Button button = ((Button) findViewById(resID));
        final DatabaseReference NODE = database.child(dorm).child(machine).child(num).child("endTime");

        // read and write data to the database
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
            public void onCancelled(DatabaseError databaseError) {}
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

   private AlertDialog createConfirmationDialog(final Button button, final long time){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);

       builder.setMessage("Start this machine")
               .setCancelable(false)
               .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int id) {

                       // if this button is clicked, start timer
                       startTimer(button, time);
                   }
               })
               .setNegativeButton("no",new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog,int id) {
                       // if this button is clicked, just close
                       // the dialog box and do nothing
                       dialog.cancel();
                   }
               });

       AlertDialog dialog = builder.create();
       return dialog;
   }
}
