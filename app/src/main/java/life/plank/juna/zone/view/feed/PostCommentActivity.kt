package life.plank.juna.zone.view.feed

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.leocardz.link.preview.library.*
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.content_post_comment.*
import life.plank.juna.zone.*
import life.plank.juna.zone.R.string.blue_color
import life.plank.juna.zone.data.api.*
import life.plank.juna.zone.data.model.feed.FeedItem
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.sharedpreference.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.base.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import rx.Subscription
import java.net.HttpURLConnection
import javax.inject.Inject

class PostCommentActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    private var commentBg: String = findString(blue_color)
    private var boardId: String? = null
    private var userId: String? = null
    internal lateinit var highlight: Drawable

    private lateinit var backgroundColorSwitches: Array<ImageView>
    private val textCrawler: TextCrawler = TextCrawler()
    private var isLinkPreviewLoading: Boolean = false
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

        backgroundColorSwitches = arrayOf(blue, purple, green, orange)

        commentReflectOnPostSurface()
        userId = CurrentUser.userId
        boardId = intent.getStringExtra(getString(R.string.intent_board_id))
        highlight = resources.getDrawable(R.drawable.highlight, null)
        setupSwipeGesture(this, drag_area, root_card, null)

        initViews()
    }

    private fun initViews() {
        blue.performClick()

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
                    val hasLink = containsLink()
                    if (hasLink && !isLinkPreviewLoading) {
                        isLinkPreviewLoading = true
                        textCrawler.beginPreview(
                                toString(),
                                { sourceContent, isNull ->
                                    if (sourceContent != null && sourceContent.isSuccess && !isNull && !isNullOrEmpty(sourceContent.images)) {
                                        loadLinkPreviewImage(sourceContent)
                                        setLinkPreviewViewsVisibility(true, sourceContent)
                                        link_title.text = sourceContent.title
                                        link_summary.text = sourceContent.description
                                    } else setLinkPreviewViewsVisibility(false)
                                }
                        )
                    } else if (!hasLink && isLink) setLinkPreviewViewsVisibility(false)
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
        link_thumbnail_shimmer.startShimmerAnimation()
        Glide.with(this@PostCommentActivity)
                .load(sourceContent.images[0])
                .apply(RequestOptions.placeholderOf(findDrawable(R.drawable.shimmer_rectangle))
                        .error(R.drawable.ic_place_holder))
                .into(object : DrawableImageViewTarget(link_thumbnail) {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        link_thumbnail_shimmer.stopShimmerAnimation()

                        val width = (getScreenSize(windowManager.defaultDisplay)[0] - getDp(20f)).toInt()
                        val params = link_thumbnail.layoutParams as RelativeLayout.LayoutParams
                        params.height = width * resource.intrinsicHeight / resource.intrinsicWidth
                        link_thumbnail.layoutParams = params
                        link_thumbnail.alpha = 1f

                        super.onResourceReady(resource, transition)
                    }
                })
    }

    private fun setLinkPreviewViewsVisibility(isVisible: Boolean, sourceContent: SourceContent? = null) {
        isLink = if (isVisible) {
            comment_edit_text.setTextColor(findColor(R.color.grey))
            comment_edit_text.highlightColor = findColor(R.color.colorAccent)
            post_btn.setTextColor(findColor(R.color.grey))
            post_btn.background = findDrawable(R.drawable.logout_button_bg)
            setColor("")

            backgroundColorSwitches.makeInvisible()
            link_preview_layout.makeVisible()
            comment_edit_text.requestFocus()
            this.sourceContent = sourceContent
            true
        } else {
            comment_edit_text.setTextColor(findColor(R.color.white))
            comment_edit_text.highlightColor = findColor(R.color.white_translucent_50)
            post_btn.setTextColor(findColor(R.color.white))
            post_btn.background = findDrawable(R.drawable.white_border_button)
            setColor(commentBg)

            backgroundColorSwitches.makeVisible()
            link_preview_layout.makeGone()
            this.sourceContent = null
            false
        }
        isLinkPreviewLoading = false
    }

    private fun postText() {
        if (comment_edit_text.text.toString().isEmpty() || comment_edit_text.text.toString() == getString(R.string.what_s_on_your_mind)) {
            customToast(R.string.please_enter_comment)
        } else {
            postCommentOnBoardFeed()
        }
    }

    private fun commentReflectOnPostSurface() {
        backgroundColorSwitches.run {
            onClick {
                for (view in this) {
                    if (view == it) {
                        view.background = highlight
                    } else
                        view.background = null
                    when (it) {
                        purple -> setColor(getString(R.string.purple_color))
                        green -> setColor(getString(R.string.green_color))
                        orange -> setColor(getString(R.string.orange_color))
                        else -> setColor(getString(R.string.blue_color))
                    }
                }
            }
        }
    }

    private fun setColor(drawableText: String) {
        if (!isNullOrEmpty(drawableText)) {
            card_parent_layout.setGradientBackground(drawableText)
            commentBg = drawableText
        } else {
            card_parent_layout.backgroundColor = findColor(R.color.white)
        }
    }

    private fun postCommentOnBoardFeed() {
        if (isNullOrEmpty(IdToken))
            return

        val feedItem = if (isLink) {
            sourceContent?.run { FeedItem(title, url, cannonicalUrl, description, comment_edit_text.text.toString(), images[0]) }
        } else {
            FeedItem(comment_edit_text.text.toString(), arrayListOf(commentBg))
        }
        feedItem?.run {
            if (isNullOrEmpty(boardId)) {
                if (isLink) {
                    postFeedItem()
                } else {
                    customToast(R.string.enter_valid_link)
                    return
                }
            } else {
                postFeedItem()
            }
            hideSoftKeyboard(comment_edit_text)
        }
    }

    private fun FeedItem.postFeedItem(): Subscription {
        return restApi.postFeedItemOnBoard(this, boardId, contentType)
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
