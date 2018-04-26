package life.plank.juna.zone.view.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.AppConstants;

public class SignupPageActivity extends AppCompatActivity {

    @BindView(R.id.username_text_input_layout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.password_input_layout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.email_input_layout)
    TextInputLayout emailInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        ButterKnife.bind(this);
        AppConstants.setCollapsedHintMiddle(usernameInputLayout,this);
        AppConstants.setCollapsedHintMiddle(emailInputLayout,this);
        AppConstants.setCollapsedHintMiddle(passwordInputLayout,this);
    }
}