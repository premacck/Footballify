package life.plank.juna.zone.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.activity_splash_screen.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.service.AuthService
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.sharedpreference.checkTokenValidity
import life.plank.juna.zone.ui.auth.SignInActivity
import life.plank.juna.zone.ui.home.HomeActivity
import life.plank.juna.zone.ui.zone.SelectZoneActivity
import life.plank.juna.zone.util.common.errorToast
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
        ZoneApplication.application.uiComponent.inject(this)

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
                AuthService.loginOrRefreshToken(this, authService, null, true)
            }
        } else {
            startActivity(intentFor<SignInActivity>())
            finish()
        }
    }

    /**
     * Function to get the user preferences when ID and refresh tokens are both valid.
     * * In case of a successful response, if user doesn't have any preferences saved, launch [SelectZoneActivity], else launch [HomeActivity]
     */
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
                            startActivity(intentFor<SelectZoneActivity>())
                        } else {
                            startActivity(intentFor<HomeActivity>())
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