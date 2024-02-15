package com.example.projectsynopisis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    TextView signup,forgotPassword;
    EditText email,password;
    ImageView login;
    ToggleButton showpassword;
    FirebaseAuth auth;
    String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        showpassword = findViewById(R.id.showpassword);

        auth = FirebaseAuth.getInstance();

        forgotPassword = findViewById(R.id.forgotpassword);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });


        showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(login.this, "Enter Email!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Password)){
                    Toast.makeText(login.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                }
//                else if(!Patterns.EMAIL_ADDRESS.matcher(emailPattern).matches()){
//                    email.setError("Give Proper Email!");
//                    Toast.makeText(login.this, "Give Proper Email!", Toast.LENGTH_SHORT).show();
//                }
                else if(Password.length()<6){
                    password.setError("Atleast of 6 characters!");
                    Toast.makeText(login.this, "Atleast of 6 characters!", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                if(user!=null){
                                    String userId = user.getUid();
                                    fetchUsername(userId);
                                }
                            }
                            else{
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,signin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void fetchUsername(String userId){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String username = snapshot.child("username").getValue(String.class);
                    if(username!=null && !username.isEmpty()) {
                        Intent inten = new Intent(login.this, MainActivity.class);
                        inten.putExtra("USERNAME", username);
                        startActivity(inten);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Failed to read username!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}