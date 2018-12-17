package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import life.plank.juna.zone.R;

public class AuthForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_forgot_password);

        findViewById(R.id.forgot_password_image_view).setOnClickListener(view -> forgotPasswordAlertDialog());
    }

    private void forgotPasswordAlertDialog() {
        new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.auth_alert_dialog)
                .bindClickListener(v -> {
                    Intent intent = new Intent(AuthForgotPasswordActivity.this, SignInActivity.class);
                    startActivity(intent);
                }, R.id.text_navigate_to_login)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.02f)
                .setBlurRadius(8)
                .setAnimationDuration(100)
                .setTintColor(0x30000000)
                .build()
                .show();
    }
}