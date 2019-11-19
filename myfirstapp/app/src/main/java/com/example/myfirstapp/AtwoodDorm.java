package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.time.Instant;


public class AtwoodDorm extends AppCompatActivity {

    /*
        create instance variables that set up washsers and dryers in Atwood
         */
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    static int availableWashers = 0;
    static int availableDryers = 0;


    @Override
    /*
        Display the layout when the new activity is created.
        Set all text displayed on all buttons
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atwood_dorm);

        setTimeDisplay("Atwood_dryer_1");
        setTimeDisplay("Atwood_dryer_2");
        setTimeDisplay("Atwood_dryer_3");
        setTimeDisplay("Atwood_dryer_4");
        setTimeDisplay("Atwood_dryer_5");
        setTimeDisplay("Atwood_dryer_6");

        setTimeDisplay("Atwood_washer_1");
        setTimeDisplay("Atwood_washer_2");
        setTimeDisplay("Atwood_washer_3");
        setTimeDisplay("Atwood_washer_4");
        setTimeDisplay("Atwood_washer_5");

        // Bug
        TextView text = (TextView) findViewById(R.id.info);
        Log.e("available washer", String.valueOf(availableWashers));
        text.setText("Currently there are " + String.valueOf(availableDryers) + " dryers and " + String.valueOf(availableWashers) + " washers available");



    }





    /* This function is called when a button is clicked. It gets the ID of that button and
        then read the text of that button.

        If the button displays "available", it means the machine is available now. We will
        create a time stamp and show a dialog. Users can choose whether or not to start that machine

        If the button displays other text message, then it means the machine is occupied. If
        users click on the machine, it will show a alert dialog to users.
     */
    public void changeStatus(View view) {

        ImageButton button = (ImageButton) view;
        String textTag = button.getTag().toString().substring(2);
        String machine = button.getTag().toString().split("_")[2];
        TextView text = (TextView) findViewById(R.id.atwood).findViewWithTag(textTag);
        String status = text.getText().toString();

        if (status.equals("Available")) {
            // if the machine is available then show an confirmation dialog
            createConfirmationDialog(text).show();
        } else {

            // if the machine is occupied, then show an alert dialog
            createAlertDialog().show();
        }
    }

    /*
        Start to countdown given amount of time when a button is clicked.
     */
    private void startTimer(View view, long timeInMili) {
        final TextView status = (TextView) view;
        CountDownTimer countDownTimer = new CountDownTimer(timeInMili, 1000) {

            @Override
            // update remaining time every mins.
            public void onTick(long l) {
                String seconds = Long.toString(l / 1000);
                status.setText(seconds + " sec");
            }

            // update text of button when finished
            public void onFinish() {

                status.setText("Available");
                addNotification("Atwood");
                String machine = status.getTag().toString().split("_")[1];
            }
        }.start();
    }

    /*
        A helper function to set text message for a given button. If the end time is greater than
        current time, then start a countdown timer. Otherwise, display true.
     */
    private void setTimeDisplay(final String id) {
        // acquire machine information from its id
        String[] childNodes = id.split("_");
        String dorm = childNodes[0];
        final String machine = childNodes[1];
        String num = childNodes[2];

        // find the corresponding button and node in the database
        int resID = getResources().getIdentifier(id, "id", getPackageName());
        final TextView status = (TextView) findViewById(resID);

        DatabaseReference node = database.child(dorm).child(machine).child(num).child("endTime");

        // read and write data to the database
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long endTime = dataSnapshot.getValue(long.class);
                long now = Instant.now().toEpochMilli();
                if (endTime > now) {
                    startTimer(status, endTime - now);
                } else {
                    status.setText("Available");
                    Log.e("machine", machine);
                    if(machine.equals("washer")){
                        availableWashers++;
                        Log.e("available washer", String.valueOf(availableWashers));
                    }
                    if(machine.equals("dryer")){
                        availableDryers++;
                    }
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

        //helps sending messages to notify "This machine is in use"
        //helps set alert
        builder.setMessage("This machine is in use")
                .setTitle("Alert");

        //helps set dialog
        AlertDialog dialog = builder.create();
        return dialog;
    }


   /*
   This function returns a alert dialog that allows user to choose whether to start a machine or
   not. If the user choose yes, then it will start a timer with a given amount of time.
    */

    private Dialog createConfirmationDialog(final TextView text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Start this machine")
                .setItems(new String[]{"Regular", "Light"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (which == 0) {
                            setTime(text, 100 * 1000);
                            startTimer(text, 100 * 1000);
                        }
                        if (which == 1) {
                            setTime(text, 50 * 1000);
                            startTimer(text, 50 * 1000);
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

        //create string variables for time
        String[] childNodes = viewName.split("_");
        String dorm = childNodes[0].split("/")[1];
        String machine = childNodes[1];
        String num = childNodes[2];

        long now = Instant.now().toEpochMilli();
        long endTime = time+now;

        //set values for time variables
        DatabaseReference node = database.child(dorm).child(machine).child(num);
        node.child("endTime").setValue(endTime);
        node.child("status").setValue(false);

        String email = currentUser.getEmail().split("@")[0];

        database.child("user").child(email).child("machine").setValue(viewName.split("/")[1]);

    }

    
    /* send out notification(s) to the user when a machine(dryer/washer) is done
     */
    private void addNotification(String dormName) {
        // Builds your notification
        NotificationManager notification_manager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification_builder;

        //condition for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(this, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(this);
        }


        notification_builder.setSmallIcon(R.mipmap.laundry_service_round)
                .setContentTitle("Your laundry is done in " + dormName + "'s laundry room.")
                .setContentText("Please go and get it!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification_builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification_builder.build());
    }
}
