package com.example.projectsynopisis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signin extends AppCompatActivity {
    TextView loginButton;
    Button signinButton;
    EditText username,email,password,reenterpassword,mobilenumber;
    ToggleButton toggle1,toggle2;
    String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        loginButton = findViewById(R.id.loginbuttonsignin);
        username = findViewById(R.id.username);
        email = findViewById(R.id.emailsignin);
        password = findViewById(R.id.passwordsignin);
        reenterpassword = findViewById(R.id.reenterpasswordsignin);
        mobilenumber = findViewById(R.id.phonenumbersignin);
        signinButton = findViewById(R.id.signinbutton);
        toggle1 = findViewById(R.id.toggle1);
        toggle2 = findViewById(R.id.toggle2);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        reenterpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        toggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(null);
                }
                else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        toggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    reenterpassword.setTransformationMethod(null);
                }
                else{
                    reenterpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signin.this,login.class);
                startActivity(intent);
                finish();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = username.getText().toString();
                String EmailSignIn = email.getText().toString();
                String passwordSignIn = password.getText().toString();
                String ReEnterPassword = reenterpassword.getText().toString();
                String MobileNumber = mobilenumber.getText().toString();

                if(TextUtils.isEmpty(UserName) || TextUtils.isEmpty(EmailSignIn) || TextUtils.isEmpty(passwordSignIn) || TextUtils.isEmpty(ReEnterPassword) || TextUtils.isEmpty(MobileNumber)){
                    Toast.makeText(signin.this, "You must have to fill every field.", Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(EmailSignIn).matches()){
                    email.setError("Give valid email");
                    Toast.makeText(signin.this, "Give Valid Email!", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordSignIn.equals(ReEnterPassword)){
                    reenterpassword.setError("Password does not match");
                    Toast.makeText(signin.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                }
                else if(passwordSignIn.length()<6){
                    password.setError("Atleast 6 characters!");
                    Toast.makeText(signin.this, "Password must be atleast 6 character long", Toast.LENGTH_SHORT).show();
                }
                else if(MobileNumber.length()!=10){
                    mobilenumber.setError("Give valid number");
                    Toast.makeText(signin.this, "Give valid mobile number!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(EmailSignIn,passwordSignIn).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String UserId = auth.getCurrentUser().getUid();

                                Users users = new Users(UserName,EmailSignIn,passwordSignIn,MobileNumber);

                                Log.d("Debug", "UserName: " + UserName);
                                Log.d("Debug", "EmailSignIn: " + EmailSignIn);
                                Log.d("Debug", "passwordSignIn: " + passwordSignIn);
                                Log.d("Debug", "MobileNumber: " + MobileNumber);
                                Log.d("Debug", "UserId: " + UserId);

                                reference = database.getReference("users");
                                reference.child(UserId).setValue(users);


                                Intent intent = new Intent(signin.this, login.class);
                                startActivity(intent);
                                Toast.makeText(signin.this, "Registeration Successfull!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(signin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });

    }
}