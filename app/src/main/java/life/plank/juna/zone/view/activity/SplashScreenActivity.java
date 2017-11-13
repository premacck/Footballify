package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import life.plank.juna.zone.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private SharedPreferences loginPreferences;
    private Boolean savedLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Orange));
        }

        loginPreferences = getSharedPreferences(getString(R.string.login_pref), MODE_PRIVATE);
        savedLogin = loginPreferences.getBoolean(getString(R.string.shared_pref_save_login), false);

        new Handler().postDelayed(() -> {
            if (savedLogin) {
                startActivity(new Intent(SplashScreenActivity.this, ZoneHomeActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, SocialLoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
