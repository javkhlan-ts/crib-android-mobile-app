package com.example.cribapp.General;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cribapp.R;
import com.example.cribapp.RentAccountSettingsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ContactUsActivity extends AppCompatActivity {
    private static final String TAG = "ContactUsActivity";

    private ImageView mBack;
    private EditText mName;
    private TextView mEmail;
    private EditText mSubject;
    private EditText mMessage;
    private Button mSubmit;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        mBack = findViewById(R.id.icon_back);
        mName = findViewById(R.id.contact_name);
        mEmail = findViewById(R.id.contact_email);
        mSubject = findViewById(R.id.contact_subject);
        mMessage = findViewById(R.id.contact_message);
        mSubmit = findViewById(R.id.button_submit);

        mAuth = FirebaseAuth.getInstance();

        init();
    }

    private void init() {
        Log.d(TAG, "init: started.");

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactUsActivity.this, RentAccountSettingsActivity.class));
            }
        });

        mEmail.setText(mAuth.getCurrentUser().getEmail());

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String emailTo = "contact@crib.com";
                String subject = mSubject.getText().toString().trim();
                String message = mMessage.getText().toString().trim();

                if(validateEmptyString(name, emailTo, subject, message)){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto: contact@crib.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(intent);
                } else {
                    Toast.makeText(ContactUsActivity.this, "Problem on sending email.", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean validateEmptyString(String name, String email, String subject, String message) {
                boolean result = false;
                if(name!=null){
                    if(email!=null){
                        if(subject!=null){
                            if(message!=null){
                                result=true;
                            } else {
                                Toast.makeText(ContactUsActivity.this, "Please enter message to send.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ContactUsActivity.this, "Please enter subject.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContactUsActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContactUsActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                }
                return result;
            }
        });
    }
}
