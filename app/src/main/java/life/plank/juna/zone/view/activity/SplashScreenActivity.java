package life.plank.juna.zone.view.activity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import net.openid.appauth.AuthorizationService;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.User;
import life.plank.juna.zone.pushnotification.NotificationSettings;
import life.plank.juna.zone.pushnotification.PushNotificationsHandler;
import life.plank.juna.zone.pushnotification.RegistrationIntentService;
import life.plank.juna.zone.util.AuthUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
        NotificationsManager.handleNotifications(this, NotificationSettings.senderId, PushNotificationsHandler.class);
        registerWithNotificationHubs();
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

                                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE).edit();
                                editor.putString(getString(R.string.pref_profile_pic_url), response.body().getProfilePictureUrl()).apply();
                                if (response.body().getUserPreferences().isEmpty()) {
                                    startActivity(new Intent(SplashScreenActivity.this, ZoneActivity.class));
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


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                Toast.makeText(this, "This device is not supported by Google Play Services.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerWithNotificationHubs() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        authService.dispose();
        super.onDestroy();
    }
}