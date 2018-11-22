package life.plank.juna.zone.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import life.plank.juna.zone.notification.dispatch

private const val TAG = "JunaFirebaseMsgService"

class JunaNotificationService : FirebaseMessagingService() {

//    TODO: This Gson instance will also take care of the Date format. un-comment if required
    /*private val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, ISO8601DateSerializer())
            .setDateFormat(DateFormat.FULL, DateFormat.FULL)
            .setLenient()
            .create()*/

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
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