package life.plank.juna.zone.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 3/1/2018.
 */

public class NetworkStatus {
    public static boolean checkNetworkStatus(Context context) {
        @SuppressWarnings("static-access")
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(context
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(context, context.getString(R.string.cannot_connect_to_the_internet), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

