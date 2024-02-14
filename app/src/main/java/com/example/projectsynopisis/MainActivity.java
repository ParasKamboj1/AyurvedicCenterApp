package com.example.projectsynopisis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ImageView googlemap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        googlemap = findViewById(R.id.googlemap);

        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this,login.class);
            startActivity(intent);
            finish();
        }

        googlemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri location = Uri.parse("geo:0,0?q=Nearest+Ayurvedic+hospitals");
                Intent intent = new Intent(Intent.ACTION_VIEW,location);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }
}