package life.plank.juna.zone.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.content_post_comment.*
import life.plank.juna.zone.R
import life.plank.juna.zone.R.string.blue_color
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.*
import life.plank.juna.zone.util.AppConstants.ROOT_COMMENT
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture
import life.plank.juna.zone.util.UIDisplayUtil.showSoftKeyboard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import java.net.HttpURLConnection
import javax.inject.Inject

class PostCommentActivity : BaseCardActivity() {
    @Inject
    lateinit var restApi: RestApi

    private var commentBg = findString(blue_color)
    private var boardId: String? = null
    private var userId: String? = null
    internal lateinit var highlight: Drawable

    companion object {
        private val TAG = PostCommentActivity::class.java.simpleName

        fun launch(packageContext: Activity, boardId: String) {
            val intent = Intent(packageContext, PostCommentActivity::class.java)
            intent.putExtra(packageContext.getString(R.string.intent_board_id), boardId)
            packageContext.startActivity(intent)
            packageContext.overridePendingTransition(R.anim.float_up, R.anim.sink_up)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_post_comment)

        (application as ZoneApplication).uiComponent.inject(this)

        commentReflectOnPostSurface()
        userId = PreferenceManager.CurrentUser.getUserId()
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        highlight = resources.getDrawable(R.drawable.highlight, null)
        setupSwipeGesture(this, drag_area, root_card, null)

        comment_edit_text.requestFocus()
        showSoftKeyboard(comment_edit_text)

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
            postCommentOnBoardFeed(commentBg + "$" + comment_edit_text.text.toString(), boardId, userId, getRequestDateStringOfNow())
        }
    }

    private fun commentReflectOnPostSurface() {

        blue.setOnClickListener {
            blue.background = highlight
            purple.background = null
            green.background = null
            orange.background = null
            setColor(R.drawable.blue_gradient, getString(R.string.blue_color))
        }
        purple.setOnClickListener {
            blue.background = null
            purple.background = highlight
            green.background = null
            orange.background = null
            setColor(R.drawable.purple_gradient, getString(R.string.purple_color))
        }
        green.setOnClickListener {
            blue.background = null
            purple.background = null
            green.background = highlight
            orange.background = null
            setColor(R.drawable.green_gradient, getString(R.string.green_color))
        }
        orange.setOnClickListener {
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
        if (DataUtil.isNullOrEmpty(PreferenceManager.Auth.getToken()))
            return
        restApi.postFeedItemOnBoard(getEditTextValue, boardId, ROOT_COMMENT, userId, dateCreated, getToken())
                .setObserverThreadsAndSmartSubscribe({
                    Log.e(TAG, "Post Text: ", it)
                }, {
                    when (it.code()) {
                        HttpURLConnection.HTTP_OK -> {
                            customToast(R.string.comment_posted_successfully)
                            finish()
                        }
                        HttpURLConnection.HTTP_BAD_REQUEST -> errorToast(R.string.enter_text_to_be_posted, it)
                        else -> errorToast(R.string.could_not_post_comment, it)
                    }
                })
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container

    override fun restApi(): RestApi? = restApi

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.float_down, R.anim.sink_down)
    }

}
