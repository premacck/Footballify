package life.plank.juna.zone.view.activity.auth;

/**
 * Created by plank-dhamini on 15/02/18.
 */

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import net.openid.appauth.AuthorizationServiceConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import life.plank.juna.zone.R;

/**
 * An abstraction of identity providers, containing all necessary info.
 */
public class IdentityProvider {

    /**
     * Value used to indicate that a configured property is not specified or required.
     */
    public static final int NOT_SPECIFIED = -1;

    public static final IdentityProvider B2C_SignUpIn = new IdentityProvider(
            "B2C Sign Up/In",
            R.bool.b2c_enabled,
            R.string.b2c_discovery_uri,
            NOT_SPECIFIED, // auth endpoint is discovered
            NOT_SPECIFIED, // token endpoint is discovered
            NOT_SPECIFIED, // dynamic registration not supported
            R.string.b2c_tenant,
            R.string.b2c_client_id,
            R.string.b2c_redirect_uri,
            R.string.b2c_signupin_policy,
            R.string.b2c_scope_string,
            R.drawable.ic_default_profile,
            R.string.b2c_name,
            android.R.color.holo_green_dark);

    public static IdentityProvider idpList[] = new IdentityProvider[] {B2C_SignUpIn};

    public static final List<IdentityProvider> PROVIDERS = Arrays.asList(idpList);
    @NonNull
    public final String name;
    @DrawableRes
    public final int buttonImageRes;
    @StringRes
    public final int buttonContentDescriptionRes;
    public final int buttonTextColorRes;
    @BoolRes
    private final int mEnabledRes;
    @StringRes
    private final int mDiscoveryEndpointRes;
    @StringRes
    private final int mAuthEndpointRes;
    @StringRes
    private final int mTokenEndpointRes;
    @StringRes
    private final int mRegistrationEndpointRes;
    @StringRes
    private final int mTenantRes;
    @StringRes
    private final int mClientIdRes;
    @StringRes
    private final int mRedirectUriRes;
    @StringRes
    private final int mPolicyRes;
    @StringRes
    private final int mScopeRes;
    private boolean mConfigurationRead = false;
    private boolean mDiscoveryUriFormatted = false;
    private boolean mEnabled;
    private Uri mDiscoveryEndpoint;
    private Uri mAuthEndpoint;
    private Uri mTokenEndpoint;
    private Uri mRegistrationEndpoint;
    private String mTenant;
    private String mClientId;
    private Uri mRedirectUri;
    private String mPolicy;
    private String mScope;

    IdentityProvider(
            @NonNull String name,
            @BoolRes int enabledRes,
            @StringRes int discoveryEndpointRes,
            @StringRes int authEndpointRes,
            @StringRes int tokenEndpointRes,
            @StringRes int registrationEndpointRes,
            @StringRes int tenantRes,
            @StringRes int clientIdRes,
            @StringRes int redirectUriRes,
            @StringRes int policyRes,
            @StringRes int scopeRes,
            @DrawableRes int buttonImageRes,
            @StringRes int buttonContentDescriptionRes,
            @ColorRes int buttonTextColorRes) {
        if (!isSpecified(discoveryEndpointRes)
                && !isSpecified(authEndpointRes)
                && !isSpecified(tokenEndpointRes)) {
            throw new IllegalArgumentException(
                    "the discovery endpoint or the auth and token endpoints must be specified");
        }

        this.name = name;
        this.mEnabledRes = checkSpecified(enabledRes, "enabledRes");
        this.mDiscoveryEndpointRes = discoveryEndpointRes;
        this.mAuthEndpointRes = authEndpointRes;
        this.mTokenEndpointRes = tokenEndpointRes;
        this.mRegistrationEndpointRes = registrationEndpointRes;
        this.mTenantRes = tenantRes;
        this.mClientIdRes = clientIdRes;
        this.mRedirectUriRes = checkSpecified(redirectUriRes, "redirectUriRes");
        this.mPolicyRes = checkSpecified(policyRes, "policyRes");
        this.mScopeRes = checkSpecified(scopeRes, "scopeRes");
        this.buttonImageRes = checkSpecified(buttonImageRes, "buttonImageRes");
        this.buttonContentDescriptionRes =
                checkSpecified(buttonContentDescriptionRes, "buttonContentDescriptionRes");
        this.buttonTextColorRes = checkSpecified(buttonTextColorRes, "buttonTextColorRes");
    }

    public static List<IdentityProvider> getEnabledProviders(Context context) {
        ArrayList<IdentityProvider> providers = new ArrayList<>();
        for (IdentityProvider provider : PROVIDERS) {
            provider.readConfiguration(context);
            if (provider.isEnabled()) {
                providers.add(provider);
            }
        }
        return providers;
    }

    private static boolean isSpecified(int value) {
        return value != NOT_SPECIFIED;
    }

    private static int checkSpecified(int value, String valueName) {
        if (value == NOT_SPECIFIED) {
            throw new IllegalArgumentException(valueName + " must be specified");
        }
        return value;
    }

    private static Uri getUriResource(Resources res, @StringRes int resId, String resName) {
        return Uri.parse(res.getString(resId));
    }

    /**
     * This must be called before any of the getters will function.
     */
    public void readConfiguration(Context context) {
        if (mConfigurationRead) {
            return;
        }

        Resources res = context.getResources();
        mEnabled = res.getBoolean(mEnabledRes);

        mDiscoveryEndpoint = isSpecified(mDiscoveryEndpointRes)
                ? getUriResource(res, mDiscoveryEndpointRes, context.getString(R.string.discovery_endpoint_value_name))
                : null;
        mAuthEndpoint = isSpecified(mAuthEndpointRes)
                ? getUriResource(res, mAuthEndpointRes, context.getString(R.string.auth_end_point_value_name))
                : null;
        mTokenEndpoint = isSpecified(mTokenEndpointRes)
                ? getUriResource(res, mTokenEndpointRes, context.getString(R.string.token_end_point_value_name))
                : null;
        mRegistrationEndpoint = isSpecified(mRegistrationEndpointRes)
                ? getUriResource(res, mRegistrationEndpointRes, context.getString(R.string.registration_end_point_value_name))
                : null;
        mTenant = isSpecified(mTenantRes)
                ? res.getString(mTenantRes)
                : null;
        mClientId = isSpecified(mClientIdRes)
                ? res.getString(mClientIdRes)
                : null;
        mRedirectUri = getUriResource(res, mRedirectUriRes, context.getString(R.string.redirect_value_name));
        mPolicy = isSpecified(mPolicyRes)
                ? res.getString(mPolicyRes)
                : null;
        mScope = res.getString(mScopeRes);

        mConfigurationRead = true;
    }

    private void checkConfigurationRead() {
        if (!mConfigurationRead) {
            throw new IllegalStateException("Configuration not read");
        }
    }

    public boolean isEnabled() {
        checkConfigurationRead();
        return mEnabled;
    }

    @Nullable
    public Uri getDiscoveryEndpoint() {
        checkConfigurationRead();
        if (mDiscoveryEndpoint != null && !mDiscoveryUriFormatted) {
            String discoveryEndpointString = String.format(mDiscoveryEndpoint.toString(), getTenant(), getPolicy());
            mDiscoveryEndpoint = Uri.parse(discoveryEndpointString);
        }
        return mDiscoveryEndpoint;
    }

    @NonNull
    public String getTenant() {
        checkConfigurationRead();
        return mTenant;
    }

    public String getClientId() {
        checkConfigurationRead();
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    @NonNull
    public Uri getRedirectUri() {
        checkConfigurationRead();
        return mRedirectUri;
    }

    @NonNull
    public String getPolicy() {
        checkConfigurationRead();
        return mPolicy;
    }

    @NonNull
    public String getScope() {
        checkConfigurationRead();
        return mScope;
    }

    public void retrieveConfig(Context context,
                               AuthorizationServiceConfiguration.RetrieveConfigurationCallback callback) {
        readConfiguration(context);
        if (getDiscoveryEndpoint() != null) {
            AuthorizationServiceConfiguration.fetchFromUrl(mDiscoveryEndpoint, callback);
        } else {
            AuthorizationServiceConfiguration config =
                    new AuthorizationServiceConfiguration(mAuthEndpoint, mTokenEndpoint,
                            mRegistrationEndpoint);
            callback.onFetchConfigurationCompleted(config, null);
        }
    }
}



