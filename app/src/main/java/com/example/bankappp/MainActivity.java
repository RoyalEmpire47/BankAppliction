package com.example.bankappp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GifImageView gifImageView=findViewById(R.id.image9);
        gifImageView.setImageResource(R.drawable.bankgif);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {

                startActivity(new Intent(getApplicationContext(), LoginAcitviy.class));
                finish();
            }
        }, 4000);
    }
}