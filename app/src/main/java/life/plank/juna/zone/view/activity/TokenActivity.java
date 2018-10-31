package life.plank.juna.zone.view.activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
import javax.inject.Named;

import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.User;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.UIDisplayUtil;
import life.plank.juna.zone.view.activity.home.HomeActivity;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.PreferenceManager.getSharedPrefs;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.PreferenceManager.saveAuthState;
import static life.plank.juna.zone.util.PreferenceManager.saveTokens;
import static life.plank.juna.zone.util.PreferenceManager.saveTokensValidity;

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
    @Named("default")
    Retrofit retrofit;
    private AuthState mAuthState;
    @Inject
    AuthorizationService mAuthService;
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
        restApi.getUser(getToken())
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
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<User> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                //TODO: Investigate why the response.body is saved
                                SharedPreferences.Editor prefEditor = getSharedPrefs(getString(R.string.pref_login_credentails)).edit();
                                prefEditor.putBoolean(getString(R.string.pref_is_logged_in), true).apply();

                                UIDisplayUtil.saveSignInUserDetails(TokenActivity.this, response.body());
                                if (response.body().getUserPreferences().isEmpty()) {
                                    startActivity(new Intent(TokenActivity.this, SelectZoneActivity.class));
                                } else {
                                    HomeActivity.Companion.launch(TokenActivity.this, false);
                                }
                                finish();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), R.string.user_name_not_found, Toast.LENGTH_LONG).show();
                                break;
                            default:
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