package life.plank.juna.zone.pushnotification;

/**
 * Created by plank-dhamini on 18/11/17.
 */

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.microsoft.windowsazure.messaging.NotificationHub;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SplashScreenActivity;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String resultString = null;
        String regID = null;

        try {
            String fcmToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "FCM Registration Token: " + fcmToken);

            // Storing the registration id that indicates whether the generated token has been
            // sent to your server. If it is not stored, send the token to your server,
            // otherwise your server should have already received the token.
            if (((regID = sharedPreferences.getString(getString(R.string.registration_id), null)) == null)) {

                NotificationHub hub = new NotificationHub(NotificationSettings.hubName,
                        NotificationSettings.hubListenConnectionString, this);
                Log.d(TAG, "Attempting a new registration with NH using FCM token : " + fcmToken);
                regID = hub.register(fcmToken).getRegistrationId();

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/

                Log.d(TAG, "New NH Registration Successfully - RegId : " + regID);

                sharedPreferences.edit().putString(getString(R.string.registration_id), regID).apply();
                sharedPreferences.edit().putString(getString(R.string.fcm_token), fcmToken).apply();
            }

            // Check if the token may have been compromised and needs refreshing.
            else if ((sharedPreferences.getString(getString(R.string.fcm_token), "")) != fcmToken) {

                NotificationHub hub = new NotificationHub(NotificationSettings.hubName,
                        NotificationSettings.hubListenConnectionString, this);
                Log.d(TAG, "NH Registration refreshing with token : " + fcmToken);
                regID = hub.register(fcmToken).getRegistrationId();

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/en-us/documentation/articles/notification-hubs-routing-tag-expressions/

                Log.d(TAG, "New NH Registration Successfully - RegId : " + regID);

                sharedPreferences.edit().putString(getString(R.string.registration_id), regID).apply();
                sharedPreferences.edit().putString(getString(R.string.fcm_token), fcmToken).apply();
            } else {
                resultString = "Previously Registered Successfully - RegId : " + regID;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete registration", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }

        // Notify UI that registration has completed.
        if (SplashScreenActivity.isVisible) {
            SplashScreenActivity.splashScreenActivity.ToastNotify(resultString);
        }
    }
}
