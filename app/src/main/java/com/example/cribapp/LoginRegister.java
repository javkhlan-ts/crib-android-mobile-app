package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginRegister extends AppCompatActivity {

    private static final String TAG = "LoginRegister";
    private ImageView mBackButton;
    private TextView mSignIn;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private TextView mShowPassword;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private Boolean mHidePassword = true;
    //private FirebaseFirestore mFirestore;
    //private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        mBackButton = findViewById(R.id.imageViewIconBack);
        mSignIn = findViewById(R.id.textViewSignIn);
        mEmail = findViewById(R.id.editTextEmail);
        mPassword = findViewById(R.id.editTextPassword);
        mShowPassword = findViewById(R.id.textViewShow);
        mConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        mRegisterButton = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        //mFirestore = FirebaseFirestore.getInstance();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegister.this, LoginLogin.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegister.this, LoginLogin.class));
            }
        });

        mShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mHidePassword){
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mHidePassword = false;
                }
                else{
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mHidePassword = true;
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mConfirmPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
                    if(password.equals(confirmPassword))
                    {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginRegister.this, "User created", Toast.LENGTH_LONG).show();
                                    //before go to the main page, user must be saved to the Firestore db
                                    //userId = mAuth.getCurrentUser().getUid(); //this might be the problem???
                                    //saveUserToFirestore(userId);
                                    gotoMainActivity_Rent();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginRegister.this, "Registration failed", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onFailure: "+e.getMessage());
                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginRegister.this, "Password does not match", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(LoginRegister.this, "Fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    private void saveUserToFirestore(final String userId) {
//        DocumentReference documentReference = mFirestore.collection("Users").document(userId);
//        Map<String, Object> user = new HashMap<>();
//        user.put("email", mEmail);
//        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "onSuccess: successfully saved to Firestore. userId: "+userId);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "onFailure: "+ e.toString());
//            }
//        });
//    }

    public void gotoMainActivity_Rent(){
        Intent intent = new Intent(this, MainActivity_Rent.class);
        startActivity(intent);
    }
}