package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.RoomActivities.RecyclerActivity;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTv;
    private Button customerBtn, registerBtn;
    public static Button calculateBtn;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Firebase.setAndroidContext(this);

        customerBtn = (Button)findViewById(R.id.customerBtn);
        emailTv = (TextView)findViewById(R.id.emailTv);
        calculateBtn = (Button)findViewById(R.id.calcBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        emailTv.setText(getIntent().getExtras().getString("Email"));

        /*UserPermission userPermission = new UserPermission(ProfileActivity.this);
        emailTv.setText(userPermission.getName());*/
        //userPermission.getUserPermission("user");


        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null ) {
                    user.sendEmailVerification();
                } else {
                    FirebaseAuth.getInstance().signOut();
                }

            }
        };

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

}
