package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.ActivityUtil;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.submit_button)
    ImageView submitImageview;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        ActivityUtil.setCollapsedHintMiddle(usernameInputLayout,this);
        ActivityUtil.setCollapsedHintMiddle(passwordInputLayout,this);
    }

    @OnClick({R.id.forgot_password,R.id.submit_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_password:
                Intent intent = new Intent(SignInActivity.this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.submit_button:
                Intent intentSubmit = new Intent(SignInActivity.this, SwipePageActivity.class);
                startActivity(intentSubmit);
                break;
        }
    }
}
