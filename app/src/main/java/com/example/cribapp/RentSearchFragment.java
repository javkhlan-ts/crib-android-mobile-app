package com.example.cribapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RentSearchFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    public RentSearchFragment() {
        // Required empty public constructor
    }

    //passing data from fragment to containing activity
    PassDataInterface passDataInterface;
    //if don't override onAttach, exception error!
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        passDataInterface = (PassDataInterface) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rent_search, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // Customise the styling of the base map using a JSON object defined in a raw resource file.
        try
        {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity(), R.raw.mapstyle)); //replaced this with getActivity()

            if (!success) {
                Log.e("RentSearchFragment", "Style parsing failed.");
            }
        }
        catch (Resources.NotFoundException e)
        {
            Log.e("RentSearchFragment", "Can't find style. Error: ", e);
        }

        //check LOCATION PERMISSION and set current location Marker
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    currentLocation = location;
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mGoogleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Here is the current location"));
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
                }
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mGoogleMap.clear();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                try{
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses!=null && addresses.size()>0){
                        String postal_code = addresses.get(0).getPostalCode();
                        passDataInterface.onDataPass(postal_code);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}