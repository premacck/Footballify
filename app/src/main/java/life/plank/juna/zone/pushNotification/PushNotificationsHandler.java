package life.plank.juna.zone.pushNotification;

/**
 * Created by plank-dhamini on 18/11/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SplashScreenActivity;

public class PushNotificationsHandler extends NotificationsHandler {
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;
    Context context;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        this.context = context;
        String nhMessage = bundle.getString("message");
        sendNotification(nhMessage);
        if (SplashScreenActivity.isVisible) {
            SplashScreenActivity.splashScreenActivity.ToastNotify(nhMessage);
        }
    }

    private void sendNotification(String msg) {

        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification Hub Demo")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setSound(defaultSoundUri)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}