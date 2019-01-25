package life.plank.juna.zone.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.activity_splash_screen.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.service.AuthUtil
import life.plank.juna.zone.sharedpreference.*
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.ui.auth.SignInActivity
import life.plank.juna.zone.ui.home.HomeActivity
import life.plank.juna.zone.ui.zone.SelectZoneActivity
import net.openid.appauth.AuthorizationService
import org.jetbrains.anko.intentFor
import java.net.HttpURLConnection
import javax.inject.Inject

/**
 * The launcher activity. This activity is launched when starting the app from drawer.
 */
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
            startActivity(intentFor<SignInActivity>())
            finish()
        }
    }

    /* todo: doc: */
    private fun getUserPreference() {
        restApi.getUser().setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "onError: $it")
        }, {
            when (it.code()) {
                HttpURLConnection.HTTP_OK -> {
                    val user = it.body()
                    if (user != null) {
                        CurrentUser.saveUser(user)
                        if (isNullOrEmpty(user.userPreferences)) {
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