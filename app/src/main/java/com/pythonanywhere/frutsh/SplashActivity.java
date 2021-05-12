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

    static ArrayList<String> imageURLs = new ArrayList<>();
    static ArrayList<String> fruitName = new ArrayList<>();
    static ArrayList<String> condition = new ArrayList<>();
    static ArrayList<String> time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        logoImageView = findViewById(R.id.logo_image);

        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);

        logoImageView.setAnimation(logo_anim);

        downloadWebContent();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser() != null){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        },5000);

    }

    public void downloadWebContent(){
        DownloadWebContent task = new DownloadWebContent();
        String result;

        try {
            result = task.execute("https://frutsh.pythonanywhere.com").get();

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(result);

            while (m.find()){
                imageURLs.add(m.group(1));
            }

            p = Pattern.compile("<h5 class=\"card-title\">(.*?)</h5>");
            m = p.matcher(result);

            while (m.find()){
                fruitName.add(m.group(1));
            }

            p = Pattern.compile("<p class=\"card-text\" id=\"a_condition\">(.*?)</p>");
            m = p.matcher(result);

            while (m.find()){
                condition.add(0, m.group(1));
            }

            p = Pattern.compile("<p class=\"card-text\" id=\"p_condition\">(.*?)</p>");
            m = p.matcher(result);

            while (m.find()){
                condition.add(1, m.group(1));
            }

            p = Pattern.compile("<p class=\"card-text\" id=\"o_condition\">(.*?)</p>");
            m = p.matcher(result);

            while (m.find()){
                condition.add(2, m.group(1));
            }

            p = Pattern.compile("<small class=\"text-muted\">(.*?)</small></p>");
            m = p.matcher(result);

            while (m.find()){
                time.add(m.group(1));
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}