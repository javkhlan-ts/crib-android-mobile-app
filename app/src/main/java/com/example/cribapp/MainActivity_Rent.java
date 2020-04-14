package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity_Rent extends AppCompatActivity implements PassDataInterface {

    private BottomNavigationView bottomNavigationView;
    private TextView mReceivedZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rent);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentSearchFragment()).commit();

        mReceivedZip = findViewById(R.id.navZip);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.bottomNavSearch:
                    fragment = new RentSearchFragment();
                    break;
                case R.id.bottomNavFavorites:
                    fragment = new RentFavoritesFragement();
                    break;
                case R.id.bottomNavAlerts:
                    fragment = new RentAlertsFragment();
                    break;
                case R.id.bottomNavAccount:
                    fragment = new RentAccountFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

            return true; //default was false and it worked too! not sure what it does???
        }
    };

    @Override
    public void onDataPass(String data) {
        mReceivedZip.setText(data);
    }
}