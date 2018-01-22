package com.ebookfrenzy.quovie;

import android.content.Intent;
import android.preference.PreferenceGroup;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    //Get the references from the UI
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText)findViewById(R.id.email);
        mPasswordView = (EditText)findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = (ProgressBar)findViewById(R.id.login_progress);

        Button mEmailSignInButton = (Button)findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        Button mEmailCreateAccountButton = (Button)findViewById(R.id.create_account_button);
        mEmailCreateAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    //The register method
    private void registerUser(){
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (email.isEmpty()){
            mEmailView.setError("Email is required");
            mEmailView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailView.setError("Invalid email....Please enter a valid email");
        }

        if (password.isEmpty()) {
            mPasswordView.setError("Password is Required");
            mPasswordView.requestFocus();
            return;
        }

        if (password.length() < 8){
            mPasswordView.setError("Password must be atleast 8 characters long");
            mPasswordView.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        //This is when the user officially gets registered
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Successfully created an account!... Try logging in!", Toast.LENGTH_LONG).show();
                }
                //this will say that the user is already registered
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(LoginActivity.this, "This user is already registered", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void loginUser(){
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        if (email.isEmpty()){
            mEmailView.setError("Email is required");
            mEmailView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailView.setError("Invalid email...Please enter a valid email");
        }

        if (password.isEmpty()){
            mPasswordView.setError("Password is required");
            mPasswordView.requestFocus();
            return;
        }

        if (password.length() < 8){
            mPasswordView.setError("Password must be atleast 8 characters long");
            mPasswordView.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, "Successful Login", Toast.LENGTH_SHORT).show();
                    //send them to the main activity
                    Intent i = new Intent(LoginActivity.this, QuovieMainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
