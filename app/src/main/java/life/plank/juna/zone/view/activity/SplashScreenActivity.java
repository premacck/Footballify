package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;

import net.openid.appauth.AuthorizationService;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.common.AuthUtil;
import life.plank.juna.zone.util.sharedpreference.PreferenceManager;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.common.RestUtilKt.errorToast;
import static life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.checkTokenValidity;
import static life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @Inject
    RestApi restApi;
    private AuthorizationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        authService = new AuthorizationService(this);
        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        animationView.setSpeed(3.0f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        proceedToApp();
    }

    /**
     * Flow:
     * * check if the refresh token is valid
     * * if the refresh token is valid, check if ID token is valid
     * * if Id token is valid, proceed to {@link HomeActivity}
     * * if Id token is not valid, try to refresh the token
     * * if the refresh token is not valid, go to {@link SignInActivity}
     */
    private void proceedToApp() {
        if (checkTokenValidity(R.string.pref_refresh_token_validity)) {
            if (checkTokenValidity(R.string.pref_id_token_validity)) {
                getUserPreference();
            } else {
                AuthUtil.loginOrRefreshToken(this, authService, null, true);
            }
        } else {
            startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
            finish();
        }
    }

    private void getUserPreference() {
        restApi.getUser(getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                User user = response.body();
                                if (user != null) {
                                    PreferenceManager.CurrentUser.saveUser(user);
                                    if (isNullOrEmpty(user.getUserPreferences())) {
                                        startActivity(new Intent(SplashScreenActivity.this, SelectZoneActivity.class));
                                    } else {
                                        startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                                    }
                                }
                                finish();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                errorToast(R.string.user_name_not_found, response);
                                break;
                            default:
                                errorToast(R.string.something_went_wrong, response);
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        authService.dispose();
        super.onDestroy();
    }
}