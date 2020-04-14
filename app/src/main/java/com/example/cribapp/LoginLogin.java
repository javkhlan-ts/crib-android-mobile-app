package com.example.cribapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginLogin extends AppCompatActivity {

    private ImageView mBackButton;
    private TextView mSignUp;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mShowPassword;
    private TextView mForgotPassword;
    private CheckBox mRememberMe;
    private Button mLogin;

    private FirebaseAuth mAuth;
    private Boolean mHidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_login);

        mBackButton = findViewById(R.id.imageViewIconBack);
        mSignUp = findViewById(R.id.textViewSignUp);
        mEmail = findViewById(R.id.editTextEmailAddress);
        mPassword = findViewById(R.id.editTextPassword);
        mShowPassword = findViewById(R.id.textViewShow);
        mRememberMe = findViewById(R.id.checkBoxRememberMe);
        mForgotPassword = findViewById(R.id.textViewForgotPassword);
        mLogin = findViewById(R.id.buttonLogin);

        mAuth = FirebaseAuth.getInstance();

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginLogin.this, LoginBuyRentLandlord.class));
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginLogin.this, LoginRegister.class));
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

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginLogin.this, LoginForgotPassword.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginLogin.this, "Login successful", Toast.LENGTH_LONG).show();
                                openMainActivity_Rent();
                            }
                            else{
                                Toast.makeText(LoginLogin.this, "Login error: "+task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LoginLogin.this, "Fill email and password to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openMainActivity_Rent(){
        startActivity(new Intent(this, MainActivity_Rent.class));
    }
}
