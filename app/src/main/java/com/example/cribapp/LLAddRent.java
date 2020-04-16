package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LLAddRent extends AppCompatActivity {

    private EditText mAddress1;
    private EditText mAddress2;
    private Spinner mState;
    private EditText mZip;
    private EditText mPrice;
    private Button mSave;
    private Button mCancel;
    private ImageView mBack;

    private FirebaseFirestore db;

    private String stateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lladd_rent);

        mAddress1 = findViewById(R.id.StreetAddress1);
        mAddress2 = findViewById(R.id.StreetAddress2);
        mState = findViewById(R.id.stateSpinner);
        mZip = findViewById(R.id.ZipCode);
        mPrice = findViewById(R.id.RentPrice);
        mSave = findViewById(R.id.buttonSave);
        mCancel = findViewById(R.id.buttonCancel);
        mBack = findViewById(R.id.imageViewIconBack);

        db = FirebaseFirestore.getInstance();

        initStateSpinner();
        addListenerOnButton();
    }

    private void addListenerOnButton() {
        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stateString = parent.getItemAtPosition(position).toString();
                stateStr = stateString;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LLAddRent.this, MainActivity_Rent.class));
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LLAddRent.this, MainActivity_Rent.class));
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address1 = mAddress1.getText().toString().trim();
                String address2 = mAddress2.getText().toString().trim();
                String zip = mZip.getText().toString().trim();
                String price = mPrice.getText().toString().trim();

                if(validate(address1, zip, price)){
                    CollectionReference dbListing = db.collection("listing");
                    Listing listing = new Listing (address1, address2, stateStr, Integer.parseInt(zip), Integer.parseInt(price));
                    dbListing.add(listing).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(LLAddRent.this, "Listing added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LLAddRent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validate(String address1, String zip, String price) {
        if (address1.isEmpty()) {
            mAddress1.setError("Address required");
            mAddress1.requestFocus();
            return false;
        }

        if (zip.isEmpty()) {
            mZip.setError("Zip required");
            mZip.requestFocus();
            return false;
        }

        if (price.isEmpty()) {
            mPrice.setError("Description required");
            mPrice.requestFocus();
            return false;
        }

        return true;
    }

    private void initStateSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(LLAddRent.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.states_list));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(arrayAdapter);
    }
}
