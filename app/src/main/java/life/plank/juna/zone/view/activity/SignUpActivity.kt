package life.plank.juna.zone.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.SignUpModel
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.onTextChanged
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.HttpURLConnection
import java.util.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        private var TAG = SignUpActivity::class.java.canonicalName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        ZoneApplication.getApplication().uiComponent.inject(this)

        initListeners()
    }

    private fun initListeners() {
        arrayOf(email_edit_text, password_edit_text, username_edit_text).onTextChanged {
            if (!email_edit_text.text.toString().trim { it <= ' ' }.isEmpty() && !password_edit_text.text.toString().trim { it <= ' ' }.isEmpty() &&
                    !username_edit_text.text.toString().trim { it <= ' ' }.isEmpty()) {
                sign_up.visibility = View.VISIBLE
            } else {
                sign_up.visibility = View.INVISIBLE
            }
        }

        sign_up.onDebouncingClick { signUp() }

        sign_in_card.onDebouncingClick {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun signUp() {
        val signUpModel = SignUpModel(UUID.randomUUID().toString(), username_edit_text.text.toString().trim { it <= ' ' },
                email_edit_text.text.toString().trim { it <= ' ' }, "USA", "Washington DC", "email", "Sam", "Jackson")
        restApi.createUser(signUpModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<SignUpModel>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError: $e")
                        errorToast(R.string.something_went_wrong, e)
                    }

                    override fun onNext(response: Response<SignUpModel>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_CREATED -> {
                                val intentSubmit = Intent(this@SignUpActivity, SignInActivity::class.java)
                                startActivity(intentSubmit)
                            }
                            HttpURLConnection.HTTP_FORBIDDEN -> errorToast(R.string.username_exists, response)
                            else -> {
                                errorToast(R.string.something_went_wrong, response)
                                Log.e(TAG, response.message())
                            }
                        }
                    }
                })
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}