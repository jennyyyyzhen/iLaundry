package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    /* onCreate function helps set up the the layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_timer:
                        Intent intentTimer = new Intent(MainActivity.this, AnimatedTimer.class);
                        startActivity(intentTimer);
                        break;
                    case R.id.action_map:
                        Intent intentMap = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intentMap);
                        break;
                    case R.id.action_account:
                        Intent intentProfile = new Intent(MainActivity.this, Profile.class);
                        startActivity(intentProfile);
                        break;
                }
                return true;
            }
        });
    }

    /* onStart function helps set up the first page the user sees, including a login page,
    and a welcome message
     */
    @Override
    public void onStart(){
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        } else {
            TextView greeting = findViewById(R.id.greeting);
            greeting.setText("Hi " + currentUser.getDisplayName() + "! Welcome to iLaundry! \nPlease select the dorm you are in:");
        }
    }

    /* The following functions would be called if the corresponding buttons get clicked by
     users. It would start a new activity and navigate user to the new page.
     */
    public void goEastDorm(View view){

        //called when the user taps the East Dorm button

        Intent intent = new Intent(this, EastDorm.class);
        startActivity(intent);
    }

    public void goWestDorm(View view){

        //called when the user taps the West Dorm button


        Intent intent = new Intent(this, WestDorm.class);
        startActivity(intent);
    }
    public void goSouthDorm(View view){

        //called when the user taps the South Dorm button

        Intent intent = new Intent(this, SouthDorm.class);
        startActivity(intent);
    }
    public void goNorthDorm(View view){

        //called when the user taps the North Dorm button

        Intent intent = new Intent(this, NorthDorm.class);
        startActivity(intent);
    }
    public void goSontagDorm(View view){

        //called when the user taps the Sontag Dorm button


        Intent intent = new Intent(this, SontagDorm.class);
        startActivity(intent);
    }
    public void goCaseDorm(View view){

        //called when the user taps the Case Dorm button


        Intent intent = new Intent(this, CaseDorm.class);
        startActivity(intent);
    }
    public void goDrinkwardDorm(View view){

        //called when the user taps the Drinkward Dorm button

        Intent intent = new Intent(this, DrinkwardDorm.class);
        startActivity(intent);
    }
    public void goLindeDorm(View view){

        //called when the user taps the Linde Dorm button

        Intent intent = new Intent(this, LindeDorm.class);
        startActivity(intent);
    }
    public void goAtwoodDorm(View view){

        //called when the user taps the Atwood Dorm button
        Intent intent = new Intent(this, AtwoodDorm.class);
        startActivity(intent);
    }


    /* Log Out function helps the user to log out of their credentials
     */
    public void logOut(View view){
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
