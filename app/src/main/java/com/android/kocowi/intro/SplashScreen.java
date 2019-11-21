package com.android.kocowi.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.kocowi.R;
import com.android.kocowi.backend.users.UsersRepository;
import com.android.kocowi.backend.users.UsersRepositoryImpl;
import com.android.kocowi.login.LoginScreen;
import com.android.kocowi.model.User;
import com.android.kocowi.operator.WellsLocationGoogleMap;
import com.android.kocowi.production_operation.gc.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent mainIntent = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(mainIntent);
                } else {
                    UsersRepository usersRepository = new UsersRepositoryImpl();
                    usersRepository.getCurrentUserData(new UsersRepository.UsersRetrievingCallback() {
                        @Override
                        public void onUserRetrievedSuccessfully(User user) {
                            Intent mainIntent = null;
                            if (user.getRole() == User.UserRole.PRODUCTION_OPERATION) {
                                mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            } else {
                                mainIntent = new Intent(SplashScreen.this, WellsLocationGoogleMap.class);
                            }
                            startActivity(mainIntent);
                            finish();
                        }

                        @Override
                        public void onUserRetrievedFailed(String err) {
                            Toast.makeText(SplashScreen.this, err, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
