package life.plank.juna.zone.util.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import life.plank.juna.zone.R;

/**
 * Created by plank-hasan on 3/1/2018.
 */

public class NetworkStatus {
    public static boolean isNetworkAvailable(View view, Context context) {
        @SuppressWarnings("static-access")
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(context
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(view, context.getString(R.string.cannot_connect_to_the_internet), Snackbar.LENGTH_SHORT).show();
                }
            });
            return false;
        }
        return true;
    }
}

