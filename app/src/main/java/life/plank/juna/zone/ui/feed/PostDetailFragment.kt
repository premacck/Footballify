package life.plank.juna.zone.ui.feed

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import android.view.View.*
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.prembros.facilis.util.isNullOrEmpty
import com.prembros.facilis.util.onDebouncingClick
import kotlinx.android.synthetic.main.emoji_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_post_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.component.helper.showSignupPopup
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.data.model.board.Emoji
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.sharedpreference.IdToken
import life.plank.juna.zone.ui.base.fragment.BaseJunaCardChild
import life.plank.juna.zone.ui.base.setRootCommentPost
import life.plank.juna.zone.ui.base.showFor
import life.plank.juna.zone.ui.emoji.EmojiAdapter
import life.plank.juna.zone.ui.emoji.EmojiContainer
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.time.DateUtil.*
import life.plank.juna.zone.util.toro.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import net.openid.appauth.AuthorizationService
import java.io.IOException
import java.net.HttpURLConnection.*
import javax.inject.Inject

class PostDetailFragment : BaseJunaCardChild(), EmojiContainer {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var feedEntry: FeedEntry
    lateinit var boardId: String
    private var emojiBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var emojiAdapter: EmojiAdapter? = null
    private var authService: AuthorizationService? = null
    private var exoPlayer: SimpleExoPlayer? = null

    companion object {
        private val TAG = PostDetailFragment::class.java.simpleName
        fun newInstance(feedEntry: FeedEntry, boardId: String) = PostDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ZoneApplication.appContext.getString(R.string.intent_feed_items), feedEntry)
                putString(ZoneApplication.appContext.getString(R.string.intent_board_id), boardId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
        arguments?.run {
            feedEntry = getParcelable(getString(R.string.intent_feed_items))!!
            boardId = getString(getString(R.string.intent_board_id))!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_post_detail, container, false)

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

    private fun initBottomSheetRecyclerView() {
        emojiAdapter = EmojiAdapter(restApi, boardId, emojiBottomSheetBehavior, feedEntry.feedItem.id, true, this)
        emoji_recycler_view.adapter = emojiAdapter
    }

    private fun setupBottomSheet() {
        emojiBottomSheetBehavior = BottomSheetBehavior.from(emoji_bottom_sheet)
        emojiBottomSheetBehavior?.peekHeight = 0
        emojiBottomSheetBehavior?.isHideable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
        initBottomSheetRecyclerView()

        bindFeetContent()

        initListeners()
    }

    private fun initListeners() {
        pin_image_view.onDebouncingClick {
            if (feedEntry.feedInteractions.hasPinned) {
                unpinItem()
            } else {
                pinItem()
            }
        }
        reaction_view.onDebouncingClick {
            if (isNullOrEmpty(IdToken)) {
                showPopup()
            } else {
                emojiBottomSheetBehavior?.showFor(emojiAdapter!!, feedEntry.feedItem.id)
            }
        }
    }

    private fun showPopup() {
        authService = AuthorizationService(context!!)
        showSignupPopup(authService!!)
    }

    override fun onResume() {
        super.onResume()
        feed_title_text_view.isSelected = true
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
        if (SDK_INT <= 23) releaseExoPlayer()
    }

    override fun onStop() {
        super.onStop()
        if (SDK_INT > 23) releaseExoPlayer()
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun bindFeetContent() {
        if (feedEntry.feedItem.user != null) {
            Glide.with(this)
                    .load(feedEntry.feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerInsideTransform().override(getDp(20f).toInt(), getDp(20f).toInt())
                            .error(R.drawable.ic_football))
                    .into(profile_pic)
        } else
            profile_pic.setImageResource(R.drawable.ic_football)

        creation_date.text = getCommentDateAndTimeFormat(feedEntry.feedItem.dateCreated)
        reaction_count.text = feedEntry.feedItem.interactions.emojiReacts.toString()
        feed_title_text_view.text = if (feedEntry.feedItem.contentType != ROOT_COMMENT) feedEntry.feedItem.title else null
        if (feedEntry.feedItem.contentType == NEWS) {
            if (feedEntry.feedItem.user != null) {
                user_name_text_view.text = feedEntry.feedItem.user?.displayName
            } else {
                user_name_text_view.setText(R.string.juna_user_topic)
            }
            description_text_view.visibility = VISIBLE
            description_text_view.movementMethod = LinkMovementMethod.getInstance()
            val stringBuilder = SpannableStringBuilder()
                    .append(feedEntry.feedItem.source?.bold())
                    .append(NEW_LINE, NEW_LINE)
                    .append(feedEntry.feedItem.summary)
                    .append(NEW_LINE, NEW_LINE)
                    .append(feedEntry.feedItem.description?.formatLinks(activity)
                            ?: feedEntry.feedItem.url?.toClickableWebLink(activity))
            description_text_view.text = stringBuilder
        } else {
            if (feedEntry.feedItem.user != null) {
                user_name_text_view.text = feedEntry.feedItem.user?.displayName
            } else {
                user_name_text_view.visibility = View.INVISIBLE
            }
            description_text_view.visibility = if (feedEntry.feedItem.description == null) GONE else VISIBLE
            description_text_view.text = feedEntry.feedItem.description
        }

        pin_image_view.setImageResource(
                if (feedEntry.feedInteractions.hasPinned)
                    R.drawable.ic_pin_active
                else
                    R.drawable.ic_pin_inactive
        )

        when (feedEntry.feedItem.contentType) {
            NEWS, IMAGE -> {
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
                val mediaPlayer = MediaPlayer()
                setVisibilities(VISIBLE, GONE, GONE)
                feed_image_view.setImageResource(R.drawable.ic_mic_white)

                val audioUriString = feedEntry.feedItem.url
                val audioUri = Uri.parse(audioUriString)

                mediaPlayer.setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                try {
                    mediaPlayer.setDataSource(ZoneApplication.appContext, audioUri)
                    mediaPlayer.prepare()
                } catch (e: IOException) {
                    mediaPlayer.stop()
                }

                mediaPlayer.start()

                try {
                    Glide.with(this)
                            .load(feedEntry.feedItem.url)
                            .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder)
                                    .error(R.drawable.ic_place_holder))
                            .into(feed_image_view)
                } catch (e: Exception) {
                    feed_image_view.setImageResource(R.drawable.ic_place_holder)
                }

            }
            VIDEO -> {
                setVisibilities(GONE, VISIBLE, GONE)
                val videoUriString = feedEntry.feedItem.url
                val videoUri = Uri.parse(videoUriString)

                exoPlayer = ExoBuilder
                        .with(this)
                        .withMediaSource(videoUri)
                        .withContainer(video_player_container)
                        .applyTo(video_player)
                        .addPlayPauseListener(video_player, play_pause, video_loading_progress)
            }
            ROOT_COMMENT -> {
                setVisibilities(GONE, GONE, VISIBLE)
                feed_text_view.setRootCommentPost(feedEntry.feedItem)
            }
        }
        feed_title_text_view.isSelected = true
    }

    private fun pinItem() {
        restApi.pinFeedItem(feedEntry.feedItem.id!!, BOARD, boardId, getRequestDateStringOfNow()).setObserverThreadsAndSmartSubscribe({
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
        }, this)
    }

    private fun unpinItem() {
        if (feedEntry.feedInteractions.pinId == null) {
            feedEntry.feedInteractions.hasPinned = false
            pin_image_view.setImageResource(R.drawable.ic_pin_inactive)
            return
        }
        restApi.unpinFeedItem(boardId, feedEntry.feedInteractions.pinId!!).setObserverThreadsAndSmartSubscribe({
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
        }, this)
    }

    private fun setVisibilities(imageViewVisibility: Int, videoViewVisibility: Int, textViewVisibility: Int) {
        feed_image_view.visibility = imageViewVisibility
        video_player_container.visibility = videoViewVisibility
        feed_text_view.visibility = textViewVisibility
    }

    override fun onEmojiPosted(emoji: Emoji) {}
}