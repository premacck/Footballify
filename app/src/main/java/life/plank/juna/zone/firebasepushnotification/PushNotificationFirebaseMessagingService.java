package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.firebase.BoardNotification;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.helper.ISO8601DateSerializer;
import life.plank.juna.zone.view.activity.JoinBoardActivity;
import life.plank.juna.zone.view.activity.MatchBoardActivity;
import life.plank.juna.zone.view.activity.PrivateBoardActivity;

import static life.plank.juna.zone.util.AppConstants.LIVE_EVENT_TYPE;

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

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new ISO8601DateSerializer())
                    .setDateFormat(DateFormat.FULL, DateFormat.FULL)
                    .setLenient()
                    .create();

            Map<String, String> dataPayload = remoteMessage.getData();
            if (dataPayload.containsKey(getApplicationContext().getString(R.string.intent_content_type))) {
                JSONObject jsonObject = new JSONObject(dataPayload);
                String notificationString = jsonObject.toString();
                BoardNotification boardNotification = gson.fromJson(notificationString, BoardNotification.class);

                updateBoardActivity(getApplicationContext(), boardNotification);

                sendNotification(boardNotification);
            } else if (dataPayload.containsKey(LIVE_EVENT_TYPE)) {
                LiveFootballMatchNotifier.notify(getApplicationContext(), gson, dataPayload);
            }
            //TODO: Move to strings.xml and change to camel case once done on backend
            else if (dataPayload.containsKey("InvitationLink")) {
                JSONObject jsonObject = new JSONObject(dataPayload);
                String notificationString = jsonObject.toString();
                BoardNotification boardNotification = gson.fromJson(notificationString, BoardNotification.class);
                sendNotification(boardNotification);
            }
        }
    }

    public void sendNotification(BoardNotification boardNotification) {

        //TODO: Construct custom message
        PendingIntent pendingIntent;
        Uri defaultSoundUri;
        String messageBody;

        if (boardNotification.getInvitationLink() != null) {

            messageBody = boardNotification.getInviterName()
                    + " invited you to join "
                    + boardNotification.getBoardId()
                    + " board";
        } else {
            messageBody = boardNotification.getActor()
                    + " "
                    + boardNotification.getAction()
                    + "ed" + " " + "an" + " "
                    + boardNotification.getContentType();
        }

        if (boardNotification.getInvitationLink() != null) {
            Intent intent = new Intent(this, JoinBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(getString(R.string.board_id_prefix), boardNotification.getBoardId());
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        } else {
            Intent msgIntent = new Intent(this, boardNotification.getForeignId() == 0 ? PrivateBoardActivity.class : MatchBoardActivity.class);
            msgIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            msgIntent.putExtra(getString(R.string.match_id_string), boardNotification.getForeignId());
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT);
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        //TODO: Remove this and make it general, Such that the message is appropriate when the user uploads a video , image or any other content
        //Will be done in the next pull request
        try {
            bitmap = Picasso.with(getApplicationContext()).load(boardNotification.getThumbnailImageUrl()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        SharedPreferences sharedPref = ZoneApplication.getContext().getSharedPreferences(getString(R.string.pref_user_details), MODE_PRIVATE);
        String userName = sharedPref.getString(ZoneApplication.getContext().getString(R.string.pref_display_name), "NA");
        //TODO: refactor this after the push notification is generalised
        if (boardNotification.getActor() != null) {
            if (!boardNotification.getActor().equals(userName)) {
                sendNotification(messageBody, defaultSoundUri, pendingIntent);
            }
        } else if (boardNotification.getInviterName() != null) {
            sendNotification(messageBody, defaultSoundUri, pendingIntent);
        }

    }

    private void sendNotification(String messageBody, Uri defaultSoundUri, PendingIntent pendingIntent) {
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

    /**
     * Async Class for handling Live data in Match boards.
     */
    private static class LiveFootballMatchNotifier extends AsyncTask<Void, Void, String> {

        private final WeakReference<Context> ref;
        private final Map<String, String> dataPayload;

        static void notify(Context context, Gson gson, Map<String, String> dataPayload) {
            new LiveFootballMatchNotifier(context, gson, dataPayload).execute();
        }

        LiveFootballMatchNotifier(Context context, Gson gson, Map<String, String> dataPayload) {
            this.ref = new WeakReference<>(context);
            this.dataPayload = dataPayload;
        }

        @Override
        protected String doInBackground(Void... voids) {
            JSONObject zoneLiveDataJsonObject = new JSONObject(dataPayload);
            return zoneLiveDataJsonObject.toString();
        }

        @Override
        protected void onPostExecute(String zoneLiveDataString) {
            Intent intent = new Intent(ref.get().getString(R.string.intent_board));
            intent.putExtra(ref.get().getString(R.string.intent_zone_live_data), zoneLiveDataString);
            ref.get().sendBroadcast(intent);
        }
    }
}