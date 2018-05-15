package life.plank.juna.zone.pushnotification;

/**
 * Created by plank-dhamini on 18/11/17.
 */

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class NotificationInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "NotificationInstanceIDService";

    @Override
    public void onTokenRefresh() {

        Log.d( TAG, "Refreshing GCM Registration Token" );

        Intent intent = new Intent( this, RegistrationIntentService.class );
        startService( intent );
    }
};
