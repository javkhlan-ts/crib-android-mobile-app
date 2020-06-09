package com.example.cribapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cribapp.Login.LoginMainActivity;
import com.example.cribapp.Utility.PassDataInterface;
import com.google.firebase.auth.FirebaseAuth;

public class RentAccountFragment extends Fragment {
    private static final String TAG = "RentAccountFragment";
    private FirebaseAuth mAuth;

    //variables
    private Button mAccountSettings;
    private Button mMyApplication;
    private TextView mLoggedInUserEmail;
    private Button mLogoutButton;

    //to pass title from frag to activity navbar
    PassDataInterface passTitleFromFragToRentMainActivity;

    public RentAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        passTitleFromFragToRentMainActivity = (PassDataInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_account, container, false);
        mAuth = FirebaseAuth.getInstance();
        mAccountSettings = view.findViewById(R.id.button_account_settings);
        mMyApplication = view.findViewById(R.id.button_my_application);
        mLoggedInUserEmail = view.findViewById(R.id.loggedin_user_email);
        mLogoutButton = view.findViewById(R.id.buttonLogout);

        passTitleFromFragToRentMainActivity.onDataPass("Account");

        if(mAuth.getCurrentUser().getEmail()!=null){
            mLoggedInUserEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        initButtons();

        return view;
    }

    private void initButtons() {
        Log.d(TAG, "initButtons: started.");

        mAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fragmentId = "3";
                Intent intent = new Intent(getActivity(), RentAccountSettingsActivity.class);
                intent.putExtra(RentAccountSettingsActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
                //Toast.makeText(getActivity(), "Account Settings clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        mMyApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fragmentId = "3";
                Intent intent = new Intent(getActivity(), RentMyApplicationActivity.class);
                intent.putExtra(RentMyApplicationActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginMainActivity.class));
            }
        });
    }
}