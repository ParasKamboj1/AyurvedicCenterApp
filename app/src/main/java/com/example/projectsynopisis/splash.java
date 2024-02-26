package com.example.projectsynopisis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class splash extends AppCompatActivity {
    ImageView splash1,splash2,splash3;
    TextView textView,textView1;
    Animation top,bottom,right,left;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash1 = findViewById(R.id.splash1);
        splash2 = findViewById(R.id.splash2);
        splash3 = findViewById(R.id.splash3);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView2);

        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        right = AnimationUtils.loadAnimation(this,R.anim.right_animation);
        left = AnimationUtils.loadAnimation(this,R.anim.left_animation);

        splash1.setAnimation(top);
        splash2.setAnimation(top);
        splash3.setAnimation(right);
        textView.setAnimation(bottom);
        textView1.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}