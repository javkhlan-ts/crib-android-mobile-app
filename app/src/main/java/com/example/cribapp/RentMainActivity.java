package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.cribapp.Utility.PassDataInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RentMainActivity extends AppCompatActivity implements PassDataInterface {

    private static final String TAG = "RentMainActivity";
    public static final String FRAGMENT_ID = "receivedFragmentId";
    private BottomNavigationView bottomNavigationView;
    private TextView mReceivedZip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        mReceivedZip = findViewById(R.id.navZip);

        Intent intent = getIntent();
        String fragmentId = intent.getStringExtra(FRAGMENT_ID)==null ? "0" : intent.getStringExtra(FRAGMENT_ID);
        switch (fragmentId){
            case "1":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentFavoritesFragment()).commit();
                break;
            case "2":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentAlertsFragment()).commit();
                break;
            case "3":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentAccountFragment()).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RentSearchFragment()).commit();
                break;
        }
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
                    fragment = new RentFavoritesFragment();
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