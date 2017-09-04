package life.plank.juna.zone.presentation.activity;

import android.content.Intent;
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
    }

    @OnClick(R.id.text_signin)
    public void signInClicked() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.text_signup)
    public void signUpClicked() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
