package com.example.cribapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RentListingDetailsActivity extends AppCompatActivity {
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_listing_details);

        mBack = findViewById(R.id.imageViewIconBack);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentListingDetailsActivity.this, RentMainActivity.class));
            }
        });


    }
}