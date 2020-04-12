package com.example.cribapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginRegisterOrLogin extends AppCompatActivity {

    private ImageView buttonBack;
    private TextView buttonLogin;
    private Button buttonNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_or_login);

        buttonBack = findViewById(R.id.imageViewIconBack);
        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivityBuyRentLandlord();
            }
        });

        buttonLogin = findViewById(R.id.textViewLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLoginLogin();
            }
        });

        buttonNewAccount = findViewById(R.id.buttonNewAccount);
        buttonNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLoginRegister();
            }
        });
    }

    public void openActivityBuyRentLandlord(){
        Intent intent = new Intent(this, LoginBuyRentLandlord.class);
        startActivity(intent);
    }

    public void openActivityLoginLogin(){
        Intent intent = new Intent(this, LoginLogin.class);
        startActivity(intent);
    }

    public void openActivityLoginRegister(){
        Intent intent = new Intent(this, LoginRegister.class);
        startActivity(intent);
    }
}
