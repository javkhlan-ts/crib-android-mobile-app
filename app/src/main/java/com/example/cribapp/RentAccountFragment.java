package com.example.cribapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cribapp.Login.LoginBuyRentLandlord;
import com.google.firebase.auth.FirebaseAuth;

public class RentAccountFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button mLogoutButton;

    public RentAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_account, container, false);
        mAuth = FirebaseAuth.getInstance();
        mLogoutButton = view.findViewById(R.id.buttonLogout);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginBuyRentLandlord.class));
            }
        });

        return view;
    }
}