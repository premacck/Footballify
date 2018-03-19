package life.plank.juna.zone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import life.plank.juna.zone.R;
import life.plank.juna.zone.pushnotification.NotificationSettings;
import life.plank.juna.zone.pushnotification.PushNotificationsHandler;
import life.plank.juna.zone.pushnotification.RegistrationIntentService;
import life.plank.juna.zone.util.NetworkStateReceiver;
import life.plank.juna.zone.util.NetworkStatus;

public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = "SplashScreenActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static SplashScreenActivity splashScreenActivity;
    public static Boolean isVisible = false;
    private static int SPLASH_TIME_OUT = 6000;
    ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
    Runnable longRunningTask = new Runnable() {
        @Override
        public void run() {
        }
    };
    Thread thread;
    private Boolean savedLogin;
    private NetworkStateReceiver networkStateReceiver;
    private boolean isSplashScreenTimeOut = false;
    private boolean isIntentCalled = false;
    private boolean isInterrupted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash_screen_activity);
        splashScreenActivity = this;
        NotificationsManager.handleNotifications(this, NotificationSettings.senderId, PushNotificationsHandler.class);
        registerWithNotificationHubs();
        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.login_pref), MODE_PRIVATE);
        savedLogin = loginPreferences.getBoolean(getString(R.string.shared_pref_save_login), false);

/* //TODO: Uncomment when remember me is implemented on the current version
//            if (savedLogin) {
//                startActivity(new Intent(SplashScreenActivity.this, SwipePageActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(SplashScreenActivity.this, SwipePageActivity.class));
//                finish();
//            }
        }, SPLASH_TIME_OUT);*/

        launchSplashScreen();
    }

    private void launchSplashScreen() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isSplashScreenTimeOut = true;
                try {
                    Thread.sleep(5000);
                    if (NetworkStatus.isNetworkAvailable(SplashScreenActivity.this)) {
                        isIntentCalled = true;
                        if (!isInterrupted) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashScreenActivity.this, SwipePageActivity.class));
                                    finish();
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d("error message",e.toString());
                }
            }
        });
        thread.start();
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
                ToastNotify("This device is not supported by Google Play Services.");
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
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        isInterrupted = true;
        thread.interrupt();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInterrupted = false;
        launchSplashScreen();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        isInterrupted = true;
        thread.interrupt();
        super.onStop();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO: comment will be removed latrer
                // Toast.makeText(SplashScreenActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void networkAvailable() {
        if (isSplashScreenTimeOut && !isIntentCalled) {
            startActivity(new Intent(SplashScreenActivity.this, SwipePageActivity.class));
            finish();
        }
    }

    @Override
    public void networkUnavailable() {

    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /**
     * Register the NetworkStateReceiver
     *
     * @param currentContext
     */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Unregister the NetworkStateReceiver with your activity
     *
     * @param currentContext
     */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        try {
            currentContext.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
