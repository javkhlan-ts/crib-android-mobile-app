package com.example.cribapp;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cribapp.Adapter.FavoritesRecyclerViewAdapter;
import com.example.cribapp.Utility.PassDataInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RentFavoritesFragment extends Fragment{

    private static final String TAG = "RentFavoritesFragment";

    //variables for firestore
    private ArrayList<String> mListingImageUrlsArrayList = new ArrayList<>();
    private ArrayList<String> mBedBathSqrtArrayList = new ArrayList<>();
    private ArrayList<String> mPriceArrayList = new ArrayList<>();
    private ArrayList<String> mAddressArrayList = new ArrayList<>();
    private ArrayList<String> mTypeArrayList = new ArrayList<>();

    //variables for database
    private ArrayList<String> mListingIdArrayList = new ArrayList<>();

    //to passData as FAVORITES text to NavBar @ RentMain
    PassDataInterface passTitleFromFragToRentMainActivity;

    public RentFavoritesFragment() { /*Required empty public constructor*/ }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        passTitleFromFragToRentMainActivity = (PassDataInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        View view = inflater.inflate(R.layout.fragment_rent_favorites, container, false);

        passTitleFromFragToRentMainActivity.onDataPass("Favorites");

        initImageBitmaps();

        Log.d(TAG, "onCreateView: ended.");

        return view;
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: started.");

        getSavedListingIds();

        Log.d(TAG, "initImageBitmaps: ended.");
    }

    private void getSavedListingIds() {
        Log.d(TAG, "getSavedListingIds: started.");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("favorites").child(userId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: started.");
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String listingId = ds.getValue(String.class);
                    mListingIdArrayList.add(listingId);
                }
                getSavedListingDetails(new FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<String> urls, ArrayList<String> bbs, ArrayList<String> price, ArrayList<String> addr, ArrayList<String> type) {
                        mListingImageUrlsArrayList = new ArrayList<>(urls);
                        mBedBathSqrtArrayList = new ArrayList<>(bbs);
                        mPriceArrayList = new ArrayList<>(price);
                        mAddressArrayList = new ArrayList<>(addr);
                        mTypeArrayList = new ArrayList<>(type);
                        initRecyclerView();
                    }
                });
                Log.d(TAG, "onDataChange: ended.");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        favoritesRef.addListenerForSingleValueEvent(valueEventListener);
        Log.d(TAG, "getSavedListingIds: mListingIdArrayList: "+mListingIdArrayList.size());
        Log.d(TAG, "getSavedListingIds: ended.");
    }

    private void getSavedListingDetails(final FirebaseCallback firebaseCallback) {
        Log.d(TAG, "getSavedListingDetails: started.");
        Log.d(TAG, "getSavedListingDetails: mListingIdArrayList: "+mListingIdArrayList.size());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference listingRef = db.collection("listing");

        for(int i=0; i<mListingIdArrayList.size(); i++){
            DocumentReference docRef = listingRef.document(mListingIdArrayList.get(i));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String imageUrl = document.getString("imageUrl").trim();
                            String price = document.get("price").toString().trim();
                            String bed = document.get("beds").toString().trim();
                            String bath = document.get("baths").toString().trim();
                            String sqft = document.get("squareFeet").toString().trim();
                            String address = document.getString("address1").trim();
                            String type = document.getString("property").trim();

                            mListingImageUrlsArrayList.add(imageUrl);
                            mBedBathSqrtArrayList.add("Bed "+bed+" | Bath "+bath+ " | "+sqft);
                            mPriceArrayList.add("$"+price);
                            mAddressArrayList.add(address);
                            mTypeArrayList.add(type);

                            firebaseCallback.onCallback(mListingImageUrlsArrayList, mBedBathSqrtArrayList, mPriceArrayList, mAddressArrayList, mTypeArrayList);
                            //Log.d(TAG, "onComplete: mPriceArrayList: "+mPriceArrayList.size());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "getSavedListingDetails: retrive docRef failed!", task.getException());
                    }
                }
            });
        } //END OF FOR LOOP, END OF ARRAYLISTS
        Log.d(TAG, "getSavedListingDetails: before initRecyclerView(): mPriceArrayList: "+mPriceArrayList.size());
        //initRecyclerView();
        Log.d(TAG, "getSavedListingDetails: ended.");
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");
        Log.d(TAG, "initRecyclerView: mListingIdArrayList: "+mListingIdArrayList.size());
        Log.d(TAG, "initRecyclerView: mPriceArrayList: "+mPriceArrayList.size());

        View view = getView();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_favorites);
        FavoritesRecyclerViewAdapter adapter = new FavoritesRecyclerViewAdapter( getContext(), mListingImageUrlsArrayList, mBedBathSqrtArrayList, mPriceArrayList, mAddressArrayList, mTypeArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Log.d(TAG, "initRecyclerView: ended.");
    }

    interface FirebaseCallback{
        void onCallback(ArrayList<String> urls, ArrayList<String> bbs, ArrayList<String> price, ArrayList<String> addr, ArrayList<String> type);
    }
}