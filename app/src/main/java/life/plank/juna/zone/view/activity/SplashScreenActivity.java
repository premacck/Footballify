
package life.plank.juna.zone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import butterknife.BindView;
import life.plank.juna.zone.R;
import life.plank.juna.zone.pushnotification.NotificationSettings;
import life.plank.juna.zone.pushnotification.PushNotificationsHandler;
import life.plank.juna.zone.pushnotification.RegistrationIntentService;

/**
 * Created by plank-dhamini on 18/7/2018.
 */

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static Thread thread;
    private boolean isInterrupted = false;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        NotificationsManager.handleNotifications(this, NotificationSettings.senderId, PushNotificationsHandler.class);
        registerWithNotificationHubs();

        launchSplashScreen();
    }

    private void launchSplashScreen() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);

                    if (!isInterrupted) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                                finish();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
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
    protected void onPause() {
        isInterrupted = true;
        thread.interrupt();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInterrupted = false;
    }

    @Override
    protected void onStop() {
        isInterrupted = true;
        thread.interrupt();
        super.onStop();
    }
}

