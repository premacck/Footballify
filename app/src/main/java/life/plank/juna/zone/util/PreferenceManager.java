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

    private static SharedPreferences getSharedPrefs(String sharedPrefs) {
        return getContext().getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE);
    }

    public static class Auth {
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
            try {
                long idTokenValidity = Long.parseLong(Objects.requireNonNull(additionalParameters.get(getContext().getString(R.string.pref_not_before)))) +
                        Long.parseLong(Objects.requireNonNull(additionalParameters.get(getContext().getString(R.string.pref_id_token_expires_in))));
                long refreshTokenValidity = Long.parseLong(Objects.requireNonNull(additionalParameters.get(getContext().getString(R.string.pref_not_before)))) +
                        Long.parseLong(Objects.requireNonNull(additionalParameters.get(getContext().getString(R.string.pref_refresh_token_expires_in))));
                getSharedPrefs(getContext().getString(R.string.pref_login_credentails))
                        .edit()
                        .putLong(getContext().getString(R.string.pref_id_token_validity), idTokenValidity)
                        .putLong(getContext().getString(R.string.pref_refresh_token_validity), refreshTokenValidity)
                        .apply();
            } catch (Exception e) {
                Log.e("PreferenceManager", "saveTokensValidity(): ", e);
            }
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

        static AuthState getSavedAuthState() {
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

    public static class CurrentUser {

        static SharedPreferences getUserPrefs() {
            return getSharedPrefs(findString(R.string.pref_user_details));
        }

        public static void saveUser(User user) {
            getUserPrefs().edit()
                    .putString(findString(R.string.pref_object_id), user.getObjectId())
                    .putString(findString(R.string.pref_display_name), user.getDisplayName())
                    .putString(findString(R.string.pref_handle), user.getHandle())
                    .putString(findString(R.string.pref_email_address), user.getEmailAddress())
                    .putString(findString(R.string.pref_profile_pic_url), user.getProfilePictureUrl())
                    .putString(findString(R.string.pref_country), user.getCountry())
                    .putString(findString(R.string.pref_city), user.getCity())
                    .apply();
        }

        static void saveUserEmail(String email) {
            getUserPrefs().edit().putString(findString(R.string.pref_email_address), email).apply();
        }

        public static void saveUserLoginStatus(boolean isLoggedIn) {
            getUserPrefs().edit().putBoolean(findString(R.string.pref_is_logged_in), isLoggedIn).apply();
        }

        public static String getUserEmail() {
            return getUserPrefs().getString(findString(R.string.pref_email_address), null);
        }

        public static String getUserId() {
            return getUserPrefs().getString(findString(R.string.pref_object_id), null);
        }

        public static String getDisplayName() {
            return getUserPrefs().getString(findString(R.string.pref_display_name), null);
        }

        public static String getHandle() {
//            TODO: add null as default return value when backend completes forum comment mentions
            return getUserPrefs().getString(findString(R.string.pref_handle), getDisplayName().replace(" ", ""));
        }

        public static String getProfilePicUrl() {
            return getUserPrefs().getString(findString(R.string.pref_profile_pic_url), null);
        }

        public static void saveProfilePicUrl(String url) {
            getUserPrefs().edit().putString(findString(R.string.pref_profile_pic_url), url).apply();
        }
    }
}
