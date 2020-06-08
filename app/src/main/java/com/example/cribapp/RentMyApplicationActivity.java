package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cribapp.Models.Listing;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class RentMyApplicationActivity extends AppCompatActivity {
    private static final String TAG = "RentMyApplication";
    public static final String FRAGMENT_ID = "fragmentId";
    private static final int UPLOAD_REQUEST = 123;

    private ImageView mBack;

    //ImageViews
    private ImageView mApplicationForm;
    private ImageView mPaystub;
    private ImageView mBankStatement;
    private ImageView mId;

    //Buttons
    private Button mDownloadApplicationForm;
    private Button mDownloadPaystub;
    private Button mDownloadBankStatement;
    private Button mDownloadId;
    private Button mUploadApplicationForm;
    private Button mUploadPaystub;
    private Button mUploadBankStatement;
    private Button mUploadId;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;
    private StorageTask mStorageTaskUpload;
    private Uri mImageUri;

    private int imageViewId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_my_application);

        mBack = findViewById(R.id.icon_back);

        mApplicationForm = findViewById(R.id.myapplication_application_form);
        mPaystub = findViewById(R.id.myapplication_paystub);
        mBankStatement = findViewById(R.id.myapplication_bank_statement);
        mId = findViewById(R.id.myapplication_id);

        mDownloadApplicationForm = findViewById(R.id.applicationform_download);
        mUploadApplicationForm = findViewById(R.id.applicationform_upload);
        mDownloadPaystub = findViewById(R.id.paystub_download);
        mUploadPaystub = findViewById(R.id.paystub_upload);
        mDownloadBankStatement = findViewById(R.id.bankstatement_download);
        mUploadBankStatement = findViewById(R.id.bankstatement_upload);
        mDownloadId = findViewById(R.id.id_download);
        mUploadId = findViewById(R.id.id_upload);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        //for now, crib can upload image to database, but cannot delete it when new image is uploaded
        //old images become garbage
        //need to save somewhere the uploadId~millisec and delete when upload
        initImageViews();
        initOnClickListeners();
    }

    private void initImageViews() {
        Log.d(TAG, "initImageViews: started.");
        CollectionReference collectionReference = mFirestore.collection("myApplication");
        DocumentReference documentReference = collectionReference.document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Log.d(TAG, "onComplete: data: "+documentSnapshot.getData());
                        if(documentSnapshot.getString("ApplicationForm")!=null){
                            String url = documentSnapshot.getString("ApplicationForm");
                            Glide.with(getApplicationContext()).load(url).into(mApplicationForm);
                        }
                        if(documentSnapshot.getString("Paystub")!=null){
                            String url = documentSnapshot.getString("Paystub");
                            Glide.with(getApplicationContext()).load(url).into(mPaystub);
                        }
                        if(documentSnapshot.getString("BankStatement")!=null){
                            String url = documentSnapshot.getString("BankStatement");
                            Glide.with(getApplicationContext()).load(url).into(mBankStatement);
                        }
                        if(documentSnapshot.getString("Id")!=null){
                            String url = documentSnapshot.getString("Id");
                            Glide.with(getApplicationContext()).load(url).into(mId);
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: initImageViews(): "+e.getMessage());
            }
        });
    }

    private void initOnClickListeners() {
        Log.d(TAG, "initOnClickListeners: started.");
        Intent intent = getIntent();
        final String fragmentId = intent.getStringExtra(FRAGMENT_ID);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentMyApplicationActivity.this, RentMainActivity.class);
                intent.putExtra(RentMainActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
            }
        });

        mUploadApplicationForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(0);
            }
        });

        mUploadPaystub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(1);
            }
        });

        mUploadBankStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(2);
            }
        });

        mUploadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(3);
            }
        });

        mDownloadApplicationForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(0);
            }
        });

        mDownloadPaystub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(1);
            }
        });

        mDownloadBankStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(2);
            }
        });

        mDownloadId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(3);
            }
        });
    }

    private void downloadImage(final int imageViewId){
        Log.d(TAG, "downloadImage: started.");
        this.imageViewId = imageViewId;

        CollectionReference collectionReference = mFirestore.collection("myApplication");
        DocumentReference documentReference = collectionReference.document(mAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d(TAG, "onComplete: downloadImage(): started.");
                Log.d(TAG, "onComplete: imageViewId: "+imageViewId);
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Log.d(TAG, "onComplete: data: "+documentSnapshot.getData());
                        if(imageViewId==0) {
                            if (documentSnapshot.getString("ApplicationForm") != null) {
                                //download with url
                                String url = documentSnapshot.getString("ApplicationForm");
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(RentMyApplicationActivity.this, DIRECTORY_DOWNLOADS, "ApplicationForm.jpg");
                                downloadManager.enqueue(request);
                            } else {
                                Toast.makeText(RentMyApplicationActivity.this, "Application form does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(imageViewId==1) {
                            if (documentSnapshot.getString("Paystub") != null) {
                                //download with url
                                String url = documentSnapshot.getString("Paystub");
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(RentMyApplicationActivity.this, DIRECTORY_DOWNLOADS, "Paystub.jpg");
                                downloadManager.enqueue(request);
                            } else {
                                Toast.makeText(RentMyApplicationActivity.this, "Paystub does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(imageViewId==2) {
                            if (documentSnapshot.getString("BankStatement") != null) {
                                //download with url
                                String url = documentSnapshot.getString("BankStatement");
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(RentMyApplicationActivity.this, DIRECTORY_DOWNLOADS, "BankStatement.jpg");
                                downloadManager.enqueue(request);
                            } else {
                                Toast.makeText(RentMyApplicationActivity.this, "Bank statement does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(imageViewId==3) {
                            if (documentSnapshot.getString("Id") != null) {
                                //download with url
                                String url = documentSnapshot.getString("Id");
                                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                Uri uri = Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                request.setDestinationInExternalFilesDir(RentMyApplicationActivity.this, DIRECTORY_DOWNLOADS, "Id.jpg");
                                downloadManager.enqueue(request);
                            } else {
                                Toast.makeText(RentMyApplicationActivity.this, "I.D. does not exist.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: downloadImage(): "+e.getMessage());
            }
        });
    }

    private void chooseImage(int imageViewId){
        Log.d(TAG, "chooseImage: started.");
        Log.d(TAG, "chooseImage: imageViewId: "+imageViewId);
        Intent imageIdIntent = new Intent();
        this.imageViewId = imageViewId;
        imageIdIntent.setType("image/*");
        imageIdIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imageIdIntent, UPLOAD_REQUEST);
        //startActivityForResult calls onActivityResult
        Log.d(TAG, "chooseImage: ended.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: started.");
        if (requestCode == UPLOAD_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            //this uri will be used in uploadImage, to check null
            mImageUri = data.getData();

            Log.d(TAG, "onActivityResult: imageViewId: "+imageViewId);

            switch (imageViewId){
                case 0:
                    mApplicationForm.setImageURI(mImageUri);
                    break;
                case 1:
                    mPaystub.setImageURI(mImageUri);
                    break;
                case 2:
                    mBankStatement.setImageURI(mImageUri);
                    break;
                case 3:
                    mId.setImageURI(mImageUri);
                    break;
                default:
                    Toast.makeText(this, "Problem with mImageUri!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: imageViewId problem!");
            }

            uploadImage(imageViewId);
        }
        Log.d(TAG, "onActivityResult: ended.");
    }

    private void uploadImage(final int imageViewId){
        Log.d(TAG, "uploadImage: started.");
        Log.d(TAG, "uploadImage: imageViewId: "+imageViewId);
        if(mImageUri!=null){
            //processing dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //get image extension
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mStorageTaskUpload = fileReference.putFile(mImageUri);

            //get image url
            //Task<Uri> urlTask =
            mStorageTaskUpload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    Log.d(TAG, "then: started and ended.");
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: imageViewId: "+imageViewId);
                        String docKey="initialDocKey";
                        switch (imageViewId){
                            case 0:
                                docKey = "ApplicationForm";
                                break;
                            case 1:
                                docKey = "Paystub";
                                break;
                            case 2:
                                docKey = "BankStatement";
                                break;
                            case 3:
                                docKey = "Id";
                                break;
                            default:
                                Toast.makeText(RentMyApplicationActivity.this, "Invalid imageViewId", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        Uri downloadUri = task.getResult();
                        Log.d(TAG, "onComplete: docKey: "+docKey);
                        Log.d(TAG, "onComplete: docUri: "+downloadUri.toString());

                        Map<String, Object> docMyApplication = new HashMap<>();
                        docMyApplication.put(docKey, downloadUri.toString());

                        //save to Firestore
                        mFirestore.collection("myApplication").document(mAuth.getCurrentUser().getUid())
                                .update(docMyApplication)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RentMyApplicationActivity.this, "Upload Successful.", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onSuccess: Upload to firestore success.");
                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RentMyApplicationActivity.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure: Upload to firestore failed.");
                            }
                        });
                    }
                }
            });
        }

        Log.d(TAG, "uploadImage: ended.");
    }

    private String getFileExtension(Uri uri) {
        Log.d(TAG, "getFileExtension: started.");
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Log.d(TAG, "getFileExtension: uri: "+mime.getExtensionFromMimeType(cR.getType(uri)));
        Log.d(TAG, "getFileExtension: ended.");
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
