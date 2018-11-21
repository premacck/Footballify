package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.firebase.BoardNotification;
import life.plank.juna.zone.util.AppConstants;
import life.plank.juna.zone.util.PreferenceManager;
import life.plank.juna.zone.util.helper.ISO8601DateSerializer;
import life.plank.juna.zone.view.activity.board.JoinBoardActivity;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import life.plank.juna.zone.view.activity.zone.ZoneActivity;

import static life.plank.juna.zone.util.AppConstants.LIVE_EVENT_TYPE;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PushNotificationFirebaseMessagingService.class.getSimpleName();
    String CHANNEL_ID = "juna_notification_channel";
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
            else if (dataPayload.containsKey("invitationLink")) {
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

        if (!isNullOrEmpty(boardNotification.getInvitationLink())) {
            messageBody = boardNotification.getInviterName()
                    + " invited you to join "
                    + boardNotification.getBoardName()
                    + " board";

            Intent intent = new Intent(this, JoinBoardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(getString(R.string.board_id_prefix), boardNotification.getBoardId());
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sendTextNotification(messageBody, defaultSoundUri, pendingIntent);
            return;
        } else {
            messageBody = boardNotification.getActor()
                    + " "
                    + boardNotification.getAction()
                    + "ed" + " " + "an" + " "
                    + boardNotification.getContentType();

            Intent msgIntent;

            if (boardNotification.getForeignId() == 0) {
//                open private board in HomeActivity
                msgIntent = new Intent(this, HomeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra(getString(R.string.intent_private_board_id), boardNotification.getBoardId());
            } else {
//                open Match Board in ZoneActivity
                msgIntent = new Intent(this, ZoneActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        .putExtra(getString(R.string.intent_league_name), ""/*TODO: insert league name here*/)
                        .putExtra(getString(R.string.match_id_string), boardNotification.getForeignId());
            }
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT);
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        //TODO: Remove this and make it general, Such that the message is appropriate when the user uploads a video , image or any other content
        //Will be done in the next pull request
        if (boardNotification.getImageUrl() != null) {
            try {
                bitmap = Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(boardNotification.getImageUrl())
                        .submit(200, 200)
                        .get();
            } catch (Exception e) {
                Log.e(TAG, "Loading with Glide: ", e);
            }
        }

        String userName = PreferenceManager.CurrentUser.getDisplayName();
        //TODO: refactor this after the push notification is generalised
        if (!isNullOrEmpty(boardNotification.getActor())) {
            if (!boardNotification.getActor().equals(userName)) {
                sendNotification(messageBody, defaultSoundUri, pendingIntent);
            }
        }
    }

    private void sendTextNotification(String messageBody, Uri defaultSoundUri, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getNotificationChannel(notificationManager);
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(messageBody, defaultSoundUri, pendingIntent);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification(String messageBody, Uri defaultSoundUri, PendingIntent pendingIntent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        getNotificationChannel(notificationManager);
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder(messageBody, defaultSoundUri, pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder(String messageBody, Uri defaultSoundUri, PendingIntent pendingIntent) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);
    }

    private void getNotificationChannel(NotificationManager notificationManager) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.channel_name), importance);
            channel.setDescription(getString(R.string.channel_description));
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Async Class for handling Live data in Match boards.
     */
    private static class LiveFootballMatchNotifier extends AsyncTask<Void, Void, String> {

        private final WeakReference<Context> ref;
        private final Map<String, String> dataPayload;

        LiveFootballMatchNotifier(Context context, Gson gson, Map<String, String> dataPayload) {
            this.ref = new WeakReference<>(context);
            this.dataPayload = dataPayload;
        }

        static void notify(Context context, Gson gson, Map<String, String> dataPayload) {
            new LiveFootballMatchNotifier(context, gson, dataPayload).execute();
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