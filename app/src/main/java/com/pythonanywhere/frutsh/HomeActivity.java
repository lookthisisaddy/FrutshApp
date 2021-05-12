package com.pythonanywhere.frutsh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Classes.DownloadWebContent;
import Classes.ImageDownloader;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    final Fragment fragment1 = new AppleFragment();
    final Fragment fragment2 = new BananaFragment();
    final Fragment fragment3 = new OrangeFragment();

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AppleFragment()).commit();
        }

        downloadImage();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.apple:
                        selectedFragment = fragment1;
                        break;
                    case R.id.banana:
                        selectedFragment = fragment2;
                        break;
                    case R.id.orange:
                        selectedFragment = fragment3;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };


    public void downloadImage(){

        ImageDownloader task = new ImageDownloader();
        try {
            String url1 = "https://frutsh.pythonanywhere.com/" + SplashActivity.imageURLs.get(0);
            String url2 = "https://frutsh.pythonanywhere.com/" + SplashActivity.imageURLs.get(1);
            String url3 = "https://frutsh.pythonanywhere.com/" + SplashActivity.imageURLs.get(2);
            String[] urls = {url1,url2,url3};

            Bitmap[] bitmaps = task.execute(urls).get();

            for (int i = 0; i < bitmaps.length; i++){
                saveImages(bitmaps[i], i);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveImages(Bitmap bitmap, int number){

        ContextWrapper contextWrapper = new ContextWrapper(getBaseContext());
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);

        File file = new File(directory, "image" + number + ".jpg");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.d("HomeActivity", "image" + number + " saved");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity.this , LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}