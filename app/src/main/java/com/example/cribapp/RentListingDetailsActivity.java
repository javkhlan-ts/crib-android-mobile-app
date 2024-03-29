package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cribapp.Config.PayPalConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentListingDetailsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ListingDetailsActivity";
    public static final String LISTING_ID = "receivedListingId";
    public static final String FRAGMENT_ID = "receivedFragmentId";
    private static final int PAYPAL_REQEUST_CODE = 123;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
    private TextView mAmenities;
    private Button mApply;

    private static PayPalConfiguration payPalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

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
        mAmenities = findViewById(R.id.listing_amenities);
        mApply = findViewById(R.id.button_apply);

        //start Paypal service and this service needs to be stopped @ onDestroy
        Intent intentPaypalService = new Intent(this, PayPalService.class);
        intentPaypalService.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        startService(intentPaypalService);

        displayListingDetails(listingId);

        mFavorites.setOnCheckedChangeListener(this);
        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApplyConfirmationDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void showApplyConfirmationDialog() {
        Log.d(TAG, "showApplyConfirmationDialog: started.");

        final Dialog dialog = new Dialog(RentListingDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply);

        final TextView mApplicationForm = dialog.findViewById(R.id.apply_application_form);
        final TextView mPaystub = dialog.findViewById(R.id.apply_paystub);
        final TextView mBankStatement = dialog.findViewById(R.id.apply_bank_statement);
        final TextView mId = dialog.findViewById(R.id.apply_id);
        Button mCancel = dialog.findViewById(R.id.button_cancel);
        Button mConfirmAndPay = dialog.findViewById(R.id.button_confirm_and_pay);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CollectionReference collectionReference = db.collection("myApplication");
        DocumentReference documentReference = collectionReference.document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Log.d(TAG, "onComplete: data: "+documentSnapshot.getData());
                        if(documentSnapshot.getString("ApplicationForm")!=null){
                            mApplicationForm.setText(" * Application form");
                        } else {
                            mApplicationForm.setVisibility(View.GONE);
                        }
                        if(documentSnapshot.getString("Paystub")!=null){
                            mPaystub.setText(" * Paystub");
                        } else {
                            mPaystub.setVisibility(View.GONE);
                        }
                        if(documentSnapshot.getString("BankStatement")!=null){
                            mBankStatement.setText(" * Bank statement");
                        } else {
                            mBankStatement.setVisibility(View.GONE);
                        }
                        if(documentSnapshot.getString("Id")!=null){
                            mId.setText(" * I.D.");
                        } else {
                            mId.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RentListingDetailsActivity.this, "Upload documents on \"My Application\"!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        
        mConfirmAndPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Paypal started.");
                paypalPayment();
            }
        });

        dialog.show();
    }

    public void paypalPayment(){
        int paymentAmount = 50;
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "USD", "Application payment", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //startActivityForResult calls onActivityResult, hence it needs to be defined
        startActivityForResult(intent, PAYPAL_REQEUST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PAYPAL_REQEUST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Payment successful.", Toast.LENGTH_SHORT).show();
                //it also sends the document to landlord, which has not implemented yet.
                // Let's just log userId, so that landlord can access the documents in the later part
                Log.d(TAG, "onActivityResult: userId: "+FirebaseAuth.getInstance().getCurrentUser().getUid());
            } else {
                Toast.makeText(this, "Payment not successful.", Toast.LENGTH_SHORT).show();
            }
        }
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

                        //1.0 => 1
                        DecimalFormat decimalFormat = new DecimalFormat("0.#");

                        String price = document.get("price").toString();
                        String bed = decimalFormat.format(document.get("beds"));
                        //String bed = document.get("beds").toString();
                        //String bath = document.get("baths").toString();
                        String bath = decimalFormat.format(document.get("baths"));
                        String sqft = decimalFormat.format(document.get("squareFeet"));
                        //String sqft = document.get("squareFeet").toString();
                        String address = document.getString("address1");
                        String type = document.getString("property");
                        String desc = document.getString("description");
                        //cast array to ArrayList
                        ArrayList<String> amenities = (ArrayList<String>) document.get("amenities");
//                        Log.d(TAG, "onComplete: amenities: "+amenities);
//                        Log.d(TAG, "onComplete: mAmenities: "+mAmenities);

                        //== does not work. use equals for Strings!!!
                        if(bed.equals("0")){
                            mBedBathSqft.setText("Studio"+" | "+bath+ " bath | "+sqft+" sqft");
                        } else {
                            mBedBathSqft.setText(bed+" bed | "+bath+ " bath | "+sqft+" sqft");
                        }
                        mPrice.setText("$"+price);
                        mAddress.setText(address);
                        mType.setText(type);
                        mDescription.setText(desc);

                        if(amenities!=null) {
                            mAmenities.setText("");
                            for (int i = 0; i < amenities.size(); i++) {
                                mAmenities.append(" * " + amenities.get(i) + "\n");
                            }
                        } else {
                            mAmenities.setText("No amenities. Please read description for more information.");
                        }

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
                        Log.d(TAG, "onDataChange: listing is already in favorites.");
                        //Toast.makeText(RentListingDetailsActivity.this, "Already saved in favorites", Toast.LENGTH_SHORT).show();
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