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
import android.widget.SearchView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RentSearchFragment extends Fragment implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 12;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    //passing data from fragment to containing activity
    PassDataInterface passDataInterface;
    private SearchView mSearchText;
    private static final String TAG = RentSearchFragment.class.getName();


    public RentSearchFragment() {
        // Required empty public constructor
    }

    //if don't override onAttach, passing data from fragment to activity gives an exception error!
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
        mSearchText = view.findViewById(R.id.search_input);

        searchLocation();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mapStyle(mGoogleMap); //styles the map

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
                    //mGoogleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Here is the current location"));
                    mGoogleMap.setMyLocationEnabled(true); //sets blue dot for your location
                    //remove my location button. cuz cannot change the position and need room for search bar
                    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                }
                else{
                    Log.d(TAG, "current location == null");
                    Toast.makeText(getActivity(), "current location == null", Toast.LENGTH_LONG).show();
                }
            }
        });

        showZipCode();
    }

    private void showZipCode() {
        //if press on map, it will fetch the zipcode and pass to MainActivity_Rent to show on navbar
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                mGoogleMap.clear();
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                try
                {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses!=null && addresses.size()>0)
                    {
                        String postal_code = addresses.get(0).getPostalCode();
                        //pass to MainActivity_Rent to show on navbar
                        passDataInterface.onDataPass(postal_code);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onMapClick: "+e.getMessage());
                }

            }
        });
    }

    private void mapStyle(GoogleMap googleMap) {
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
    }

    private void searchLocation(){
        Log.d(TAG, "searchLocation: searching...");
        
        mSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do your task here
                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> list = new ArrayList<>();
                Log.d(TAG, "query: "+query);
                try{
                    list = geocoder.getFromLocationName(query, 1);
                    //problem is here. getFromLocationName returns empty list --> size 0
                }
                catch(IOException e){
                    Log.e(TAG, "geocoder.getFromLocationName-IOException: " + e.getMessage());
                }

                if(list.size() > 0){
                    Address address = list.get(0);
                    Log.d(TAG, "found a location: " + address.toString());
                    //Toast.makeText(getActivity(), "found a location: "+address.toString(), Toast.LENGTH_LONG).show();
                    moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
                }
                else
                {
                    Toast.makeText(getActivity(), "geocoder.getFromLocationName: null", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "geocoder.getFromLocationName: null");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called")
                return false;
            }
        });
    }

    private void moveCamera(LatLng latLng, int zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: "+latLng.latitude + ", lng: "+latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
        mGoogleMap.addMarker(markerOptions);
    }
}