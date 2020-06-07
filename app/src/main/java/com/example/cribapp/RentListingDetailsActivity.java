package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.HashMap;
import java.util.Map;

public class RentListingDetailsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ListingDetailsActivity";
    public static final String LISTING_ID = "receivedListingId";
    public static final String FRAGMENT_ID = "receivedFragmentId";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listingRef = db.collection("listing");

    private ImageView mBack;
    private CheckBox mFavorites;
    private ImageView mListingImage;
    private TextView mBedBathSqft;
    private TextView mPrice;
    private TextView mAddress;
    private TextView mType;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_listing_details);

        Intent intent = getIntent();
        String listingId = intent.getStringExtra(LISTING_ID);
        final String fragmentId = intent.getStringExtra(FRAGMENT_ID);

        mBack = findViewById(R.id.imageViewIconBack);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentListingDetailsActivity.this, RentMainActivity.class);
                Log.d(TAG, "FRAGMENT_ID: "+fragmentId);
                intent.putExtra(RentMainActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
            }
        });
        mFavorites = findViewById(R.id.checkbox_favorites);
        mListingImage = findViewById(R.id.listing_images);
        mBedBathSqft = findViewById(R.id.listing_bed_bath_sqft);
        mPrice = findViewById(R.id.listing_price);
        mAddress = findViewById(R.id.listing_address);
        mType = findViewById(R.id.listing_type);
        mDescription = findViewById(R.id.listing_description);

        displayListingDetails(listingId);

        mFavorites.setOnCheckedChangeListener(this);
    }

    private void displayListingDetails(String listingId) {

        Log.d(TAG, "displayListingDetails: initiated...");
        DocumentReference docRef = listingRef.document(listingId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String imageUrl = document.getString("imageUrl");
                        Glide.with(getApplicationContext()).load(imageUrl).into(mListingImage);

                        String price = document.get("price").toString();
                        String bed = document.get("beds").toString();
                        String bath = document.get("baths").toString();
                        String sqft = document.get("squareFeet").toString();
                        String address = document.getString("address1");
                        String type = document.getString("property");
                        String desc = document.getString("description");

                        mBedBathSqft.setText("Bed "+bed+" | Bath "+bath+ " | "+sqft);
                        mPrice.setText("$"+price);
                        mAddress.setText(address);
                        mType.setText(type);
                        mDescription.setText(desc);

                        //set favorites when going into listing details
                        Intent intent = getIntent();
                        final String listingId = intent.getStringExtra(LISTING_ID);
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites");
                        final DatabaseReference favoriteListingRef = favRef.child(userId).getRef();
                        favoriteListingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mFavorites.setChecked(false);
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    if(data.getValue().toString().equals(listingId)){
                                        mFavorites.setChecked(true);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "displayListingDetails: retrive docRef failed!", task.getException());
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //if not logged in, toast and set unchecked
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(this, "Please login to save to favorites", Toast.LENGTH_SHORT).show();
            buttonView.setChecked(false);
            return;
        }

        DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favorites");

        //if checked, check the listing is already in favorites list
        if(isChecked){
            buttonView.setChecked(true);
            Intent intent = getIntent();
            final String listingId = intent.getStringExtra(LISTING_ID);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference favoriteListingRef = favRef.child(userId).getRef();

            favoriteListingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean listingExists = false;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.getValue().toString().equals(listingId)){
                            listingExists = true;
                        }
                    }
                    Log.d(TAG, "favoritesListing: listingExists: "+listingExists);
                    if(listingExists==false){
                        Map<String, Object> listingToSave = new HashMap<>();
                        listingToSave.put(""+System.currentTimeMillis(), listingId);
                        favoriteListingRef.updateChildren(listingToSave);
                        Toast.makeText(RentListingDetailsActivity.this, "Listing saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RentListingDetailsActivity.this, "Already saved in favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            buttonView.setChecked(false);
            Intent intent = getIntent();
            final String listingId = intent.getStringExtra(LISTING_ID);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference favoriteListingRef = favRef.child(userId).getRef();

            favoriteListingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean listingExists = false;
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        if(data.getValue().toString().equals(listingId)){
                            listingExists = true;
                            data.getRef().removeValue();
                        }
                    }
                    if(listingExists==false){
                        Toast.makeText(RentListingDetailsActivity.this, "Listing is not in favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RentListingDetailsActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}