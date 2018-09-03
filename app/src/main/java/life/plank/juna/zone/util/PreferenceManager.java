package life.plank.juna.zone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

import java.util.Map;

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

    public static SharedPreferences getSharedPrefs(String sharedPrefs) {
        return ZoneApplication.getContext().getSharedPreferences(sharedPrefs, 0);
    }

    public static String getToken(Context context) {
        return context.getString(R.string.bearer) + " " + getSharedPrefsString(context.getString(R.string.pref_login_credentails), context.getString(R.string.pref_azure_token));
    }

    public static void saveTokens(String idToken, String refreshToken) {
        getSharedPrefs(ZoneApplication.getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putString(ZoneApplication.getContext().getString(R.string.pref_azure_token), idToken)
                .putString(ZoneApplication.getContext().getString(R.string.pref_refresh_token), refreshToken)
                .apply();
    }

    public static void saveTokensValidity(Map<String, String> additionalParameters) {
        long idTokenValidity = Long.parseLong(additionalParameters.get(ZoneApplication.getContext().getString(R.string.pref_not_before))) +
                Long.parseLong(additionalParameters.get(ZoneApplication.getContext().getString(R.string.pref_id_token_expires_in)));
        long refreshTokenValidity = Long.parseLong(additionalParameters.get(ZoneApplication.getContext().getString(R.string.pref_not_before))) +
                Long.parseLong(additionalParameters.get(ZoneApplication.getContext().getString(R.string.pref_refresh_token_expires_in)));
        getSharedPrefs(ZoneApplication.getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putLong(ZoneApplication.getContext().getString(R.string.pref_id_token_validity), idTokenValidity)
                .putLong(ZoneApplication.getContext().getString(R.string.pref_refresh_token_validity), refreshTokenValidity)
                .apply();
    }

    public static boolean checkTokenValidity(@StringRes int whichToken) {
        SharedPreferences loginPrefs = getSharedPrefs(ZoneApplication.getContext().getString(R.string.pref_login_credentails));
        long validity = loginPrefs.getLong(ZoneApplication.getContext().getString(whichToken), 0);
        return validity > System.currentTimeMillis() / 1000;
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

    public static String getSharedPrefsString(String sharedPrefs, String preferenceKey) {
        SharedPreferences sharedPreference = ZoneApplication.getContext().getSharedPreferences(sharedPrefs, 0);
        return sharedPreference.getString(preferenceKey, "NA");
    }

    public static Boolean getSharedPrefsBoolean(String sharedPrefs, String preferenceKey) {
        SharedPreferences sharedPreference = ZoneApplication.getContext().getSharedPreferences(sharedPrefs, 0);
        return sharedPreference.getBoolean(preferenceKey, false);
    }

}
