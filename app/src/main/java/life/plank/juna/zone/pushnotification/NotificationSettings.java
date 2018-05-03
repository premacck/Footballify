package life.plank.juna.zone.pushnotification;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SplashScreenActivity;

/**
 * Created by plank-dhamini on 18/11/17.
 */

public class NotificationSettings {

    public static String senderId = SplashScreenActivity.splashScreenActivity.getString(R.string.sender_id);
    public static String hubName = SplashScreenActivity.splashScreenActivity.getString(R.string.hub_name);
    public static String hubListenConnectionString = SplashScreenActivity.splashScreenActivity.getString(R.string.hub_listen_connection);
}
