package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.firebaseModel.BoardNotification;
import life.plank.juna.zone.firebasepushnotification.database.DBHelper;
import life.plank.juna.zone.view.activity.BoardActivity;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PushNotificationFirebaseMessagingService.class.getSimpleName();
    private Bitmap bitmap;


    public void updateBoardActivity(Context context, BoardNotification boardNotification) {

        Intent intent = new Intent(context.getString(R.string.board_intent));

        //TODO: Investigate how to pass an object from one activity to another. App crashes when trying to use Serializable and Parcelable
        intent.putExtra(context.getString(R.string.content_type), boardNotification.getFeedItem().getContentType());
        intent.putExtra(context.getString(R.string.thumbnail_url), boardNotification.getFeedItem().getThumbnail().getImageUrl());
        intent.putExtra(context.getString(R.string.thumbnail_height), boardNotification.getFeedItem().getThumbnail().getHeight());
        intent.putExtra(context.getString(R.string.thumbnail_width), boardNotification.getFeedItem().getThumbnail().getWidth());
        intent.putExtra(context.getString(R.string.image_url), boardNotification.getFeedItem().getUrl());

        //send broadcast
        context.sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationString = remoteMessage.getNotification().getBody();
        BoardNotification boardNotification = new BoardNotification();
        Gson gson = new Gson();
        boardNotification = gson.fromJson(notificationString, BoardNotification.class);
        updateBoardActivity(getApplicationContext(), boardNotification);
        sendNotification(boardNotification);
    }

    public void sendNotification(BoardNotification boardNotification) {
        Intent msgIntent = new Intent(this, BoardActivity.class);
        msgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //TODO: Remove this and make it general, Such that the message is appropriate when the user uploads a video , image or any other content
        //Will be done in the next pull request
        String messageBody = boardNotification.getActivity().getActor()
                + " "
                + boardNotification.getActivity().getAction()
                + "ed" + " " + "an" + " "
                + boardNotification.getFeedItem().getContentType();
        try {
            bitmap = Picasso.with(getApplicationContext()).load(boardNotification.getFeedItem().getThumbnail().getImageUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Zone")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))/*Notification with Image*/;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}