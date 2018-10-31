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
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.model.FeedItem;
import life.plank.juna.zone.data.model.User;

import static life.plank.juna.zone.ZoneApplication.getContext;
import static life.plank.juna.zone.util.DataUtil.findString;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;

/**
 * Created by plank-sobia on 10/4/2017.
 */

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public PreferenceManager() {
        sharedPreferences = getContext().getSharedPreferences("General", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static SharedPreferences getSharedPrefs(String sharedPrefs) {
        return getContext().getSharedPreferences(sharedPrefs, 0);
    }

    public static String getToken() {
        String token = getSharedPrefs(getContext().getString(R.string.pref_login_credentails)).getString(getContext().getString(R.string.pref_azure_token), null);
        return isNullOrEmpty(token) ? null : getContext().getString(R.string.bearer) + " " + token;
    }

    public static void saveTokens(String idToken, String refreshToken) {
        getSharedPrefs(getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putString(getContext().getString(R.string.pref_azure_token), idToken)
                .putString(getContext().getString(R.string.pref_refresh_token), refreshToken)
                .apply();
    }

    public static void saveTokensValidity(Map<String, String> additionalParameters) {
        long idTokenValidity = Long.parseLong(additionalParameters.get(getContext().getString(R.string.pref_not_before))) +
                Long.parseLong(additionalParameters.get(getContext().getString(R.string.pref_id_token_expires_in)));
        long refreshTokenValidity = Long.parseLong(additionalParameters.get(getContext().getString(R.string.pref_not_before))) +
                Long.parseLong(additionalParameters.get(getContext().getString(R.string.pref_refresh_token_expires_in)));
        getSharedPrefs(getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putLong(getContext().getString(R.string.pref_id_token_validity), idTokenValidity)
                .putLong(getContext().getString(R.string.pref_refresh_token_validity), refreshTokenValidity)
                .apply();
    }

    public static boolean checkTokenValidity(@StringRes int whichToken) {
        SharedPreferences loginPrefs = getSharedPrefs(getContext().getString(R.string.pref_login_credentails));
        long validity = loginPrefs.getLong(getContext().getString(whichToken), 0);
        return validity > System.currentTimeMillis() / 1000;
    }

    public static void saveAuthState(AuthState authState) {
        getSharedPrefs(getContext().getString(R.string.pref_login_credentails))
                .edit()
                .putString(getContext().getString(R.string.pref_auth_state), authState.jsonSerializeString())
                .apply();
    }

    public static AuthState getSavedAuthState() {
        try {
            return AuthState.jsonDeserialize(
                    Objects.requireNonNull(getSharedPrefs(getContext().getString(R.string.pref_login_credentails))
                            .getString(getContext().getString(R.string.pref_auth_state), null))
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
        SharedPreferences sharedPreference = getContext().getSharedPreferences(sharedPrefs, 0);
        return sharedPreference.getString(preferenceKey, "NA");
    }

    public static Boolean getSharedPrefsBoolean(String sharedPrefs, String preferenceKey) {
        SharedPreferences sharedPreference = getContext().getSharedPreferences(sharedPrefs, 0);
        return sharedPreference.getBoolean(preferenceKey, false);
    }

    public static void setUserLoggedIn() {
        getSharedPrefs(findString(R.string.pref_login_credentails)).edit().putBoolean(findString(R.string.pref_is_logged_in), true).apply();
    }

    public static void saveSignInUserDetails(User body) {
        SharedPreferences.Editor editor = getSharedPrefs(findString(R.string.pref_user_details)).edit();
        editor.putString(findString(R.string.pref_object_id), body.getObjectId());
        editor.putString(findString(R.string.pref_display_name), body.getDisplayName());
        editor.putString(findString(R.string.pref_email_address), body.getEmailAddress());
        editor.putString(findString(R.string.pref_country), body.getCountry());
        editor.putString(findString(R.string.pref_city), body.getCity());
        editor.putString(findString(R.string.pref_profile_pic_url), body.getProfilePictureUrl());
        editor.apply();
    }

    public static void saveProfilePicUrl(String url) {
        getSharedPrefs(findString(R.string.pref_user_details)).edit().putString(findString(R.string.pref_profile_pic_url), url).apply();
    }

    public static String getSavedProfilePicUrl() {
        return getSharedPrefs(findString(R.string.pref_user_details)).getString(findString(R.string.pref_profile_pic_url), findString(R.string.na));
    }

//    TODO: remove this class when pin functionality is done in backend
    public static class PinManager {

        private static SharedPreferences getPinSharedPreferences() {
            return getContext().getSharedPreferences("Pin", Context.MODE_PRIVATE);
        }

    public static void toggleFeedItemPin(FeedEntry feedEntry, boolean isPinned) {
        feedEntry.getFeedInteractions().setHasPinned(isPinned);
            SharedPreferences.Editor editor = getPinSharedPreferences().edit();
            if (isPinned) {
                editor.putBoolean(feedEntry.getFeedItem().getId(), true);
            } else {
                editor.remove(feedEntry.getFeedItem().getId());
            }
            editor.apply();
        }

        public static boolean isFeedItemPinned(FeedItem feedItem) {
            return getPinSharedPreferences().getBoolean(feedItem.getId(), false);
        }
    }
}
