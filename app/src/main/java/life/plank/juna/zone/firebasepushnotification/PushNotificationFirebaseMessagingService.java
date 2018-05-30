package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import life.plank.juna.zone.R;
import life.plank.juna.zone.interfaces.UpdateDataOnChartFragment;
import life.plank.juna.zone.view.activity.MatchResultActivity;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static UpdateDataOnChartFragment updateDataOnChartFragment;

    public static void setUpdateDataOnChartFragment(UpdateDataOnChartFragment updateDataChartFragment) {
        updateDataOnChartFragment = updateDataChartFragment;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e( "onMessageReceived ", "From: " + remoteMessage.getFrom() );
        sendNotification( "messageBody" );
        try {
            if (updateDataOnChartFragment != null)
                updateDataOnChartFragment.updateData( "10", 20 );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String messageBody) {
        Intent msgIntent = new Intent( this, MatchResultActivity.class );
        msgIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0 /* Request code */, msgIntent,
                PendingIntent.FLAG_ONE_SHOT );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this )
                .setContentTitle( "FCM Message" )
                .setContentText( messageBody )
                .setAutoCancel( true )
                .setSound( defaultSoundUri )
                .setSmallIcon( R.mipmap.ic_launcher )
                .setContentIntent( pendingIntent );
        NotificationManager notificationManager =
                (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 0 /* ID of notification */, notificationBuilder.build() );
    }
}