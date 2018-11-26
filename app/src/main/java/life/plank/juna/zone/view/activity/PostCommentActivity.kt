package life.plank.juna.zone.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.content_post_comment.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.AppConstants.ROOT_COMMENT
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.customToast
import life.plank.juna.zone.util.errorToast
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PostCommentActivity : AppCompatActivity() {
    internal var TAG = PostCommentActivity::class.java.simpleName

    @Inject
    lateinit var restApi: RestApi

    private var commentBg = "blue_bg"
    private var boardId: String? = null
    private var userId: String? = null
    private var date: String? = null
    internal lateinit var highlight: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_post_comment)

        (application as ZoneApplication).uiComponent.inject(this)

        commentReflectOnPostSurface()
        date = SimpleDateFormat(getString(R.string.string_format)).format(Calendar.getInstance().time)
        userId = PreferenceManager.CurrentUser.getUserId()
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        highlight = resources.getDrawable(R.drawable.highlight)

        comment_edit_text.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    postText()
                    true
                }
                else -> false
            }
        }
    }

    private fun postText() {
        if (comment_edit_text.text.toString().isEmpty()) {
            customToast(R.string.please_enter_comment)
        } else {
            postCommentOnBoardFeed(commentBg + "$" + comment_edit_text.text.toString(), boardId, userId, date)
        }
    }

    private fun commentReflectOnPostSurface() {

        blue.setOnClickListener { v ->
            blue.background = highlight
            purple.background = null
            green.background = null
            orange.background = null
            setColor(R.drawable.blue_gradient, getString(R.string.blue_color))
        }
        purple.setOnClickListener { v ->
            blue.background = null
            purple.background = highlight
            green.background = null
            orange.background = null
            setColor(R.drawable.purple_gradient, getString(R.string.purple_color))
        }
        green.setOnClickListener { v ->
            blue.background = null
            purple.background = null
            green.background = highlight
            orange.background = null
            setColor(R.drawable.green_gradient, getString(R.string.green_color))
        }
        orange.setOnClickListener { v ->
            blue.background = null
            purple.background = null
            green.background = null
            orange.background = highlight
            setColor(R.drawable.orange_gradient, getString(R.string.orange_color))
        }

    }

    private fun setColor(drawable: Int, drawableText: String) {
        commentBg = drawableText
        card_relative_layout.background = resources.getDrawable(drawable, null)
    }

    private fun postCommentOnBoardFeed(getEditTextValue: String, boardId: String?, userId: String?, dateCreated: String?) {
        restApi.postFeedItemOnBoard(getEditTextValue, boardId, ROOT_COMMENT, userId, dateCreated, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<JsonObject>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted: ")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError: $e")
                        errorToast(R.string.something_went_wrong, e)
                    }

                    override fun onNext(jsonObjectResponse: Response<JsonObject>) {

                        when (jsonObjectResponse.code()) {
                            HttpURLConnection.HTTP_OK -> {
                                customToast(R.string.comment_posted_successfully)
                                finish()
                            }
                            HttpURLConnection.HTTP_BAD_REQUEST -> errorToast(R.string.enter_text_to_be_posted, jsonObjectResponse)
                            else -> errorToast(R.string.could_not_post_comment, jsonObjectResponse)
                        }
                    }
                })
    }

    companion object {

        fun launch(packageContext: Activity, boardId: String) {
            val intent = Intent(packageContext, PostCommentActivity::class.java)
            intent.putExtra(packageContext.getString(R.string.intent_board_id), boardId)
            packageContext.startActivity(intent)
        }
    }
}
