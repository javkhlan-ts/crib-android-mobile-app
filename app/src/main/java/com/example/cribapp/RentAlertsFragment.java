package com.example.cribapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cribapp.Utility.PassDataInterface;

public class RentAlertsFragment extends Fragment {
    private static final String TAG = "RentAlertsFragment";

    //to pass title from frag to activity navbar
    PassDataInterface passTitleFromFragToRentMainActivity;


    public RentAlertsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rent_alerts, container, false);
        passTitleFromFragToRentMainActivity.onDataPass("Alerts");

        // Inflate the layout for this fragment
        return view;
    }

}
