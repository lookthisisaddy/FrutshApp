package com.pythonanywhere.frutsh;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

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
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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