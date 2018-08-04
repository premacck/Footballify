package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.RegistrationRequest;
import net.openid.appauth.ResponseTypeValues;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.helper.StackAnimation;
import retrofit2.Retrofit;

public class SignInActivity extends AppCompatActivity {
    String TAG = SignInActivity.class.getCanonicalName();
    @Inject
    @Named("default")
    Retrofit retrofit;
    @Nullable
    @BindView(R.id.login)
    ImageView login;
    @BindView(R.id.email_edit_text)
    EditText emailEditText;
    @BindView(R.id.password_edit_text)
    EditText passwordEditText;

    StackAnimation stackAnimation;

    private AuthorizationService mAuthService;

    TextWatcher loginFieldsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //TODO: validate email
            if (!emailEditText.getText().toString().trim().isEmpty() && !passwordEditText.getText().toString().trim().isEmpty()) {
                login.setVisibility(View.VISIBLE);
            } else {
                login.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ((ZoneApplication) getApplication()).getSignInUserNetworkComponent().inject(this);

        ButterKnife.bind(this);
        initStackAnimation();

        emailEditText.addTextChangedListener(loginFieldsWatcher);
        passwordEditText.addTextChangedListener(loginFieldsWatcher);

        if (getResources().getBoolean(R.bool.is_dev_environment)) {
            emailEditText.setText(getString(R.string.azure_login_username));
            loginViaAzureAdb2c();
        }
    }

    private void initStackAnimation() {
        stackAnimation = new StackAnimation(AppConstants.ANIMATION_DURATION,
                AppConstants.ANIMATION_START_SCALE,
                AppConstants.ANIMATION_PIVOT_VALUE);
    }

    private void makeRegistrationRequest(
            @NonNull AuthorizationServiceConfiguration serviceConfig,
            @NonNull final IdentityProvider idp) {

        final RegistrationRequest registrationRequest = new RegistrationRequest.Builder(
                serviceConfig,
                Arrays.asList(idp.getRedirectUri()))
                .setTokenEndpointAuthenticationMethod(ClientSecretBasic.NAME)
                .build();

        Log.d(TAG, "Making registration request to " + serviceConfig.registrationEndpoint);
        mAuthService.performRegistrationRequest(
                registrationRequest,
                (registrationResponse, ex) -> {
                    Log.d(TAG, "Registration request complete");
                    if (registrationResponse != null) {
                        idp.setClientId(registrationResponse.clientId);
                        Log.d(TAG, "Registration request complete successfully");
                        // Continue with the authentication
                        makeAuthRequest(registrationResponse.request.configuration, idp,
                                new AuthState((registrationResponse)), emailEditText.getText().toString());
                    }
                });
    }

    private void makeAuthRequest(
            @NonNull AuthorizationServiceConfiguration serviceConfig,
            @NonNull IdentityProvider idp,
            @NonNull AuthState authState, String emailId) {

        AuthorizationRequest authRequest = new AuthorizationRequest.Builder(
                serviceConfig,
                idp.getClientId(),
                ResponseTypeValues.CODE,
                idp.getRedirectUri())
                .setScope(idp.getScope())
                .setLoginHint(emailId)
                .build();

        Log.d(TAG, "Making auth request to " + serviceConfig.authorizationEndpoint);
        mAuthService.performAuthorizationRequest(
                authRequest,
                TokenActivity.createPostAuthorizationIntent(
                        this,
                        authRequest,
                        serviceConfig.discoveryDoc,
                        authState, emailEditText.getText().toString()),
                mAuthService.createCustomTabsIntentBuilder()
                        .setToolbarColor(getResources().getColor(R.color.colorAccent))
                        .build());
    }


    @OnClick({R.id.login, R.id.forgot_password_text_view, R.id.sign_up_card, R.id.skip_login})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.login:
                loginViaAzureAdb2c();

                break;
            case R.id.forgot_password_text_view:
                Intent intent = new Intent(this, AuthForgotPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.sign_up_card:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }

    private void loginViaAzureAdb2c() {
        mAuthService = new AuthorizationService(this);
        List<IdentityProvider> providers = IdentityProvider.getEnabledProviders(this);

        for (final IdentityProvider idp : providers) {
            final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                    (serviceConfiguration, ex) -> {
                        if (ex != null) {
                            Log.w(TAG, "Failed to retrieve configuration for " + idp.name, ex);
                        } else {
                            Log.d(TAG, "configuration retrieved for " + idp.name
                                    + ", proceeding");
                            if (idp.getClientId() == null) {
                                // Do dynamic client registration if no client_id
                                makeRegistrationRequest(serviceConfiguration, idp);
                            } else {
                                makeAuthRequest(serviceConfiguration, idp, new AuthState(), emailEditText.getText().toString());
                            }
                        }
                    };
            idp.retrieveConfig(this, retrieveCallback);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
