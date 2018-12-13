package life.plank.juna.zone.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import kotlinx.android.synthetic.main.content_post_comment.*
import life.plank.juna.zone.R
import life.plank.juna.zone.R.string.blue_color
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.ROOT_COMMENT
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.containsLink
import life.plank.juna.zone.util.common.customToast
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.beginPreview
import life.plank.juna.zone.util.facilis.makeGone
import life.plank.juna.zone.util.facilis.makeVisible
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import java.net.HttpURLConnection
import javax.inject.Inject

class PostCommentActivity : BaseCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    private var commentBg = findString(blue_color)
    private var boardId: String? = null
    private var userId: String? = null
    internal lateinit var highlight: Drawable
    private val textCrawler: TextCrawler = TextCrawler()
    private var isLink: Boolean = false
    private var sourceContent: SourceContent? = null

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

        initViews()
    }

    private fun initViews() {
        setColor(R.drawable.blue_gradient, getString(R.string.blue_color))

        comment_edit_text.imeOptions = EditorInfo.IME_ACTION_DONE
        comment_edit_text.setRawInputType(InputType.TYPE_CLASS_TEXT)

        comment_edit_text.requestFocus()

        initListeners()
    }

    private fun initListeners() {
        post_btn.onDebouncingClick { postText() }

        comment_edit_text.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                charSequence?.run {
                    if (containsLink()) {
                        textCrawler.beginPreview(
                                toString(),
                                { sourceContent, isNull ->
                                    if (sourceContent != null && sourceContent.isSuccess && !isNull && !isNullOrEmpty(sourceContent.images)) {
                                        loadLinkPreviewImage(sourceContent)
                                        setLinkPreviewViewsVisibility(true, sourceContent)
                                        link_title.text = sourceContent.title
                                        link_description.text = sourceContent.description
                                    } else setLinkPreviewViewsVisibility(false)
                                }
                        )
                    } else setLinkPreviewViewsVisibility(false)
                }
            }
        }
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

    private fun loadLinkPreviewImage(sourceContent: SourceContent) {
        Glide.with(this@PostCommentActivity)
                .asBitmap()
                .load(sourceContent.images[0])
                .apply(RequestOptions.placeholderOf(findDrawable(R.drawable.shimmer_rectangle))
                        .error(R.drawable.ic_place_holder))
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        link_thumbnail.setImageResource(R.drawable.shimmer_rectangle)
                        link_thumbnail.alpha = 0.5f
                        return true
                    }

                    override fun onResourceReady(bitmap: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        val width = (UIDisplayUtil.getScreenSize(windowManager.defaultDisplay)[0] - UIDisplayUtil.getDp(8f)).toInt()
                        val params = link_thumbnail.layoutParams as LinearLayout.LayoutParams
                        bitmap?.run {
                            params.height = width * height / width
                            link_thumbnail.layoutParams = params
                            link_thumbnail.setImageBitmap(this)
                            link_thumbnail.alpha = 1f
                            return true
                        } ?: return false
                    }
                }).into(link_thumbnail)
    }

    private fun setLinkPreviewViewsVisibility(isVisible: Boolean, sourceContent: SourceContent? = null) {
        isLink = if (isVisible) {
            link_thumbnail.makeVisible()
            link_title.makeVisible()
            link_description.makeVisible()
            this.sourceContent = sourceContent
            true
        } else {
            link_thumbnail.makeGone()
            link_title.makeGone()
            link_description.makeGone()
            this.sourceContent = null
            false
        }
    }

    private fun postText() {
        if (comment_edit_text.text.toString().isEmpty() || comment_edit_text.text.toString() == getString(R.string.what_s_on_your_mind)) {
            customToast(R.string.please_enter_comment)
        } else {
            hideSoftKeyboard(comment_edit_text)
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
        card_parent_layout.background = resources.getDrawable(drawable, null)
    }

    private fun postCommentOnBoardFeed(getEditTextValue: String, boardId: String?, userId: String?, dateCreated: String?) {
        if (isNullOrEmpty(getToken()))
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

    override fun onDestroy() {
        textCrawler.cancel()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.float_down, R.anim.sink_down)
    }
}
