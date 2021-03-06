package life.plank.juna.zone.component.helper

import android.app.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.fragment.app.Fragment
import life.plank.juna.zone.R
import life.plank.juna.zone.service.AuthService
import net.openid.appauth.AuthorizationService

fun Fragment.showSignupPopup(authService: AuthorizationService) = activity?.showSignupPopup(authService)

fun Activity.showSignupPopup(authService: AuthorizationService) {
    val signUpDialog = Dialog(this).apply {
        setContentView(R.layout.signup_dialogue)
        findViewById<View>(R.id.drag_handle).setOnClickListener { dismiss() }
        findViewById<View>(R.id.signup_button).setOnClickListener { AuthService.loginOrRefreshToken(this@showSignupPopup, authService, null, false) }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    signUpDialog.show()
}