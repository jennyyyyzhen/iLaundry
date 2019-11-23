package com.example.myfirstapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

import static com.example.myfirstapp.AtwoodDorm.availableWashers;


public class AnimatedTimer extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_timer);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar) ;
        setRemainingTime();
         //create instance variables that help to countdown for the timer
        TextView text = (TextView) findViewById(R.id.machine_in_use);
        text.setText("Your machine: ");

    }

    // TODO: complete this function
    private void setRemainingTime(){

        String email = currentUser.getEmail().split("@")[0];
        DatabaseReference id = database.child("user").child(email).child("machine");

        id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String machineId = dataSnapshot.getValue(String.class);
                setTimeHelper(machineId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;
    }



        private void setTimeHelper(final String id) {
            // acquire machine information from its id
            String[] childNodes = id.split("_");
            final String dorm = childNodes[0];
            final String machine = childNodes[1];
            final String num = childNodes[2];
            final ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar) ;

            DatabaseReference node = database.child(dorm).child(machine).child(num).child("endTime");

            // read and write data to the database
            node.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long endTime = dataSnapshot.getValue(long.class);
                    long now = Instant.now().toEpochMilli();
                    if (endTime > now) {
                        startTimer(pb, endTime - now);
                        TextView text = (TextView) findViewById(R.id.machine_in_use);
                        text.setText("Your machine: "+ id );
                    } else {
                        startTimer(pb,0);
                        Log.e("0","0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        private void startTimer(final ProgressBar pb, long time){
        new CountDownTimer(time, 1000) {

            //initialize progress to start the timer
            public void onTick(long millisUntilFinished) {
                pb.setProgress((int)millisUntilFinished/1000);
            }

            //set progress to 0 when the timer finishes
            public void onFinish() {
                pb.setProgress(0);
            }
        }.start();
    }
}
