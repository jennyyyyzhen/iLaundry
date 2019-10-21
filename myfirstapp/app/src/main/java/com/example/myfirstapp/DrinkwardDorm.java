package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkwardDorm extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinkward_dorm);
    }

    public void changeStatus(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        database.child("Drinkward").child("student").setValue(currentUser.getEmail());
    }

    public void goDrinkwardDormRoom1(View view){

        //called when the user taps the East Dorm button

        Intent intent = new Intent(this, DrinkwardDormRoom1.class);
        startActivity(intent);
    }

    public void goDrinkwardDormRoom2(View view){

        //called when the user taps the East Dorm button

        Intent intent = new Intent(this, DrinkwardDormRoom2.class);
        startActivity(intent);
    }

    public void goDrinkwardDormRoom3(View view){

        //called when the user taps the East Dorm button

        Intent intent = new Intent(this, DrinkwardDormRoom3.class);
        startActivity(intent);
    }

    public void goDrinkwardDormRoom4(View view){

        //called when the user taps the East Dorm button

        Intent intent = new Intent(this, DrinkwardDormRoom4.class);
        startActivity(intent);
    }
}
