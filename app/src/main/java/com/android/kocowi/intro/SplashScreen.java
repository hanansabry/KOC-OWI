package com.android.kocowi.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.kocowi.R;
import com.android.kocowi.login.LoginScreen;
import com.android.kocowi.production_operation.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-ServiceListActivity. */
                Intent mainIntent;
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    mainIntent = new Intent(SplashScreen.this, LoginScreen.class);
                } else {
                    mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                }
                startActivity(mainIntent);

                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
