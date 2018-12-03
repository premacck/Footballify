package life.plank.juna.zone.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import life.plank.juna.zone.notification.dispatch

private const val TAG = "JunaFirebaseMsgService"

class JunaNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "Refreshed token: $token")
//    TODO: Implement this method to send token to your app server.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Check if message contains a data payload.
        if (remoteMessage?.data?.isNotEmpty() == true) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            dispatch(remoteMessage.data)
        }
    }
}