package com.pythonanywhere.frutsh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import Classes.DownloadWebContent;
import Classes.ImageDownloader;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    final Fragment fragment1 = new AppleFragment();
    final Fragment fragment2 = new BananaFragment();
    final Fragment fragment3 = new OrangeFragment();

    public static HashMap<String, String> hashMap;
    public static Bitmap[] bitmaps;

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

        downloadWebContent();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    public void logout() {
        mAuth.signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void downloadWebContent() {
        DownloadWebContent task = new DownloadWebContent();
        try {
            hashMap = task.execute("https://frutsh.pythonanywhere.com").get();
            downloadImage(hashMap);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void downloadImage(HashMap<String, String> hashMap) {

        String url1 = "https://frutsh.pythonanywhere.com/" + hashMap.get("a_url");
        String url2 = "https://frutsh.pythonanywhere.com/" + hashMap.get("b_url");
        String url3 = "https://frutsh.pythonanywhere.com/" + hashMap.get("o_url");

        ImageDownloader task = new ImageDownloader();
        try {
            bitmaps = task.execute(url1, url2, url3).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}