package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.firebaseModel.BoardNotification;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.view.activity.BoardActivity;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PushNotificationFirebaseMessagingService.class.getSimpleName();
    private Bitmap bitmap;

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    public void updateBoardActivity(Context context, BoardNotification boardNotification) {

        Intent intent = new Intent(context.getString(R.string.intent_board));
        intent.putExtra(context.getString(R.string.intent_content_type), boardNotification.getContentType());
        Log.e("content type", "content_type" + boardNotification.getContentType());

        //TODO: Investigate how to pass an object from one activity to another. App crashes when trying to use Serializable and Parcelable
        if (boardNotification.getContentType().equals(AppConstants.ROOT_COMMENT)) {
            intent.putExtra(getString(R.string.intent_comment_title), boardNotification.getTitle());
        } else {
            intent.putExtra(context.getString(R.string.intent_thumbnail_url), boardNotification.getThumbnailImageUrl());
            intent.putExtra(context.getString(R.string.intent_thumbnail_height), boardNotification.getThumbnailHeight());
            intent.putExtra(context.getString(R.string.intent_thumbnail_width), boardNotification.getThumbnailWidth());
            intent.putExtra(context.getString(R.string.intent_image_url), boardNotification.getImageUrl());
        }
        //send broadcast
        context.sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            BoardNotification boardNotification = new BoardNotification();

            Map<String, String> dataPayload = remoteMessage.getData();
            JSONObject jsonObject = new JSONObject(dataPayload);
            String notificationString = jsonObject.toString();
            Gson gson = new Gson();
            boardNotification = gson.fromJson(notificationString, BoardNotification.class);

            updateBoardActivity(getApplicationContext(), boardNotification);

            sendNotification(boardNotification);
        }

    }

    public void sendNotification(BoardNotification boardNotification) {

        String messageBody = boardNotification.getActor()
                + " "
                + boardNotification.getAction()
                + "ed" + " " + "an" + " "
                + boardNotification.getContentType();


        Intent msgIntent = new Intent(this, BoardActivity.class);
        msgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //TODO: Remove this and make it general, Such that the message is appropriate when the user uploads a video , image or any other content
        //Will be done in the next pull request
        try {
            bitmap = Picasso.with(getApplicationContext()).load(boardNotification.getThumbnailImageUrl()).get();
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