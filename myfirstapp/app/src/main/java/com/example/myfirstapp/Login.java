package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/*
    Login function for users to sign up or sign in, uses interface AppCompatActivity
    Sign up:create new credentials in the database
    Sign in:using existing credentials to log in
    */
public class Login extends AppCompatActivity {
    EditText username, password, email;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void getExtraInformation(View view) {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email_address);

        if(!checkRequirefields(password)) return;
        if(!checkRequirefields(email)) return;

        Intent intent = new Intent(this, SignUpInformation.class);
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        intent.putExtra("email", email.getText().toString());

        startActivity(intent);
    }


    /*
    Sign in:using existing credentials to log in
    */
    public void setSignin(View view){
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email_address);

        // check if either field is empty
        if(!checkRequirefields(password)) return;
        if(!checkRequirefields(email)) return;
        
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Welcome "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 
    If email or password field are empty, throw an exception.
    */
    private boolean checkRequirefields(EditText editText){
        if(TextUtils.isEmpty(editText.getText())){
            // Throw the error message to the user
            Toast.makeText(Login.this, "Email and password are required",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
