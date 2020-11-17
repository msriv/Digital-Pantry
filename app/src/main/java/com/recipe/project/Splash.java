package com.recipe.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        background = (ImageView) findViewById(R.id.s_img);


        Glide.with(this)
                .load(R.drawable.splash)
                .into(background);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                finish();
                startActivity(new Intent(Splash.this, Home.class));
            }
        }, 3000);
    }


}
