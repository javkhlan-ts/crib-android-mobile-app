package com.example.cribapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginForgotPassword extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_forgot_password);

        mBackButton = findViewById(R.id.imageViewIconBack);
        mSignIn = findViewById(R.id.textViewSignIn);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForgotPassword.this, LoginLogin.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForgotPassword.this, LoginLogin.class));
            }
        });
    }
}
