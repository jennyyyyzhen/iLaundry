package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AtwoodDorm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atwood_dorm);
    }
    public void changeStatus(View view){
        DatabaseReference database=FirebaseDatabase.getInstance().getReference();
        database.child("north").child("dryer").child("dryer1").setValue(true);

    }
}
