package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

public class DrinkwardDormRoom1 extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinkward_dorm_room1);
        setButtonDisplay("Drinkward_room_1_washer_1");
        setButtonDisplay("Drinkward_room_1_washer_2");
        setButtonDisplay("Drinkward_room_1_dryer_1");
        setButtonDisplay("Drinkward_room_1_dryer_2");
    }

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
            createConfirmationDialog(button,100*1000).show();
        } else{
            createAlertDialog().show();
        }
    }

    private void startTimer(View view, long timeInMili) {
        final Button button = (Button) view;
        CountDownTimer countDownTimer = new CountDownTimer(timeInMili, 1000) {
            @Override
            public void onTick(long l) {
                button.setText(Long.toString(l/60/1000)+" mins");
            }

            public void onFinish() {
                button.setText("true");
                addNotification("Drinkward Room 1");
            }
        }.start();
    }

    private void setButtonDisplay(final String id){
        String[] childNodes = id.split("_");
        String dorm = childNodes[0];
        String room = childNodes[1] + childNodes[2];
        String machine = childNodes[3];
        String num = childNodes[4];
        System.out.println(dorm+room+machine+num);

        int resID = getResources().getIdentifier(id, "id", getPackageName());
        final Button button = ((Button) findViewById(resID));
        final DatabaseReference NODE = database.child(dorm).child(room).child(machine).child(num).child("endTime");

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

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("This machine is in use")
                .setTitle("Alert");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createConfirmationDialog(final Button button, final long time){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Start this machine")
                .setCancelable(false)
                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
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

    private void addNotification(String dormName) {
        // Builds your notification
        NotificationManager notification_manager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification_builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3001";
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
                .setContentTitle("Your laundry is done in" + dormName + "'s laundry room.")
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
