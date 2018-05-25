package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.systemmanagement.FireBaseAccountActivity.LogInSystemActivities.RegistrationActivity;
import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInformation.UserPermission;
import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.RoomActivities.RecyclerActivity;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminProfileActivity extends AppCompatActivity {

    private TextView emailTv;
    private Button customerBtn, registerBtn,userBtn;
    public static Button calculateBtn;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "UserPermission";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Firebase.setAndroidContext(this);

        customerBtn = (Button)findViewById(R.id.customerBtn);
        emailTv = (TextView)findViewById(R.id.emailTv);
        calculateBtn = (Button)findViewById(R.id.calcBtn);
        registerBtn = (Button)findViewById(R.id.registerBtn);
        userBtn = (Button)findViewById(R.id.userBtn);
        //emailTv.setText(getIntent().getExtras().getString("Email"));

        UserPermission userPermission = new UserPermission(AdminProfileActivity.this);
        emailTv.setText(userPermission.getName());
        //userPermission.getUserPermission();

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileActivity.this, RecyclerActivity.class);
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
                Intent intent = new Intent(AdminProfileActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileActivity.this, ViewDatabase.class);
                startActivity(intent);
            }
        });
    }

}
