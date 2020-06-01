package com.example.cribapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cribapp.RentMainActivity;
import com.example.cribapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginMainActivity extends AppCompatActivity {

    private Button buttonRent;
    private Button buttonBuy;
    private Button buttonLandlord;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        buttonRent = findViewById(R.id.buttonRent);
        buttonBuy = findViewById(R.id.buttonBuy);
        buttonLandlord = findViewById(R.id.buttonLandlord);
        mAuth = FirebaseAuth.getInstance();

        //if signed in and stuck
        //FirebaseAuth.getInstance().signOut();

        buttonRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivityRegisterOrLogin();
            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivityRegisterOrLogin();
            }
        });

        buttonLandlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openActivityRegisterOrLogin();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, RentMainActivity.class));
        }
    }

    public void openActivityRegisterOrLogin(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
