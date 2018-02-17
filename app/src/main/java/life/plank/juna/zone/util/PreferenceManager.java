package life.plank.juna.zone.util;

import android.content.Context;
import android.content.SharedPreferences;

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

    public String getPreference(String key) {
        return sharedPreferences.getString(key, "NA");
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
}
