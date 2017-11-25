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
import com.wang.avi.AVLoadingIndicatorView;

import java.net.HttpURLConnection;
import java.util.Arrays;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.instagram_model_class.InstagramResponse;
import life.plank.juna.zone.data.network.service.RestClient;
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
    Retrofit retrofit;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.layout_social_login_activity);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.Green));
        }
        spinner = (AVLoadingIndicatorView) findViewById(R.id.social_signup_spinner);

        ((ZoneApplication) getApplication()).getSocialLoginNetworkComponent().inject(this);
        socialLoginViewModel = new SocialLoginViewModel(this, new AuthenticationService(retrofit));
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

    @OnClick(R.id.button_instagram)
    public void startInstagramLogin() {
        //Todo: Add progress dialog - Juna-1065
        authenticationDialog = new AuthenticationDialog(SocialLoginActivity.this, SocialLoginActivity.this);
        authenticationDialog.setCancelable(true);
        authenticationDialog.show();
    }

    @OnClick(R.id.button_facebook)
    public void startFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL, PUBLIC_PROFILE));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "In onSuccess");
                        if (loginResult.getRecentlyGrantedPermissions() != null)
                            registerUser(loginResult);
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

    private void registerUser(LoginResult loginResult) {
        if (Profile.getCurrentProfile() != null) {
            subscription = socialLoginViewModel.registerFacebookUser(loginResult)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        //Todo: Change this response code to 409 when backend issue - JUNA-921 is fixed
                        if (response.code() == HttpsURLConnection.HTTP_CREATED || response.code() == 422) {
                            socialLoginViewModel.saveLoginDetails(loginResult.getAccessToken().getUserId(), loginResult.getAccessToken().getUserId());
                            subscription = socialLoginViewModel.loginFacebookUser(loginResult)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(voidResponse -> {
                                        if (voidResponse.code() == HttpsURLConnection.HTTP_OK)
                                            startZoneHomeActivity();
                                        else {
                                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.facebook_login_failed));
                                            Log.d(TAG, String.valueOf(voidResponse.code()));
                                        }
                                    }, throwable -> Log.d(TAG, throwable.getMessage()));

                        } else {
                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.facebook_login_failed));
                            Log.d(TAG, String.valueOf(response.code()));
                        }
                    }, throwable -> Log.d(TAG, throwable.getMessage()));
        }
    }

    public void registerInstagramUser(InstagramResponse instagramResponse) {
        if (instagramResponse != null) {
            subscription = socialLoginViewModel.registerInstagramUser(instagramResponse)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        if (response.code() == HttpsURLConnection.HTTP_CREATED || response.code() == 422) {
                            subscription = socialLoginViewModel.loginInstagramUser(instagramResponse)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(voidResponse -> {
                                        if (voidResponse.code() == HttpsURLConnection.HTTP_OK) {
                                            startZoneHomeActivity();
                                            spinner.hide();
                                        } else {
                                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed));
                                            Log.d(TAG, String.valueOf(voidResponse.code()));
                                        }
                                    }, throwable -> Log.d(TAG, throwable.getMessage()));

                        } else {
                            UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed));
                            Log.d(TAG, String.valueOf(response.code()));
                        }
                    }, throwable -> Log.d(TAG, throwable.getMessage()));
        }
    }

    @Override
    public void onCodeReceived(String access_token) {
        if (access_token == null) {
            authenticationDialog.dismiss();
        }

        subscription = RestClient.getRetrofitService()
                .getInstagramUserData(access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(instagramResponse -> {
                    if (instagramResponse.getMeta().getCode() == HttpURLConnection.HTTP_OK) {
                        socialLoginViewModel.saveLoginDetails(instagramResponse.getData().getUsername(), instagramResponse.getData().getUsername());
                        registerInstagramUser(instagramResponse);
                    } else
                        UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed));
                }, throwable -> UIDisplayUtil.getInstance().displaySnackBar(relativeLayout, getString(R.string.instagram_login_failed)));
    }

    @Override
    public void showProgressSpinner() {
        spinner.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startZoneHomeActivity() {
        startActivity(new Intent(SocialLoginActivity.this, ZoneHomeActivity.class));
    }
}
