package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(MainActivity.this, "please login first", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        } else {
            TextView greeting = findViewById(R.id.greeting);
            greeting.setText("Hi " + currentUser.getDisplayName());
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

    public void logOut(View view){
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
