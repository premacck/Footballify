package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.HttpURLConnection;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.domain.service.AuthenticationService;
import life.plank.juna.zone.util.AuthenticationDialog;
import life.plank.juna.zone.util.AuthenticationListener;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.viewmodel.SocialLoginViewModel;
import retrofit2.Retrofit;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocialLoginActivity extends AppCompatActivity implements AuthenticationListener {

    @Inject
    @Named("default")
    Retrofit retrofit;

    @Inject
    @Named("instagram")
    Retrofit instagramRetrofit;

    @BindView(R.id.relative_layout_social_login)
    RelativeLayout relativeLayout;

    private static final String TAG = SocialLoginActivity.class.getSimpleName();
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private Subscription subscription;
    private SocialLoginViewModel socialLoginViewModel;
    private CallbackManager callbackManager;
    private AuthenticationDialog authenticationDialog;
    private AVLoadingIndicatorView spinner;
    private TwitterAuthClient twitterAuthClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.layout_social_login_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(twitterConfig);

        spinner = (AVLoadingIndicatorView) findViewById(R.id.social_signup_spinner);

        ((ZoneApplication) getApplication()).getSocialLoginNetworkComponent().inject(this);
        socialLoginViewModel = new SocialLoginViewModel(this, new AuthenticationService(retrofit, instagramRetrofit));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
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

    @OnClick(R.id.instagram_login_button)
    public void startInstagramLogin() {
        //Todo: Add progress dialog - Juna-1065
        authenticationDialog = new AuthenticationDialog(SocialLoginActivity.this, SocialLoginActivity.this);
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();
    }

    @OnClick(R.id.facebook_login_button)
    public void startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "In onSuccess");
                        if (loginResult.getRecentlyGrantedPermissions() != null)
                            registerUser(loginResult.getAccessToken().getUserId(),
                                    Profile.getCurrentProfile().getName(),
                                    getString(R.string.facebook_string));
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "In onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        if (error instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        } else {
                            Log.d(TAG, "In onError: " + error.getMessage());
                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.facebook_login_failed));
                        }
                    }
                });
    }

    @OnClick(R.id.twitter_login_button)
    public void startTwitterLogin() {
        twitterAuthClient = new TwitterAuthClient();
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterResponse) {
                Log.d(TAG, "In onSuccess");
                if (twitterResponse.data.getAuthToken() != null) {
                    showProgressSpinner();
                    registerUser(twitterResponse.data.getUserName(),
                            twitterResponse.data.getUserName(),
                            getString(R.string.twitter_string));
                }
            }

            @Override
            public void failure(TwitterException e) {
                UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.twitter_login_failed));
                Log.d(TAG, "In onError: " + e.getMessage());
            }
        });
    }

    @OnClick(R.id.google_login_button)
    public void startGoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressSpinner();
    }

    private void registerUser(String userName, String displayName, String provider) {
        subscription = socialLoginViewModel.registerUser(userName, displayName, provider)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    socialLoginViewModel.saveLoginDetails(userName, userName);
                    //Todo: Change 422 response code to 409 when backend issue - JUNA-921 is fixed
                    if (response.code() == HttpsURLConnection.HTTP_CREATED || response.code() == getResources().getInteger(R.integer.unprocessable_entity_response_code) || response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        subscription = socialLoginViewModel.loginUser(userName)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(voidResponse -> {
                                    if (voidResponse.code() == HttpsURLConnection.HTTP_OK || voidResponse.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                        startZoneHomeActivity();
                                        hideProgressSpinner();
                                    } else {
                                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.login_failed_message));
                                        hideProgressSpinner();
                                        Log.d(TAG, String.valueOf(voidResponse.code()));
                                    }
                                }, throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
                    } else {
                        hideProgressSpinner();
                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.login_failed_message));
                        Log.d(TAG, String.valueOf(response.code()));
                    }
                }, throwable -> Log.d(TAG, "In onError: " + throwable.getMessage()));
    }

    @Override
    public void onCodeReceived(String authToken) {
        if (authToken == null) {
            authenticationDialog.dismiss();
        }

        subscription = socialLoginViewModel.getInstagramLoginData(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(instagramResponse -> {
                    if (instagramResponse.getMeta().getCode() == HttpURLConnection.HTTP_OK) {
                        socialLoginViewModel.saveLoginDetails(instagramResponse.getData().getUsername(), instagramResponse.getData().getUsername());
                        registerUser(instagramResponse.getData().getUsername(),
                                instagramResponse.getData().getFullName(),
                                getString(R.string.instagram_string));
                    } else
                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed));
                }, throwable -> UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed)));
    }

    @Override
    public void showProgressSpinner() {
        spinner.show();
    }

    public void hideProgressSpinner() {
        spinner.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, account.getDisplayName());
            if (account.getAccount() != null) {
                registerUser(account.getEmail(),
                        account.getDisplayName(),
                        getString(R.string.google_string));
            }
        } catch (ApiException e) {
            Log.d(TAG, "Google signin failed:" + e.getStatusCode());
        }
    }

    public void startZoneHomeActivity() {
        startActivity(new Intent(SocialLoginActivity.this, ZoneHomeActivity.class));
    }
}
