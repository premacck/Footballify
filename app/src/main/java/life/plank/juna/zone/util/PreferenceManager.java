package life.plank.juna.zone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.util.Log;

import net.openid.appauth.AuthState;

import org.json.JSONException;

import java.util.Map;
import java.util.Objects;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FeedItem;

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

    public static void saveAuthState(AuthState authState) {
        getSharedPrefs(ZoneApplication.getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putString(ZoneApplication.getContext().getString(R.string.pref_auth_state), authState.jsonSerializeString())
                .apply();
    }

    public static AuthState getSavedAuthState() {
        try {
            return AuthState.jsonDeserialize(
                    Objects.requireNonNull(getSharedPrefs(ZoneApplication.getContext().getString(R.string.pref_login_credentails))
                            .getString(ZoneApplication.getContext().getString(R.string.pref_auth_state), null))
            );
        } catch (JSONException e) {
            Log.e("saveAuthState()", e.getMessage());
            return null;
        }
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

    public static class PinManager {

        private static SharedPreferences getPinSharedPreferences() {
            return ZoneApplication.getContext().getSharedPreferences("Pin", Context.MODE_PRIVATE);
        }

        public static void toggleFeedItemPin(FeedItem feedItem, boolean isPinned) {
            feedItem.setPinned(isPinned);
            SharedPreferences.Editor editor = getPinSharedPreferences().edit();
            if (isPinned) {
                editor.putBoolean(feedItem.getId(), true);
            } else {
                editor.remove(feedItem.getId());
            }
            editor.apply();
        }

        public static boolean isFeedItemPinned(FeedItem feedItem) {
            return getPinSharedPreferences().getBoolean(feedItem.getId(), false);
        }
    }
}
