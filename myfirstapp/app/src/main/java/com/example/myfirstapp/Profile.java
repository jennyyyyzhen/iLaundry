package com.example.myfirstapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        setDisplay();
    }

    private void setDisplay() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        String name = firebaseUser.getDisplayName();
        String emailAddress =  firebaseUser.getEmail();


        // find the corresponding button and node in the database
        int userNameID = getResources().getIdentifier("user_name", "id", getPackageName());
        final TextView userName= (TextView) findViewById(userNameID);

        // find the corresponding button and node in the database
        int userEmailID = getResources().getIdentifier("user_email", "id", getPackageName());
        final TextView userEmail = (TextView) findViewById(userEmailID);

        userName.setText("Username: " + name + "\n");
        userEmail.setText("User Email: " + emailAddress +"\n");

    }

    public void showInputDialog(View view) {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(Profile.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(Profile.this);
        inputDialog.setTitle("New Username").setView(editText);
        inputDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = editText.getText().toString();
                        updateUsername(username);

                    }
                }).show();
    }

    private void updateUsername(String username){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Profile.this, "Username changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
