package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AtwoodDorm extends AppCompatActivity {

    @Override
    /*
        Display the layout when the new activity is created.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atwood_dorm);
    }

    /* This function is called when a button is clicked. It gets the ID of that button and
        the check the corresponding value stored in the database. If the status is currently
        true, then changed it to false. Otherwise, change it to true
     */
    public void changeStatus(View view){
        DatabaseReference database=FirebaseDatabase.getInstance().getReference();

        int viewID = view.getId();
        String viewName = getResources().getResourceName(viewID);

        String[] childNodes = viewName.split("_");
        String dorm = childNodes[0].split("/")[1];
        String machine = childNodes[1];
        String num = childNodes[2];

        final DatabaseReference NODE = database.child(dorm).child(machine).child(num);

        NODE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean status = dataSnapshot.getValue(Boolean.class);
                NODE.setValue(!status);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
