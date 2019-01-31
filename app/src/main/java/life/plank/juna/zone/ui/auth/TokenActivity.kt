@file:Suppress("DEPRECATION")

package life.plank.juna.zone.ui.auth

import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.sharedpreference.*
import life.plank.juna.zone.ui.home.HomeActivity
import life.plank.juna.zone.ui.zone.SelectZoneActivity
import life.plank.juna.zone.util.common.errorToast
import net.openid.appauth.*
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection.*
import javax.inject.Inject

/**
 * Client to the Native Oauth library.
 */
class TokenActivity : AppCompatActivity() {
    @Inject
    lateinit var restApi: RestApi
    @Inject
    lateinit var mAuthService: AuthorizationService

    private var mAuthState: AuthState? = null
    private var mUserInfoJson: JSONObject? = null
    private var progressDialog: ProgressDialog? = null

    companion object {
        private const val TAG = "TokenActivity"
        private const val KEY_AUTH_STATE = "authState"
        private const val KEY_USER_INFO = "userInfo"
        private const val EXTRA_AUTH_SERVICE_DISCOVERY = "authServiceDiscovery"
        private const val EXTRA_AUTH_STATE = "authState"


        /**
         * Returns a [PendingIntent] for [TokenActivity] containing the current [AuthState] as [EXTRA_AUTH_STATE]
         */
        fun createPostAuthorizationIntent(context: Context, requestHashCode: Int, discoveryDoc: AuthorizationServiceDiscovery?, authState: AuthState): PendingIntent {
            val intent = Intent(context, TokenActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(EXTRA_AUTH_STATE, authState.jsonSerializeString())
            if (discoveryDoc != null) {
                intent.putExtra(EXTRA_AUTH_SERVICE_DISCOVERY, discoveryDoc.docJson.toString())
            }
            return PendingIntent.getActivity(context, requestHashCode, intent, 0)
        }
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        if (mAuthState != null) {
            state.putString(KEY_AUTH_STATE, mAuthState!!.jsonSerializeString())
        }

        if (mUserInfoJson != null) {
            state.putString(KEY_USER_INFO, mUserInfoJson!!.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupProgressDialog()
        ZoneApplication.application.uiComponent.inject(this)

        handleSavedInstanceStateIfAny(savedInstanceState)

        if (mAuthState == null) {
            mAuthState = getAuthStateFromIntent(intent)
            val response = AuthorizationResponse.fromIntent(intent)
            val ex = AuthorizationException.fromIntent(intent)
            mAuthState?.update(response, ex)

            if (response != null) {
                Log.d(TAG, "Received AuthorizationResponse.")
                performTokenRequest(response.createTokenExchangeRequest())
            } else {
                Log.e(TAG, "Authorization failed: " + ex!!)
            }
        }
    }

    private fun setupProgressDialog() {
        progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.just_a_moment))
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun getAuthStateFromIntent(intent: Intent): AuthState {
        if (!intent.hasExtra(EXTRA_AUTH_STATE)) {
            throw IllegalArgumentException("The AuthState instance is missing in the intent.")
        }
        try {
            return AuthState.jsonDeserialize(intent.getStringExtra(EXTRA_AUTH_STATE))
        } catch (ex: JSONException) {
            Log.e(TAG, "Malformed AuthState JSON saved", ex)
            throw IllegalArgumentException("The AuthState instance is missing in the intent.")
        }

    }

    private fun handleSavedInstanceStateIfAny(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            when {
                savedInstanceState.containsKey(KEY_AUTH_STATE) -> try {
                    mAuthState = AuthState.jsonDeserialize(savedInstanceState.getString(KEY_AUTH_STATE)!!)
                } catch (ex: JSONException) {
                    Log.e(TAG, "Malformed authorization JSON saved", ex)
                }
                savedInstanceState.containsKey(KEY_USER_INFO) -> try {
                    mUserInfoJson = JSONObject(savedInstanceState.getString(KEY_USER_INFO))
                } catch (ex: JSONException) {
                    Log.e(TAG, "Failed to parse saved user info JSON", ex)
                }
            }
        }
    }

    private fun performTokenRequest(request: TokenRequest) {
        val clientAuthentication: ClientAuthentication
        try {
            clientAuthentication = mAuthState!!.clientAuthentication
        } catch (ex: ClientAuthentication.UnsupportedAuthenticationMethod) {
            Log.e(TAG, "Token request cannot be made, client authentication for the token " + "endpoint could not be constructed (%s)", ex)
            return
        }
        mAuthService.performTokenRequest(request, clientAuthentication, this::receivedTokenResponse)
    }

    private fun receivedTokenResponse(tokenResponse: TokenResponse?, authException: AuthorizationException?) {
        Log.d(TAG, "Token request complete")
        mAuthState!!.update(tokenResponse, authException)
        if (tokenResponse != null) {
            saveAuthState(mAuthState!!)
            saveTokens(tokenResponse.idToken!!, tokenResponse.refreshToken!!)
            saveTokensValidity(tokenResponse.additionalParameters)
        }
        getSignInResponse()
    }

    private fun getSignInResponse() {
        restApi.getUser().setObserverThreadsAndSmartSubscribe({ e ->
            Log.e(TAG, "onError: $e")
            errorToast(R.string.something_went_wrong, e)
        }, { response ->
            when (response.code()) {
                HTTP_OK -> {
                    CurrentUser.saveUserLoginStatus(true)
                    val user = response.body()
                    if (user != null) {
                        CurrentUser.saveUser(user)
                        if (isNullOrEmpty(user.userPreferences)) {
                            startActivity(Intent(this@TokenActivity, SelectZoneActivity::class.java))
                        } else {
                            HomeActivity.launch(this@TokenActivity, false)
                        }
                    }
                    finish()
                }
                HTTP_NOT_FOUND -> errorToast(R.string.user_name_not_found, response)
                else -> {
                    errorToast(R.string.something_went_wrong, response)
                    Log.e(TAG, response.message())
                }
            }
        })
    }

    override fun onDestroy() {
        progressDialog?.cancel()
        mAuthService.dispose()
        super.onDestroy()
    }
}