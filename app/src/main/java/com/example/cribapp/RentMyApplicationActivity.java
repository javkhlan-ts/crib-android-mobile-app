package com.example.cribapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class RentMyApplicationActivity extends AppCompatActivity {
    private static final String TAG = "RentMyApplication";
    public static final String FRAGMENT_ID = "fragmentId";

    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_my_application);

        mBack = findViewById(R.id.icon_back);

        init();
    }

    private void init() {
        Log.d(TAG, "init: started.");
        Intent intent = getIntent();
        final String fragmentId = intent.getStringExtra(FRAGMENT_ID);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentMyApplicationActivity.this, RentMainActivity.class);
                intent.putExtra(RentMainActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
            }
        });
    }
}
