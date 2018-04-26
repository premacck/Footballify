package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.forgot_password)
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_password:
                Intent intent = new Intent(SignInActivity.this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
