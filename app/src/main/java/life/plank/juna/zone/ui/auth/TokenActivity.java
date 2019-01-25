package life.plank.juna.zone.ui.auth;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.api.RestApi;
import life.plank.juna.zone.data.model.user.User;
import life.plank.juna.zone.sharedpreference.CurrentUser;
import life.plank.juna.zone.ui.home.HomeActivity;
import life.plank.juna.zone.ui.zone.SelectZoneActivity;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.getIdToken;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveAuthState;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveTokens;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveTokensValidity;
import static life.plank.juna.zone.util.common.ToastUtilKt.errorToast;

/**
 * Client to the Native Oauth library.
 */

public class TokenActivity extends AppCompatActivity {
    private static final String TAG = "TokenActivity";
    private static final String KEY_AUTH_STATE = "authState";
    private static final String KEY_USER_INFO = "userInfo";
    private static final String EXTRA_AUTH_SERVICE_DISCOVERY = "authServiceDiscovery";
    private static final String EXTRA_AUTH_STATE = "authState";
    @Inject
    Retrofit retrofit;
    @Inject
    AuthorizationService mAuthService;
    private AuthState mAuthState;
    private JSONObject mUserInfoJson;
    private RestApi restApi;
    private ProgressDialog progressDialog;

    public static PendingIntent createPostAuthorizationIntent(
            @NonNull Context context,
            @NonNull AuthorizationRequest request,
            @Nullable AuthorizationServiceDiscovery discoveryDoc,
            @NonNull AuthState authState) {
        Intent intent = new Intent(context, TokenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_AUTH_STATE, authState.jsonSerializeString());
        if (discoveryDoc != null) {
            intent.putExtra(EXTRA_AUTH_SERVICE_DISCOVERY, discoveryDoc.docJson.toString());
        }

        return PendingIntent.getActivity(context, request.hashCode(), intent, 0);
    }

    static AuthState getAuthStateFromIntent(Intent intent) {
        if (!intent.hasExtra(EXTRA_AUTH_STATE)) {
            throw new IllegalArgumentException("The AuthState instance is missing in the intent.");
        }
        try {
            return AuthState.jsonDeserialize(intent.getStringExtra(EXTRA_AUTH_STATE));
        } catch (JSONException ex) {
            Log.e(TAG, "Malformed AuthState JSON saved", ex);
            throw new IllegalArgumentException("The AuthState instance is missing in the intent.");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (mAuthState != null) {
            state.putString(KEY_AUTH_STATE, mAuthState.jsonSerializeString());
        }

        if (mUserInfoJson != null) {
            state.putString(KEY_USER_INFO, mUserInfoJson.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.just_a_moment));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        restApi = retrofit.create(RestApi.class);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_AUTH_STATE)) {
                try {
                    mAuthState = AuthState.jsonDeserialize(Objects.requireNonNull(savedInstanceState.getString(KEY_AUTH_STATE)));
                } catch (JSONException ex) {
                    Log.e(TAG, "Malformed authorization JSON saved", ex);
                }
            }

            if (savedInstanceState.containsKey(KEY_USER_INFO)) {
                try {
                    mUserInfoJson = new JSONObject(savedInstanceState.getString(KEY_USER_INFO));
                } catch (JSONException ex) {
                    Log.e(TAG, "Failed to parse saved user info JSON", ex);
                }
            }
        }

        if (mAuthState == null) {
            mAuthState = getAuthStateFromIntent(getIntent());
            AuthorizationResponse response = AuthorizationResponse.fromIntent(getIntent());
            AuthorizationException ex = AuthorizationException.fromIntent(getIntent());
            mAuthState.update(response, ex);

            if (response != null) {
                Log.d(TAG, "Received AuthorizationResponse.");
                performTokenRequest(response.createTokenExchangeRequest());
            } else {
                Log.e(TAG, "Authorization failed: " + ex);
            }
        }
    }

    private void performTokenRequest(TokenRequest request) {
        ClientAuthentication clientAuthentication;
        try {
            clientAuthentication = mAuthState.getClientAuthentication();
        } catch (ClientAuthentication.UnsupportedAuthenticationMethod ex) {
            Log.e(TAG, "Token request cannot be made, client authentication for the token "
                    + "endpoint could not be constructed (%s)", ex);
            return;
        }

        mAuthService.performTokenRequest(
                request,
                clientAuthentication,
                this::receivedTokenResponse);
    }

    private void receivedTokenResponse(
            @Nullable TokenResponse tokenResponse,
            @Nullable AuthorizationException authException) {
        Log.d(TAG, "Token request complete");
        mAuthState.update(tokenResponse, authException);
        if (tokenResponse != null) {
            saveAuthState(mAuthState);
            saveTokens(tokenResponse.idToken, tokenResponse.refreshToken);
            saveTokensValidity(tokenResponse.additionalParameters);
        }
        getSignInResponse();
    }

    private void getSignInResponse() {
        restApi.getUser(getIdToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e);
                        errorToast(R.string.something_went_wrong, e);
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                CurrentUser.INSTANCE.saveUserLoginStatus(true);
                                User user = response.body();
                                if (user != null) {
                                    CurrentUser.INSTANCE.saveUser(user);
                                    if (isNullOrEmpty(user.getUserPreferences())) {
                                        startActivity(new Intent(TokenActivity.this, SelectZoneActivity.class));
                                    } else {
                                        HomeActivity.Companion.launch(TokenActivity.this, false);
                                    }
                                }
                                finish();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                errorToast(R.string.user_name_not_found, response);
                                break;
                            default:
                                errorToast(R.string.something_went_wrong, response);
                                Log.e(TAG, response.message());
                                break;
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        mAuthService.dispose();
        super.onDestroy();
    }
}