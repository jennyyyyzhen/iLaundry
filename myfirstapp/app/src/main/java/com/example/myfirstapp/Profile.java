package com.example.myfirstapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

public class Profile extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setNameDisplay();
    }

    private void setNameDisplay() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();

        // find the corresponding button and node in the database
        int resID = getResources().getIdentifier("user_name", "id", getPackageName());
        final TextView status = (TextView) findViewById(resID);

        status.setText("User Name: " + name + "\n" + "UID: " + uid);

    }

}
