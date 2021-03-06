package life.plank.juna.zone.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ClientAuthentication;
import net.openid.appauth.ClientSecretBasic;
import net.openid.appauth.RegistrationRequest;
import net.openid.appauth.ResponseTypeValues;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import life.plank.juna.zone.R;
import life.plank.juna.zone.sharedpreference.CurrentUser;
import life.plank.juna.zone.ui.auth.IdentityProvider;
import life.plank.juna.zone.ui.auth.SignInActivity;
import life.plank.juna.zone.ui.auth.TokenActivity;
import life.plank.juna.zone.ui.home.HomeActivity;

import static com.prembros.facilis.util.DataUtilKt.isNullOrEmpty;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.getSavedAuthState;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveAuthState;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveTokens;
import static life.plank.juna.zone.sharedpreference.AuthPrefsKt.saveTokensValidity;

public class AuthService {

    private static final String EXTRA_FAILED = "failed";
    private static final String TAG = AuthService.class.getSimpleName();

    /**
     * @param activity           the calling activity.
     * @param email              The email hint to auto-fill in the opening web page. Could be null.
     * @param isTokenRefreshCall true when calling to get new ID token from refresh token.
     */
    public static void loginOrRefreshToken(Activity activity, AuthorizationService authService, String email, boolean isTokenRefreshCall) {
        if (isTokenRefreshCall) {
            AuthState authState = getSavedAuthState();
            if (authState != null) {
                ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getString(R.string.just_a_moment));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                makeTokenRefreshRequest(activity, authService, authState, progressDialog);
                return;
            }
        }
        List<IdentityProvider> providers = IdentityProvider.getEnabledProviders(activity);

        for (final IdentityProvider idp : providers) {
            final AuthorizationServiceConfiguration.RetrieveConfigurationCallback retrieveCallback =
                    (serviceConfiguration, ex) -> {
                        if (ex != null) {
                            Log.e(TAG, "Failed to retrieve configuration for " + idp.name, ex);
                        } else {
                            if (!isNullOrEmpty(email)) {
                                CurrentUser.INSTANCE.saveUserEmail(email);
                            }
                            if (serviceConfiguration != null) {
                                if (idp.getClientId() == null) {
                                    // Do dynamic client registration if no client_id
                                    makeRegistrationRequest(activity, authService, serviceConfiguration, idp, email);
                                } else {
                                    makeAuthRequest(activity, authService, serviceConfiguration, idp, new AuthState(), email);
                                }
                            }
                        }
                    };
            idp.retrieveConfig(activity, retrieveCallback);
        }
    }

    /**
     * Method for starting the token refresh process
     */
    private static void makeTokenRefreshRequest(Activity activity, AuthorizationService authService, AuthState authState, ProgressDialog progressDialog) {
        ClientAuthentication clientAuthentication;
        try {
            clientAuthentication = authState.getClientAuthentication();
        } catch (ClientAuthentication.UnsupportedAuthenticationMethod ex) {
            Log.e(TAG, "Token request cannot be made, client authentication for the token "
                    + "endpoint could not be constructed (%s)", ex);
            return;
        }

        authService.performTokenRequest(authState.createTokenRefreshRequest(), clientAuthentication, (response, ex) -> {
            Log.i(TAG, "Token refresh complete");
            authState.update(response, ex);
            if (response != null) {
                saveAuthState(authState);
                saveTokens(response.idToken, response.refreshToken);
                saveTokensValidity(response.additionalParameters);
            }
            progressDialog.cancel();
            activity.startActivity(new Intent(activity, HomeActivity.class));
            activity.finish();
        });
    }

    /**
     * Method for starting the sign up process
     */
    private static void makeRegistrationRequest(
            Activity activity, AuthorizationService authService,
            @NonNull AuthorizationServiceConfiguration serviceConfig,
            @NonNull final IdentityProvider idp,
            String email) {

        final RegistrationRequest registrationRequest = new RegistrationRequest.Builder(
                serviceConfig,
                Collections.singletonList(idp.getRedirectUri()))
                .setTokenEndpointAuthenticationMethod(ClientSecretBasic.NAME)
                .build();

        Log.d(TAG, "Making registration request to " + serviceConfig.registrationEndpoint);
        authService.performRegistrationRequest(
                registrationRequest,
                (registrationResponse, ex) -> {
                    Log.d(TAG, "Registration request complete");
                    if (registrationResponse != null) {
                        idp.setClientId(registrationResponse.clientId);
                        // Continue with the authentication
                        makeAuthRequest(
                                activity,
                                authService,
                                registrationResponse.request.configuration,
                                idp,
                                new AuthState((registrationResponse)),
                                email
                        );
                    }
                });
    }

    /**
     * Method for starting the login process
     */
    private static void makeAuthRequest(
            Activity activity, AuthorizationService authService, @NonNull AuthorizationServiceConfiguration serviceConfig,
            @NonNull IdentityProvider idp,
            @NonNull AuthState authState, String emailId) {

        AuthorizationRequest authRequest = new AuthorizationRequest
                .Builder(serviceConfig, idp.getClientId(), ResponseTypeValues.CODE, idp.getRedirectUri())
                .setScope(idp.getScope())
                .setScope(activity.getString(R.string.pref_openid_offline_access))
                .setLoginHint(emailId)
                .build();

        Log.d(TAG, "Making auth request to " + serviceConfig.authorizationEndpoint);
        Intent cancelIntent = new Intent(activity, SignInActivity.class);
        cancelIntent.putExtra(EXTRA_FAILED, true);
        cancelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        authService.performAuthorizationRequest(
                authRequest,
                TokenActivity.Companion.createPostAuthorizationIntent(activity, authRequest.hashCode(), serviceConfig.discoveryDoc, authState),
                PendingIntent.getActivity(activity, 0, cancelIntent, 0),
                authService.createCustomTabsIntentBuilder()
                        .setToolbarColor(activity.getResources().getColor(R.color.colorAccent, null))
                        .build()
        );
        activity.finish();
    }
}