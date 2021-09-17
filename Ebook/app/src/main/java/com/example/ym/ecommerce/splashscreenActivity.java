package com.example.ym.ecommerce;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class splashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



//        getSupportActionBar().hide();

        Thread background = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(5000);

                    Intent intent = new Intent(splashscreenActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();

    }
}
