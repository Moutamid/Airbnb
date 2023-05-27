package com.moutamid.airbnb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.moutamid.airbnb.activities.LoginActivity;
import com.moutamid.airbnb.activities.SignupActivity;
import com.moutamid.airbnb.constant.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LinearProgressIndicator progres = findViewById(R.id.progress);
        final int[] prog = {1};

        CountDownTimer countDownTimer = new CountDownTimer(2000, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                prog[0]++;
                progres.setProgress(prog[0]);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

        new Handler().postDelayed(() -> {
            if (Constants.auth().getCurrentUser() != null){
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);

    }
}