package life.plank.juna.zone.firebasepushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.firebaseModel.BoardNotificationModel;
import life.plank.juna.zone.data.network.model.firebaseModel.Thumbnail;
import life.plank.juna.zone.firebasepushnotification.database.DBHelper;
import life.plank.juna.zone.interfaces.UpdateDataOnChartFragment;
import life.plank.juna.zone.view.activity.BoardActivity;

public class PushNotificationFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PushNotificationFirebaseMessagingService.class.getSimpleName();
    private static UpdateDataOnChartFragment updateDataOnChartFragment;
    private String user_id, image_url;
    int height,width;
    private DBHelper dbHelper = new DBHelper( this);

    public static void setUpdateDataOnChartFragment(UpdateDataOnChartFragment updateDataChartFragment) {
        updateDataOnChartFragment = updateDataChartFragment;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        BoardNotificationModel boardNotificationModel = new BoardNotificationModel();
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getNotification().getBody());
            image_url = jsonObject.getJSONObject( "thumbnail" ).getString( "imageUrl" );
            width = jsonObject.getJSONObject( "thumbnail" ).getInt(  "width"  );
            height = jsonObject.getJSONObject( "thumbnail" ).getInt(  "height"  );
            user_id = jsonObject.getString( "id" );
            Thumbnail thumbnail = new Thumbnail( image_url,width,height);
            boardNotificationModel.setThumbnail( thumbnail );
            boardNotificationModel.setId( user_id );
            Gson gson = new Gson();
            String string = gson.toJson( boardNotificationModel,BoardNotificationModel.class );
            dbHelper.insertNotificationDataInDatabase(string );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendNotification( remoteMessage.getNotification().getBody() );
    }

    public void sendNotification(String messageBody ) {
        Intent msgIntent = new Intent( this, BoardActivity.class );
        msgIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0 /* Request code */, msgIntent, PendingIntent.FLAG_ONE_SHOT );
        Uri defaultSoundUri = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder( this )
                .setContentTitle( "FCM Message" )
                .setContentText( messageBody )
                .setAutoCancel( true )
                .setSound( defaultSoundUri )
                .setSmallIcon( R.mipmap.ic_launcher )
                .setContentIntent( pendingIntent );
        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 0 /* ID of notification */, notificationBuilder.build() );
    }
}