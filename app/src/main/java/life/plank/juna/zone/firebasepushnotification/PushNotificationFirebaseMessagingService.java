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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.firebaseModel.BoardNotification;
import life.plank.juna.zone.firebasepushnotification.database.DBHelper;
import life.plank.juna.zone.view.activity.BoardActivity;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PushNotificationFirebaseMessagingService.class.getSimpleName();
    private DBHelper dbHelper = new DBHelper(this);
    private Bitmap bitmap;


    public void updateBoardActivity(Context context,JSONObject object) throws JSONException {

        Intent intent = new Intent(context.getString(R.string.board_intent));

        //TODO: Investigate how to pass an object from one activity to another. App crashes when trying to use Serializable and Parcelable
        intent.putExtra(context.getString(R.string.content_type), object.get("ContentType").toString());
        intent.putExtra(context.getString(R.string.thumbnail_url), object.get("ThumbnailImageUrl").toString());
        intent.putExtra(context.getString(R.string.thumbnail_height), object.get("ThumbnailHeight").toString());
        intent.putExtra(context.getString(R.string.thumbnail_width), object.get("ThumbnailHeight").toString());
        intent.putExtra(context.getString(R.string.image_url), object.get("ImageUrl").toString());

        //send broadcast
        context.sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("JSON_OBJECT", object.toString());

            try {
                updateBoardActivity(getApplicationContext(), object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendNotification(remoteMessage);
        }

    }

    public void sendNotification(RemoteMessage remoteMessage) {

        //Check case
        String contentType = remoteMessage.getData().get("ContentType").toString();
        String thumbnailImageUrl = remoteMessage.getData().get("ThumbnailImageUrl").toString();
        String thumbnailWidth = remoteMessage.getData().get("ThumbnailWidth").toString();
        String thumbnailHeight = remoteMessage.getData().get("ThumbnailHeight").toString();
        String imageUrl = remoteMessage.getData().get("ImageUrl").toString();
        String action = remoteMessage.getData().get("Action").toString();
        String actor = remoteMessage.getData().get("Actor").toString();


        String messageBody = actor
                + " "
                + action
                + "ed" + " " + "an" + " "
                + contentType;


        Intent msgIntent = new Intent(this, BoardActivity.class);
        msgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //TODO: Remove this and make it general, Such that the message is appropriate when the user uploads a video , image or any other content
        //Will be done in the next pull request
        try {
            bitmap = Picasso.with(getApplicationContext()).load(thumbnailImageUrl).get();
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