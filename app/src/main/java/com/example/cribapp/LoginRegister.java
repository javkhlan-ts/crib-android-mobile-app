package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegister extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView mSignIn;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private TextView mShowPassword;
    private Button mRegisterButton;

    private FirebaseAuth mAuth;
    private Boolean mHidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        mBackButton = findViewById(R.id.imageViewIconBack);
        mSignIn = findViewById(R.id.textViewSignIn);
        mEmail = findViewById(R.id.editTextEmailAddress);
        mPassword = findViewById(R.id.editTextPassword);
        mShowPassword = findViewById(R.id.textViewShow);
        mConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        mRegisterButton = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();

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
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String confirmPassword = mConfirmPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
                    if(password.equals(confirmPassword)){
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginRegister.this, "Account registered", Toast.LENGTH_LONG).show();
                                    gotoMainActivity_Rent();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginRegister.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(LoginRegister.this, "Password does not match", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(LoginRegister.this, "Fill all fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void gotoMainActivity_Rent(){
        Intent intent = new Intent(this, MainActivity_Rent.class);
        startActivity(intent);
    }
}