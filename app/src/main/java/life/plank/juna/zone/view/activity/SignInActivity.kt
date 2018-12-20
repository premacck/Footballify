package life.plank.juna.zone.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.common.AuthUtil
import life.plank.juna.zone.util.common.DataUtil.isValidEmail
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.onTextChanged
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.activity.home.HomeActivity
import net.openid.appauth.AuthorizationService

class SignInActivity : AppCompatActivity() {

    private var authService: AuthorizationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        ZoneApplication.getApplication().uiComponent.inject(this)
        PreferenceManager.CurrentUser.saveUserLoginStatus(false)
        authService = AuthorizationService(this)

        if (resources.getBoolean(R.bool.is_dev_environment)) {
            email_edit_text.setText(getString(R.string.azure_login_username))
            AuthUtil.loginOrRefreshToken(this, authService, null, false)
        }
        initListeners()
    }

    private fun initListeners() {
        arrayOf(email_edit_text, password_edit_text).onTextChanged { login.visibility = if (isValidEmail(email_edit_text.text)) View.VISIBLE else View.INVISIBLE }

        login.onDebouncingClick { AuthUtil.loginOrRefreshToken(this, authService, null, false) }

        forgot_password_text_view.onDebouncingClick { startActivity(Intent(this, AuthForgotPasswordActivity::class.java)) }

        sign_up_card.onDebouncingClick {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        skip_login.onDebouncingClick {
            PreferenceManager.CurrentUser.saveUserLoginStatus(false)
            startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
        }
    }

    override fun onDestroy() {
        authService?.dispose()
        super.onDestroy()
    }
}