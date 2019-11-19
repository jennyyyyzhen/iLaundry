package com.example.myfirstapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AnimatedTimer extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_timer);

         //create instance variables that help to countdown for the timer
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar) ;

    }

    // TODO: complete this function
    private long remainingTime(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String email = currentUser.getEmail().split("@")[0];
        //String machine = database.child("user").child("email").child("machine");
        return 0;
    }

    private void startTimer(final ProgressBar pb, long time){
        new CountDownTimer(30000, 1000) {

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
