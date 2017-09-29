package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class SocialLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_social_login_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getColor(R.color.Green));
        }
    }

    @OnClick(R.id.text_signin)
    public void signInClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.text_signup)
    public void signUpClicked() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @OnClick( {R.id.button_facebook, R.id.button_google, R.id.button_instagram, R.id.button_twitter})
    public void zoneHomeActivity() {
        startActivity(new Intent(this, ZoneHomeActivity.class));
    }
}
