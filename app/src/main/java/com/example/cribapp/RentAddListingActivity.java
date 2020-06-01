package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cribapp.Models.Listing;
import com.example.cribapp.Models.shareImageUrl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentAddListingActivity extends AppCompatActivity {

    private static final String TAG = "RentAddListingActivity";
    private EditText mAddress1;
    private EditText mAddress2;
    private Spinner mState;
    private EditText mCity;
    private EditText mZip;
    private EditText mSquareFeet;
    private EditText mPrice;
    private RadioGroup mBeds;
    private int mBedsValue;
    private RadioGroup mBaths;
    private double mBathsValue;
    private RadioGroup mPropertyType;
    private String mPropertyTypeValue;
    private EditText mDescription;
    private CheckBox mLaundry, mAC, mHeating, mParking, mGatedEntry, mDoorman, mGym, mPool, mDishwasher, mFurnished;
    private ArrayList<String> mAmenities;

    private Button mSave;
    private Button mCancel;
    private ImageView mBack;

    private FirebaseFirestore db;
    private String stateStr;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    //private String mImageUrl; //global variable returns null
    private shareImageUrl mSharedImageUrl;
    private FirebaseFirestore mFirebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_add_listing);

        mAddress1 = findViewById(R.id.StreetAddress1);
        mAddress2 = findViewById(R.id.StreetAddress2);
        mState = findViewById(R.id.stateSpinner);
        mCity = findViewById(R.id.City);
        mZip = findViewById(R.id.ZipCode);
        mSquareFeet = findViewById(R.id.SquareFeet);
        mPrice = findViewById(R.id.RentPrice);
        mBeds = findViewById(R.id.RadioGroupBeds);
        mBaths = findViewById(R.id.RadioGroupBaths);
        mPropertyType = findViewById(R.id.RadioGroupPropertyType);
        mDescription = findViewById(R.id.Description);

        mLaundry = findViewById(R.id.checkBoxLaundry);
        mAC = findViewById(R.id.checkBoxAC);
        mHeating = findViewById(R.id.checkBoxHeating);
        mParking = findViewById(R.id.checkBoxParking);
        mGatedEntry = findViewById(R.id.checkBoxGatedEntry);
        mDoorman = findViewById(R.id.checkBoxDoorman);
        mGym = findViewById(R.id.checkBoxGym);
        mPool = findViewById(R.id.checkBoxPool);
        mDishwasher = findViewById(R.id.checkBoxDishwasher);
        mFurnished = findViewById(R.id.checkBoxFurnished);

        mSave = findViewById(R.id.buttonSave);
        mCancel = findViewById(R.id.buttonCancel);
        mBack = findViewById(R.id.imageViewIconBack);

        db = FirebaseFirestore.getInstance();

        mButtonChooseImage = findViewById(R.id.buttonChooseImage);
        mImageView = findViewById(R.id.ListingPhotos);
        mProgressBar = findViewById(R.id.progressBar);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("listing");

        mSharedImageUrl = new shareImageUrl();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

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
                startActivity(new Intent(RentAddListingActivity.this, RentMainActivity.class));
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentAddListingActivity.this, RentMainActivity.class));
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAmenities();

                String address1 = mAddress1.getText().toString().trim();
                String address2 = mAddress2.getText().toString().trim();
                String state = stateStr;
                String city = mCity.getText().toString().trim();
                String zip = mZip.getText().toString().trim();
                String squareFeet = mSquareFeet.getText().toString().trim();
                String price = mPrice.getText().toString().trim();
                int beds = mBedsValue;
                double baths = mBathsValue;
                String property = mPropertyTypeValue;
                String description = mDescription.getText().toString().trim();
                ArrayList<String> amenities = mAmenities;

                String concatinatedAddress;
                String [] latlong;
                double latitude;
                double longitude;

                //checks required fields
                if(validate(address1, zip, price)){
                    //get longitude and latitude
                    concatinatedAddress = getLatLong();
                    latlong = concatinatedAddress.split(",");
                    latitude = Double.parseDouble(latlong[0]);
                    longitude = Double.parseDouble(latlong[1]);
                    Log.d(TAG, "onClick: latitude: "+latlong[0]+", longitude: "+latlong[1]);

                    //save to Firestore
                    CollectionReference dbListing = db.collection("listing");
                    final Listing listing = new Listing (address1, address2, state, city, Integer.parseInt(zip),
                            Double.parseDouble(squareFeet), Integer.parseInt(price), beds, baths, property,
                            description, amenities, latitude, longitude);
                    dbListing.add(listing).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(RentAddListingActivity.this, "Listing added", Toast.LENGTH_SHORT).show();
                            uploadImage(documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RentAddListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                startActivity(new Intent(RentAddListingActivity.this, RentMainActivity.class));
            }
        });

        mBeds.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.RadioGroupBedsStudio:
                        mBedsValue = 0;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBeds1:
                        mBedsValue = 1;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBeds2:
                        mBedsValue = 2;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBeds3:
                        mBedsValue = 3;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBeds4:
                        mBedsValue = 4;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBeds5:
                        mBedsValue = 5;
                        Toast.makeText(getApplicationContext(), ""+mBedsValue, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), ""+mBeds.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mBaths.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.RadioGroupBaths1:
                        mBathsValue = 1;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBaths1_5:
                        mBathsValue = 1.5;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBaths2:
                        mBathsValue = 2;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBaths3:
                        mBathsValue = 3;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBaths4:
                        mBathsValue = 4;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupBaths5:
                        mBathsValue = 5;
                        Toast.makeText(RentAddListingActivity.this, ""+mBathsValue, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RentAddListingActivity.this, ""+mBaths.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mPropertyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.RadioGroupPropertyType1:
                        mPropertyTypeValue = "Apartment";
                        Toast.makeText(RentAddListingActivity.this, ""+mPropertyTypeValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupPropertyType2:
                        mPropertyTypeValue = "Condo";
                        Toast.makeText(RentAddListingActivity.this, ""+mPropertyTypeValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupPropertyType3:
                        mPropertyTypeValue = "Duplex";
                        Toast.makeText(RentAddListingActivity.this, ""+mPropertyTypeValue, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.RadioGroupPropertyType4:
                        mPropertyTypeValue = "House";
                        Toast.makeText(RentAddListingActivity.this, ""+mPropertyTypeValue, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(RentAddListingActivity.this, ""+mPropertyType.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFireChooser();
            }
        });
    }

    private void updateAmenities() {
        mLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLaundry.isChecked())
                    mAmenities.add("Laundry");
            }
        });
        mAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAC.isChecked())
                    mAmenities.add("AC");
            }
        });
        mHeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHeating.isChecked())
                    mAmenities.add("Heating");
            }
        });
        mParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mParking.isChecked())
                    mAmenities.add("Parking");
            }
        });
        mGatedEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGatedEntry.isChecked())
                    mAmenities.add("Gated Entry");
            }
        });
        mDoorman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDoorman.isChecked())
                    mAmenities.add("Doorman");
            }
        });
        mGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGym.isChecked())
                    mAmenities.add("Gym");
            }
        });
        mPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPool.isChecked())
                    mAmenities.add("Pool");
            }
        });
        mDishwasher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDishwasher.isChecked())
                    mAmenities.add("Dishwasher");
            }
        });
        mFurnished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFurnished.isChecked())
                    mAmenities.add("Furnished");
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(final String listingId){
        Log.d(TAG, "uploadImage: initiated. Passed listingId: "+listingId);
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri);

            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Map<String, Object> imageUrlMap = new HashMap<>();
                        imageUrlMap.put("imageUrl", downloadUri.toString());
                        mFirebaseFirestore.collection("listing").document(listingId).update(imageUrlMap);
                        Log.d(TAG, "uploadImage(): imageUrl: "+downloadUri.toString());
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFireChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
        }
    }

    private String getLatLong() {
        Log.d(TAG, "getLatLong: initializing");

        String concatinatedAddress = "0.0,0.0";
        String givenAddress = mAddress1.getText().toString();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try{
            List<Address> addressList = geocoder.getFromLocationName(givenAddress, 1);
            if(addressList.size()>0){
                Address address=addressList.get(0);
                Log.d(TAG, "getLatLong: address found");
                concatinatedAddress = address.getLatitude()+","+address.getLongitude();
            }
        }
        catch (IOException e){
            Log.e(TAG, "getLatLong: "+e.getMessage() );
        }
        return concatinatedAddress;
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RentAddListingActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.states_list));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mState.setAdapter(arrayAdapter);
    }
}
