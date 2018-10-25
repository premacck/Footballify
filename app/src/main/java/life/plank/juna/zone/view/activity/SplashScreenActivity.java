package life.plank.juna.zone.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import net.openid.appauth.AuthorizationService;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.AuthUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.checkTokenValidity;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @Inject
    @Named("default")
    Retrofit retrofit;
    private RestApi restApi;
    private AuthorizationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        authService = new AuthorizationService(this);
        ((ZoneApplication) getApplicationContext()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        animationView.setSpeed(2.0f);
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                proceedToApp();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    /**
     * Flow:
     * * check if the refresh token is valid
     * * if the refresh token is valid, check if ID token is valid
     * * if Id token is valid, proceed to {@link UserFeedActivity}
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
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                User user = response.body();
                                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE).edit();
                                editor.putString(getString(R.string.pref_profile_pic_url), user.getProfilePictureUrl()).apply();
                                if (isNullOrEmpty(user.getUserPreferences())) {
                                    startActivity(new Intent(SplashScreenActivity.this, SelectZoneActivity.class));
                                } else {
                                    startActivity(new Intent(SplashScreenActivity.this, UserFeedActivity.class));
                                }

                                finish();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.user_name_not_found, Toast.LENGTH_LONG).show();
                                break;
                            default:
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