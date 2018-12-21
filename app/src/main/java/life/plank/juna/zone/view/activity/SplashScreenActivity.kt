package life.plank.juna.zone.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AuthUtil
import life.plank.juna.zone.util.common.DataUtil
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.checkTokenValidity
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.view.activity.home.HomeActivity
import net.openid.appauth.AuthorizationService
import java.net.HttpURLConnection
import javax.inject.Inject

class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var authService: AuthorizationService

    companion object {
        private val TAG = SplashScreenActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ZoneApplication.getApplication().uiComponent.inject(this)

        authService = AuthorizationService(this)
        animation_view.setSpeed(3.0f)
    }

    override fun onStart() {
        super.onStart()
        proceedToApp()
    }

    /**
     * Flow:
     * * check if the refresh token is valid
     * * if the refresh token is valid, check if ID token is valid
     * * if Id token is valid, proceed to [HomeActivity]
     * * if Id token is not valid, try to refresh the token
     * * if the refresh token is not valid, go to [SignInActivity]
     */
    private fun proceedToApp() {
        if (checkTokenValidity(R.string.pref_refresh_token_validity)) {
            if (checkTokenValidity(R.string.pref_id_token_validity)) {
                getUserPreference()
            } else {
                AuthUtil.loginOrRefreshToken(this, authService, null, true)
            }
        } else {
            startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
            finish()
        }
    }

    private fun getUserPreference() {
        restApi.getUser(getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: $it")
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val user = it.body()
                    if (user != null) {
                        PreferenceManager.CurrentUser.saveUser(user)
                        if (DataUtil.isNullOrEmpty<UserPreference>(user.userPreferences)) {
                            startActivity(Intent(this@SplashScreenActivity, SelectZoneActivity::class.java))
                        } else {
                            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                        }
                    }
                    finish()
                }
                HttpURLConnection.HTTP_NOT_FOUND -> errorToast(R.string.user_name_not_found, it)
                else -> {
                    errorToast(R.string.something_went_wrong, it)
                    Log.e(TAG, it.message())
                }
            }
        })
    }

    override fun onDestroy() {
        authService.dispose()
        super.onDestroy()
    }
}