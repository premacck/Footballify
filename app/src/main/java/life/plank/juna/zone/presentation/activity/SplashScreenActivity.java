package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import life.plank.juna.zone.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen_activity);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, SocialLoginActivity.class));
            finish();
        }, SPLASH_TIME_OUT);
    }
}
