package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cribapp.General.ContactUsActivity;
import com.example.cribapp.General.PrivacyPolicyActivity;
import com.example.cribapp.General.TermsOfUseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RentAccountSettingsActivity extends AppCompatActivity {
    public static final String FRAGMENT_ID = "fragmentId";
    private static final String TAG = "RentAccountSettings";

    //firebase
    FirebaseAuth mAuth;

    //buttons
    private ImageView mBack;
    private Button mChangePassword;
    private Button mAbout;
    private Button mTermsOfUse;
    private Button mPrivacyPolicy;
    private Button mContactUs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_account_settings);

        mAuth = FirebaseAuth.getInstance();

        mBack = findViewById(R.id.icon_back);
        mChangePassword = findViewById(R.id.button_change_password);
        mAbout = findViewById(R.id.button_about);
        mTermsOfUse = findViewById(R.id.button_terms_of_use);
        mPrivacyPolicy = findViewById(R.id.button_privacy_policy);
        mContactUs = findViewById(R.id.button_contact_us);

        initButtons();

    }

    private void initButtons() {
        Intent intent = getIntent();
        final String fragmentId = intent.getStringExtra(FRAGMENT_ID);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentAccountSettingsActivity.this, RentMainActivity.class);
                intent.putExtra(RentMainActivity.FRAGMENT_ID, fragmentId);
                startActivity(intent);
            }
        });

        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });

        mTermsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentAccountSettingsActivity.this, TermsOfUseActivity.class));
            }
        });

        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentAccountSettingsActivity.this, PrivacyPolicyActivity.class));
            }
        });

        mContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RentAccountSettingsActivity.this, ContactUsActivity.class));
            }
        });
    }

    public void showChangePasswordDialog(){

        final EditText mChangePasswordEditText = new EditText(RentAccountSettingsActivity.this);
        final AlertDialog.Builder mChangePasswordDialog = new AlertDialog.Builder(RentAccountSettingsActivity.this);

        mChangePasswordDialog.setTitle("Change Password");
        mChangePasswordDialog.setMessage("Please enter new password: ");
        mChangePasswordDialog.setView(mChangePasswordEditText);

        mChangePasswordDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //pull up email and send reset link
                String newPassword = mChangePasswordEditText.getText().toString();
                FirebaseUser user = mAuth.getCurrentUser();

                if(newPassword!=null){
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RentAccountSettingsActivity.this, "Password Updated.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RentAccountSettingsActivity.this, "Password Update Failed.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: showChangePasswordDialog: "+e.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(RentAccountSettingsActivity.this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mChangePasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing, dialog dissapears
            }
        });

        mChangePasswordDialog.create().show();
    }

    public void showAboutDialog(){
        AlertDialog.Builder mAboutDialog = new AlertDialog.Builder(this);
        mAboutDialog.setTitle("About crib");
        //can't justify dialog-textview, since there is no direct access to it
        mAboutDialog.setMessage("Title: Crib Real Estate\n"
                +"Version: 1.0.3\n\n"
                + "Crib helps you buy or sell homes, combining tecnology and a customer-service focus to make whole process easier and less expensive."
                + "Our real estate agents earn customer-satisfaction bonuses, not commissions.");
        mAboutDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close
            }
        });
        mAboutDialog.create().show();
    }
}