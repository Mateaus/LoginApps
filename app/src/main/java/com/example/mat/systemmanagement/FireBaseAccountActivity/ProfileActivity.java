package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mat.systemmanagement.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTv = (TextView)findViewById(R.id.emailTv);
        emailTv.setText(getIntent().getExtras().getString("Email"));
    }
}
