package com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.ManagerUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI.AdminConfirmingAccount;
import com.example.mat.systemmanagement.FireBaseAccountActivity.UserInterface.AdminUI.AdminRegistrationActivity;
import com.example.mat.systemmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "AddUserstoDatabase";

    private EditText nameregEt, passwordregEt, emailregEt, phoneregEt;
    private Button registerBtn;
    private Spinner roleDd;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_registration);

        nameregEt = (EditText)findViewById(R.id.nameregEt);
        emailregEt = (EditText)findViewById(R.id.emailregEt);
        passwordregEt = (EditText)findViewById(R.id.passwordregEt);
        phoneregEt = (EditText)findViewById(R.id.phoneregEt);
        registerBtn = (Button)findViewById(R.id.createBtn);
        roleDd = (Spinner)findViewById(R.id.roleDd);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String[] items = new String[] {"Worker"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        roleDd.setAdapter(adapter);


        if (user != null) {
            userId = user.getUid();
        }
        myRef = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Added information to database: \n" + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.show(ManagerRegistrationActivity.this, "Please wait...", "Processing...", true);
                (mAuth.createUserWithEmailAndPassword(emailregEt.getText().toString(), passwordregEt.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(ManagerRegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                        } else {
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(ManagerRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ManagerRegistrationActivity.this, "Email verification sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerRegistrationActivity.this, ManagerConfirmingAccount.class);

                    String name = nameregEt.getText().toString();
                    String email = emailregEt.getText().toString();
                    String password = passwordregEt.getText().toString();
                    String phoneNum = phoneregEt.getText().toString();
                    String item = roleDd.getSelectedItem().toString();
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    intent.putExtra("phone", phoneNum);
                    intent.putExtra("role", item);

                    Log.d(TAG, "onClick: Attempting to submit to database \n" +
                            "name: " + name + "\n" +
                            "email: " + email + "\n" +
                            "phone number: " + phoneNum + "\n" +
                            "role: " + item
                    );
                    startActivity(intent);
                } else {
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0 , 0);
                    startActivity(getIntent());
                }
            }
        });
    }
}
