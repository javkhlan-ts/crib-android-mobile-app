package com.example.cribapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cribapp.MainActivity_Rent;
import com.example.cribapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginBuyRentLandlord extends AppCompatActivity {

    private Button buttonRent;
    private Button buttonBuy;
    private Button buttonLandlord;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_buy_rent_landlord);

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
            startActivity(new Intent(this, MainActivity_Rent.class));
        }
    }

    public void openActivityRegisterOrLogin(){
        Intent intent = new Intent(this, LoginLogin.class);
        startActivity(intent);
    }
}
