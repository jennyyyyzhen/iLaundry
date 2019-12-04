package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import utils.ThreadUtils;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ThreadUtils.runInThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);

                Intent intent = new Intent(Launcher.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void skip(View view){
        Intent intent = new Intent(Launcher.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
