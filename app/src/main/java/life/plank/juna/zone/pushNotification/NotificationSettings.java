package life.plank.juna.zone.pushNotification;

import life.plank.juna.zone.R;
import life.plank.juna.zone.view.activity.SplashScreenActivity;

/**
 * Created by plank-dhamini on 18/11/17.
 */

public class NotificationSettings {

    public static String SenderId = SplashScreenActivity.splashScreenActivity.getString(R.string.sender_id);
    public static String HubName = SplashScreenActivity.splashScreenActivity.getString(R.string.hub_name);
    public static String HubListenConnectionString = SplashScreenActivity.splashScreenActivity.getString(R.string.hub_listen_connection);
    public static String HubFullAccess = SplashScreenActivity.splashScreenActivity.getString(R.string.hub_full_access);
}
