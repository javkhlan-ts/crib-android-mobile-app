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
    private Button mRegisterButton;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mShowPassword;
    private FirebaseAuth mAuth;
    private Boolean mHidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register);

        mBackButton = findViewById(R.id.imageViewIconBack);
        mRegisterButton = findViewById(R.id.buttonRegister);
        mUsername = findViewById(R.id.editTextUsername);
        mEmail = findViewById(R.id.editTextEmailAddress);
        mPassword = findViewById(R.id.editTextPassword);
        mShowPassword = findViewById(R.id.textViewShow);
        mBackButton = findViewById(R.id.imageViewIconBack);
        mAuth = FirebaseAuth.getInstance();

        //Toast.makeText(LoginRegister.this, "checkpoint", Toast.LENGTH_LONG).show();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!username.isEmpty() && !email.isEmpty() && !password.isEmpty()){
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
                    Toast.makeText(LoginRegister.this, "Fill all fields", Toast.LENGTH_LONG).show();
                }
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

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginRegister.this, LoginRegisterOrLogin.class));
            }
        });
    }

    public void gotoRegisterOrLogin(View view){
        startActivity(new Intent(LoginRegister.this, LoginLogin.class));
    }

    public void gotoMainActivity_Rent(){
        Intent intent = new Intent(this, MainActivity_Rent.class);
        startActivity(intent);
    }
}