package life.plank.juna.zone.firebasepushnotification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseInstanceService";
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d( TAG, "Refreshed token: " + refreshedToken );
        sendRegistrationToServer( refreshedToken );
    }
    private void sendRegistrationToServer(String token) {
// TODO: Implement this method to send token to your app server.
    }
}
