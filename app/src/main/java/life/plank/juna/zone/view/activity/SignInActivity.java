package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.openid.appauth.AuthorizationService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.AuthUtil;

import static life.plank.juna.zone.util.DataUtil.isValidEmail;
import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefs;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.login)
    ImageView login;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    private AuthorizationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        authService = new AuthorizationService(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        ButterKnife.bind(this);
        getSharedPrefs(getString(R.string.pref_login_credentails))
                .edit()
                .putBoolean(getString(R.string.pref_is_logged_in), false)
                .apply();

        if (getResources().getBoolean(R.bool.is_dev_environment)) {
            emailEditText.setText(getString(R.string.azure_login_username));
            AuthUtil.loginOrRefreshToken(this, authService, null, false);
        }
    }

    @OnTextChanged({R.id.email_edit_text, R.id.password_edit_text})
    public void validateFields(CharSequence charSequence) {
        login.setVisibility(isValidEmail(charSequence) ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick({R.id.login, R.id.forgot_password_text_view, R.id.sign_up_card, R.id.skip_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                AuthUtil.loginOrRefreshToken(this, authService, null, false);
                break;
            case R.id.forgot_password_text_view:
                Intent intent = new Intent(this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_up_card:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.skip_login:
                SharedPreferences.Editor prefEditor = getSharedPrefs(getString(R.string.pref_login_credentails)).edit();
                prefEditor.putBoolean(getString(R.string.pref_is_logged_in), false).apply();
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                break;

        }
    }

    @Override
    protected void onDestroy() {
        if (authService != null) {
            authService.dispose();
        }
        super.onDestroy();
    }
}