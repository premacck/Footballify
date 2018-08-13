package life.plank.juna.zone.util;

import android.content.Context;
import android.content.SharedPreferences;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class PreferenceManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("General", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void removeLoginPreferences() {
        editor.clear();
        editor.commit();
    }

    public void savePinnedFeeds(String value) {
        editor.putString(AppConstants.PINNED_FEEDS, value);
        editor.commit();
    }

    public String getPinnedFeeds(String key) {
        return sharedPreferences.getString(AppConstants.PINNED_FEEDS, "");
    }

    public static SharedPreferences getSharedPrefs(String sharedPrefs) {
        return ZoneApplication.getContext().getSharedPreferences(sharedPrefs, 0);
    }

    public static String getSharedPrefs(String sharedPrefs, String preferenceKey) {
        SharedPreferences sharedPreference = ZoneApplication.getContext().getSharedPreferences(sharedPrefs, 0);
        return sharedPreference.getString(preferenceKey, "NA");
    }

    public static String getToken(Context context) {
        return context.getString(R.string.bearer) + " " + getSharedPrefs(context.getString(R.string.login_credentails), context.getString(R.string.azure_token));
    }
}
