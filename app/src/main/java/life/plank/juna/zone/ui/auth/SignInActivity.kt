package life.plank.juna.zone.ui.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.prembros.facilis.util.onDebouncingClick
import com.prembros.facilis.util.onTextChanged
import kotlinx.android.synthetic.main.activity_sign_in.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.service.AuthService
import life.plank.juna.zone.service.CommonDataService.isValidEmail
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.home.HomeActivity
import net.openid.appauth.AuthorizationService
import org.jetbrains.anko.intentFor

class SignInActivity : AppCompatActivity() {

    private var authService: AuthorizationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ZoneApplication.application.uiComponent.inject(this)
        CurrentUser.saveUserLoginStatus(false)
        authService = AuthorizationService(this)

        if (resources.getBoolean(R.bool.is_dev_environment)) {
            email_edit_text.setText(getString(R.string.azure_login_username))
            AuthService.loginOrRefreshToken(this, authService, null, false)
        }
        initListeners()
    }

    private fun initListeners() {
        arrayOf(email_edit_text, password_edit_text).onTextChanged { login.visibility = if (isValidEmail(email_edit_text.text)) View.VISIBLE else View.INVISIBLE }

        login.onDebouncingClick { AuthService.loginOrRefreshToken(this, authService, null, false) }

        forgot_password_text_view.onDebouncingClick { startActivity(intentFor<AuthForgotPasswordActivity>()) }

        sign_up_card.onDebouncingClick {
            startActivity(intentFor<SignUpActivity>())
            finish()
        }

        skip_login.onDebouncingClick {
            CurrentUser.saveUserLoginStatus(false)
            startActivity(intentFor<HomeActivity>())
        }
    }

    override fun onDestroy() {
        authService?.dispose()
        super.onDestroy()
    }
}