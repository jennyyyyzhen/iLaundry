package com.example.myfirstapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpInformation extends AppCompatActivity {

    String username, password, email;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);
        Bundle extras = getIntent().getExtras();
        username = extras.getSerializable("username").toString();
        password = extras.getSerializable("password").toString();
        email = extras.getSerializable("email").toString();
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        final RadioGroup radioDormGroup = (RadioGroup) findViewById(R.id.select_dorm);
        Button submitDorm = (Button) findViewById(R.id.submit_dorm);
        Toast.makeText(getApplicationContext(),"Hello Javatpoint1",Toast.LENGTH_SHORT).show();

        submitDorm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello Javatpoint0",Toast.LENGTH_SHORT).show();

                // get selected radio button from radioGroup
                int selectedId = radioDormGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton selectedDorm = (RadioButton) findViewById(selectedId);
                String dorm = selectedDorm.getText().toString();

                Toast.makeText(getApplicationContext(),"Hello Javatpoint2",Toast.LENGTH_SHORT).show();
                setSignUp(username, password, email, dorm);
                Toast.makeText(getApplicationContext(),"Hello Javatpoint3",Toast.LENGTH_SHORT).show();
            }

        });

    }

    /*
Sign up:create new credentials in the database
*/
    public void setSignUp(String username, String password, String email, String dorm){
        final String currentUsername = username;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(currentUsername).build();
                            user.updateProfile(profileUpdates);
                            user.sendEmailVerification();

                            Toast.makeText(SignUpInformation.this, "Welcome "+currentUsername, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(SignUpInformation.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }});

        LaundryUser user = new LaundryUser();
        user.setName(username);
        //user.setYear(year.getText().toString());
        user.setDorm(dorm);
        Map<String, String> userData = new HashMap<String, String>();

        userData.put("Username", username);
        userData.put("Dorm", dorm);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("user").push().setValue(userData);
    }


}
