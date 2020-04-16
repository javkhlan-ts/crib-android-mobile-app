package com.example.cribapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class LLAddRent extends AppCompatActivity {

    private EditText mAddress1;
    private EditText mAddress2;
    private Spinner mState;
    private EditText mZip;
    private EditText mPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lladd_rent);

        mAddress1 = findViewById(R.id.StreetAddress1);
        mAddress2 = findViewById(R.id.StreetAddress2);
        mState = findViewById(R.id.stateSpinner);
        mZip = findViewById(R.id.ZipCode);
        mPrice = findViewById(R.id.RentPrice);

        initStateSpinner(mState);

    }

    private void initStateSpinner(Spinner spinner) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LLAddRent.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.states_list));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}
