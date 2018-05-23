package com.example.mat.systemmanagement.FireBaseAccountActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.systemmanagement.R;
import com.example.mat.systemmanagement.RoomActivities.RecyclerActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTv;
    private Button customerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        customerBtn = (Button)findViewById(R.id.customerBtn);
        emailTv = (TextView)findViewById(R.id.emailTv);
        emailTv.setText(getIntent().getExtras().getString("Email"));

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });
    }
}
