package com.example.cribapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cribapp.Models.Listing;
import com.example.cribapp.Utility.PassDataInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RentSearchFragment extends Fragment implements OnMapReadyCallback{

    private static final int DEFAULT_ZOOM = 12;
    private static final int REQUEST_CODE = 101;
    private static final String TAG = RentSearchFragment.class.getName();

    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PassDataInterface passDataInterface; //pass data from fragment to activity

    private SearchView mSearchText;
    private ImageView mGps;
    private ImageView mAdd;

    //for connecting Firestore and retrive listing
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listingRef = db.collection("listing");


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
        mGps = view.findViewById(R.id.icon_my_location);
        mAdd = view.findViewById(R.id.icon_add);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        //styles the map
        mapStyle(mGoogleMap);

        //check LOCATION PERMISSION
        getPermission();

        //set current location blue dot
        mGoogleMap.setMyLocationEnabled(true); //sets blue dot for your location
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

        //go to current location
        getDeviceLocation();

        //initialize searchBar, currentLocation button, zipCode finder
        init();

        //load listings with custom icons
        loadListings();

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadListings() {
        Log.d(TAG, "loadListings: initializing");

        listingRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.isEmpty()){
                            Log.e(TAG, "onSuccess: queryDocumentSnapshots.isEmpty()==true");
                        }

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Listing listing = documentSnapshot.toObject(Listing.class);
                            double latitude = listing.getLatitude();
                            double longitude = listing.getLongitude();
                            String price = Integer.toString(listing.getPrice());

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title("$"+price)
                                    .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.ic_custom_listing_24dp));
                            mGoogleMap.addMarker(markerOptions);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "loadListing: error!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: loadListing error: either retriving or customizing the icons" );
                    }
                });
    }

    //for customizing the marker @loadListing
    private BitmapDescriptor bitmapDescriptorFromVector (Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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

    private void getPermission() {
        Log.d(TAG, "getPermission: getting permissions");

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
            return;
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    currentLocation = location;
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                }
                else{
                    Log.d(TAG, "current location == null");
                    Toast.makeText(getActivity(), "current location == null", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init(){
        Log.d(TAG, "init: initializing...");

        //search box
        mSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do your task here
                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> list = new ArrayList<>();
                Log.d(TAG, "query: "+query);
                try{
                    list = geocoder.getFromLocationName(query, 1);
                    //PROBLEM: geocoder.getFromLocationName returns null
                    // Geocoder.java
                    // import android.os.ServiceManager;
                }
                catch(IOException e){
                    Log.e(TAG, "geocoder.getFromLocationName: IOException: " + e.getMessage());
                }

                if(list.size() > 0){
                    Address address = list.get(0);
                    Log.d(TAG, "found a location: " + address.toString());
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

        //current location button
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        //add new listing
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked add icon");
                startActivity(new Intent(getActivity(), LLAddRent.class));
            }
        });

        showZipCode();
    }

    private void moveCamera(LatLng latLng, int zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: "+latLng.latitude + ", lng: "+latLng.longitude);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mGoogleMap.addMarker(markerOptions);
        }
    }

    private void showZipCode() {
        //if press on map, it will fetch the zipcode and pass to MainActivity_Rent to show on navbar
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try
                {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    //PROBLEM: geocoder.getFromLocation returns null
                    // Geocoder.java
                    // import android.os.ServiceManager;
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
}