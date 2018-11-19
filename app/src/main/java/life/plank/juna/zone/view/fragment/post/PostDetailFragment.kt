package life.plank.juna.zone.view.fragment.post

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.BottomSheetBehavior
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.shimmer_comments.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.ZoneApplication.getApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.model.FeedItemComment
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.*
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.UIDisplayUtil.*
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.showFor
import life.plank.juna.zone.view.adapter.EmojiAdapter
import life.plank.juna.zone.view.adapter.post.PostCommentAdapter
import life.plank.juna.zone.view.fragment.base.BaseCommentContainerFragment
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.HttpURLConnection.*
import javax.inject.Inject

class PostDetailFragment : BaseCommentContainerFragment() {

    @Inject
    lateinit var restApi: RestApi

    lateinit var feedEntry: FeedEntry
    lateinit var boardId: String
    private var adapter: PostCommentAdapter? = null
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null

    companion object {
        private val TAG = PostDetailFragment::class.java.simpleName
        fun newInstance(feedEntry: FeedEntry, boardId: String) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ZoneApplication.getContext().getString(R.string.intent_feed_items), feedEntry)
                putString(ZoneApplication.getContext().getString(R.string.intent_board_id), boardId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getApplication().uiComponent.inject(this)
        arguments?.run {
            feedEntry = getParcelable(getString(R.string.intent_feed_items))!!
            boardId = getString(getString(R.string.intent_board_id))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_post_detail, container, false)

    private fun initBottomSheetRecyclerView() {
        emojiAdapter = EmojiAdapter(restApi, boardId, emojiBottomSheetBehavior)
        emoji_recycler_view.adapter = emojiAdapter
        EmojiAdapter.feedId = feedEntry.feedItem.id
    }

    private fun setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior!!.peekHeight = 0
        emojiBottomSheetBehavior!!.isHideable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PostCommentAdapter(Glide.with(this), this, "")
        post_comments_list.adapter = adapter

        setupBottomSheet()
        initBottomSheetRecyclerView()

        bindFeetContent()
        getCommentsOnFeed(false)

        initListeners()
    }

    private fun initListeners() {
        swipe_refresh_layout.setOnRefreshListener { getCommentsOnFeed(true) }

        post_comment.onDebouncingClick {
            if (comment_edit_text.text.toString().isEmpty()) {
                comment_edit_text.setError(getString(R.string.please_enter_comment), resources.getDrawable(R.drawable.ic_error, null))
            } else {
                comment_edit_text.clearFocus()
                postCommentOrReply(comment_edit_text.text.toString(), getCommentEventForFeedItemComment(boardId, feedEntry.feedItem.id!!), false, feedEntry.feedItem.id)
            }
        }
        pin_image_view.onDebouncingClick {
            if (feedEntry.feedInteractions.hasPinned) {
                unpinItem()
            } else {
                pinItem()
            }
        }
        reaction_view.onDebouncingClick {
            emojiBottomSheetBehavior?.showFor(feedEntry.feedItem.id)
        }
        no_comment_text_view.onDebouncingClick {
            if (no_comment_text_view.text.toString() == getString(R.string.failed_to_get_feed_comments)) getCommentsOnFeed(false)
        }
    }

    override fun onResume() {
        super.onResume()
        feed_title_text_view.isSelected = true
    }

    private fun bindFeetContent() {
        val mediaPlayer = MediaPlayer()

        if (feedEntry.feedItem.user != null) {
            Glide.with(this)
                    .load(feedEntry.feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerInsideTransform().override(getDp(20f).toInt(), getDp(20f).toInt()))
                    .into(profile_pic)
        }

        if (feedEntry.feedItem.user != null) {
            user_name_text_view.text = feedEntry.feedItem.user!!.displayName
        } else {
            val userEmailId = PreferenceManager.CurrentUser.getUserEmail()
            user_name_text_view.text = userEmailId
        }
        feed_title_text_view.text = if (feedEntry.feedItem.contentType != ROOT_COMMENT) feedEntry.feedItem.title else null
        if (feedEntry.feedItem.contentType == NEWS) {
            description_text_view.visibility = VISIBLE
            description_text_view.movementMethod = LinkMovementMethod.getInstance()
            val stringBuilder = SpannableStringBuilder()
                    .append(feedEntry.feedItem.source?.bold())
                    .append("\n\n")
                    .append(feedEntry.feedItem.summary)
                    .append("\n\n")
                    .append(feedEntry.feedItem.url?.toClickableWebLink(activity!!))
            description_text_view.text = stringBuilder
        } else {
            description_text_view.visibility = if (feedEntry.feedItem.description == null) GONE else VISIBLE
            description_text_view.text = feedEntry.feedItem.description
        }

        feedEntry.feedInteractions
        pin_image_view.setImageResource(
                if (feedEntry.feedInteractions.hasPinned)
                    R.drawable.ic_pin_active
                else
                    R.drawable.ic_pin_inactive
        )

        when (feedEntry.feedItem.contentType) {
            NEWS, IMAGE -> {
                mediaPlayer.stop()
                setVisibilities(VISIBLE, GONE, GONE)
                try {
                    val urlToLoad = if (feedEntry.feedItem.contentType == NEWS)
                        feedEntry.feedItem.thumbnail!!.imageUrl
                    else
                        feedEntry.feedItem.url
                    feed_content.startShimmerAnimation()
                    Glide.with(this).asBitmap().load(urlToLoad)
                            .apply(RequestOptions.errorOf(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder))
                            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                                    if (activity == null) {
                                        return
                                    }
                                    val width = (getScreenSize(activity!!.windowManager.defaultDisplay)[0] - getDp(8f)).toInt()
                                    val params = feed_image_view.layoutParams as RelativeLayout.LayoutParams
                                    params.height = width * bitmap.height / bitmap.width
                                    feed_image_view.layoutParams = params
                                    feed_content.stopShimmerAnimation()
                                    feed_image_view.setImageBitmap(bitmap)
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    feed_content.stopShimmerAnimation()
                                    feed_image_view.setImageDrawable(errorDrawable)
                                }
                            })
                } catch (e: Exception) {
                    feed_image_view.setImageResource(R.drawable.ic_place_holder)
                }

            }
            AUDIO -> {
                mediaPlayer.stop()
                setVisibilities(VISIBLE, GONE, GONE)
                feed_image_view.setImageResource(R.drawable.ic_mic_white)

                val audioUriString = feedEntry.feedItem.url
                val audioUri = Uri.parse(audioUriString)

                mediaPlayer.setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                try {
                    mediaPlayer.setDataSource(ZoneApplication.getContext(), audioUri)
                    mediaPlayer.prepare()
                } catch (e: IOException) {
                    mediaPlayer.stop()
                }

                mediaPlayer.start()

                try {
                    Picasso.with(activity).load(feedEntry.feedItem.url)
                            .error(R.drawable.ic_place_holder)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(feed_image_view)
                } catch (e: Exception) {
                    feed_image_view.setImageResource(R.drawable.ic_place_holder)
                }

            }
            VIDEO -> {
                mediaPlayer.stop()
                setVisibilities(GONE, VISIBLE, GONE)
                val videoUriString = feedEntry.feedItem.url
                val videoUri = Uri.parse(videoUriString)
                captured_video_view.setVideoURI(videoUri)
                captured_video_view.start()
            }
            ROOT_COMMENT -> {
                mediaPlayer.stop()
                setVisibilities(GONE, GONE, VISIBLE)
                val comment = feedEntry.feedItem.title!!.replace("^\"|\"$".toRegex(), "")

                feed_text_view.background = getCommentColor(comment)
                feed_text_view.text = getCommentText(comment)
            }
        }
    }

    private fun pinItem() {
        restApi.pinFeedItem(feedEntry.feedItem.id, BOARD, boardId, getRequestDateStringOfNow(), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "pinItem() ", it)
            errorToast(R.string.failed_to_pin_feed, it)
        }, {
            when (it.code()) {
                HTTP_OK, HTTP_CREATED -> {
                    feedEntry.feedInteractions.hasPinned = false
                    feedEntry.feedInteractions.pinId = it.body()
                    pin_image_view.setImageResource(R.drawable.ic_pin_active)
                }
                HTTP_NOT_FOUND -> errorToast(R.string.failed_to_find_feed, it)
                HTTP_INTERNAL_ERROR -> errorToast(R.string.already_pinned_feed, it)
                else -> errorToast(R.string.failed_to_pin_feed, it)
            }
        })
    }

    private fun unpinItem() {
        if (feedEntry.feedInteractions.pinId == null) {
            feedEntry.feedInteractions.hasPinned = false
            pin_image_view.setImageResource(R.drawable.ic_pin_inactive)
            return
        }
        restApi.unpinFeedItem(boardId, feedEntry.feedInteractions.pinId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e(TAG, "unpinItem() ", it)
            errorToast(R.string.failed_to_unpin_feed, it)
        }, {
            when (it.code()) {
                HTTP_OK, HTTP_NO_CONTENT -> {
                    feedEntry.feedInteractions.hasPinned = false
                    feedEntry.feedInteractions.pinId = null
                    pin_image_view.setImageResource(R.drawable.ic_pin_inactive)
                }
                HTTP_NOT_FOUND -> errorToast(R.string.failed_to_find_feed, it)
                HTTP_INTERNAL_ERROR -> errorToast(R.string.already_removed_pin, it)
                else -> errorToast(R.string.failed_to_unpin_feed, it)
            }
        })
    }

    private fun setVisibilities(imageViewVisibility: Int, videoViewVisibility: Int, textViewVisibility: Int) {
        feed_image_view.visibility = imageViewVisibility
        captured_video_view.visibility = videoViewVisibility
        feed_text_view.visibility = textViewVisibility
    }

    private fun getCommentsOnFeed(isRefreshing: Boolean) {
        if (isNullOrEmpty(getToken())) {
            updateCommentsUi(GONE, GONE, VISIBLE, R.string.login_signup_to_view_comments)
            post_comment_layout.visibility = GONE
        }

        restApi.getCommentsForFeed(feedEntry.feedItem.id, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (isRefreshing) swipe_refresh_layout.isRefreshing = true
                    updateCommentsUi(VISIBLE, GONE, GONE, 0)
                }
                .doOnTerminate { if (isRefreshing) swipe_refresh_layout.isRefreshing = false }
                .subscribe(object : Subscriber<Response<List<FeedItemComment>>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted : getCommentsForFeed($feedEntry)")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message)
                        updateCommentsUi(GONE, GONE, VISIBLE, R.string.failed_to_get_feed_comments)
                    }

                    override fun onNext(response: Response<List<FeedItemComment>>) {
                        when (response.code()) {
                            HTTP_OK -> {
                                updateCommentsUi(GONE, VISIBLE, GONE, 0)
                                val commentList = response.body()
                                feedEntry.feedItem.comments = commentList
                                adapter!!.setComments(commentList)
                            }
                            HTTP_NOT_FOUND -> updateCommentsUi(GONE, GONE, VISIBLE, R.string.be_the_first_to_comment_on_this_post)
                            else -> updateCommentsUi(GONE, GONE, VISIBLE, R.string.failed_to_get_feed_comments)
                        }
                    }
                })
    }

    private fun updateCommentsUi(shimmerVisibility: Int, recyclerViewVisibility: Int, errorMessageVisibility: Int, @StringRes message: Int) {
        comments_shimmer.visibility = shimmerVisibility
        post_comments_list.visibility = recyclerViewVisibility
        no_comment_text_view.visibility = errorMessageVisibility
        if (errorMessageVisibility == VISIBLE) {
            no_comment_text_view.setText(message)
        }
        if (shimmerVisibility == VISIBLE) {
            comments_shimmer.startShimmerAnimation()
        } else {
            comments_shimmer.stopShimmerAnimation()
        }
    }

    override fun specifyCommentEvent() {
        commentEvent = getCommentEventForBoardComment(boardId)
    }

    override fun getTheRestApi(): RestApi = restApi

    override fun getCommentEditText(): EditText = comment_edit_text

    override fun onPostReplyOnComment(reply: String, position: Int, parentComment: FeedItemComment) =
            postReplyOnFeedItemComment(reply, getCommentEventForReply(boardId, parentComment.id), parentComment, position)

    override fun onCommentSuccessful(responseComment: FeedItemComment) {
        comment_edit_text.text = SpannableStringBuilder("")
        hideSoftKeyboard(comment_edit_text)
        adapter?.addComment(responseComment)
        post_comments_list.smoothScrollToPosition(0)
    }

    override fun onReplySuccessful(responseReply: FeedItemComment, parentComment: FeedItemComment?, parentCommentPosition: Int, replyPosition: Int) {
        parentComment?.run {
            if (isNullOrEmpty(replies)) {
                replies = ArrayList()
            }
            (replies as ArrayList).add(replyPosition, responseReply)
            adapter?.onReplyPostedOnComment(parentCommentPosition, this)
        }
    }
}