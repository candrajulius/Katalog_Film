package com.dev_candra.moviedb.actvitites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dev_candra.moviedb.R;

public class SplashScreen extends AppCompatActivity {

    Animation bottomAnimation,topAnimation;
    ImageView gambar;
    TextView textBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        gambar = findViewById(R.id.gambarBanner1);
        textBanner = findViewById(R.id.textBanner1);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        gambar.setAnimation(topAnimation);
        textBanner.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
            }
        },5000);

    }
}
