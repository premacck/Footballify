package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class AuthForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.forgot_password_image_view)
    ImageView forgotPasswordImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_forgot_password);
        ButterKnife.bind(this);
        forgotPasswordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
    }

    private void alertDialog() {
        new BlurPopupWindow.Builder(this)
                .setContentView(R.layout.auth_alert_dialog)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                        startActivity(intent);
                    }
                }, R.id.text_navigate_to_login)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.02f)
                .setBlurRadius(8)
                .setTintColor(0x30000000)
                .build()
                .show();
    }
}