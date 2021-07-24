package com.pythonanywhere.frutsh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Classes.DownloadWebContent;

public class SplashActivity extends AppCompatActivity {

    ImageView logoImageView;

    Animation logo_anim;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        logoImageView = findViewById(R.id.logo_image);

        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);

        logoImageView.setAnimation(logo_anim);


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(mAuth.getCurrentUser() != null){
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },3000);

    }


}