package com.example.myfirstapp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimatedTimer extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_timer);

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar) ;
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                pb.setProgress((int)millisUntilFinished/1000);
            }

            public void onFinish() {
                pb.setProgress(0);
            }
        }.start();
    }
}