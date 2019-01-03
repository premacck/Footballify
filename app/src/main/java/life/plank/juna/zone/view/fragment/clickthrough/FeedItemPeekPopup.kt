package life.plank.juna.zone.view.fragment.clickthrough

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.*
import android.net.Uri
import android.os.*
import android.view.*
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.SimpleExoPlayer
import com.prembros.facilis.dialog.BaseBlurPopup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.football_feed_detail_row.*
import kotlinx.android.synthetic.main.layout_interaction_component.*
import kotlinx.android.synthetic.main.popup_feed_item_peek.*
import life.plank.juna.zone.*
import life.plank.juna.zone.data.model.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.EmojiContainer
import life.plank.juna.zone.util.common.AppConstants
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.JunaDataUtil.findString
import life.plank.juna.zone.util.toro.*
import life.plank.juna.zone.util.view.*
import java.io.IOException
import javax.inject.Inject

@Suppress("DeferredResultUnused")
class FeedItemPeekPopup : BaseBlurPopup(), EmojiContainer {

    @Inject
    lateinit var restApi: RestApi

    private lateinit var feedEntry: FeedEntry
    private var boardId: String? = null
    private var isBoardActive: Boolean = false
    private var position: Int = 0
    private var exoPlayer: SimpleExoPlayer? = null

    companion object {
        val TAG: String = FeedItemPeekPopup::class.java.simpleName
        fun newInstance(feedEntry: FeedEntry, boardId: String?, isBoardActive: Boolean = true) =
                FeedItemPeekPopup().apply {
                    arguments = Bundle().apply {
                        putParcelable(findString(R.string.intent_feed_items), feedEntry)
                        putString(findString(R.string.intent_board_id), boardId)
                        putBoolean(findString(R.string.intent_is_board_active), isBoardActive)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            feedEntry = getParcelable(getString(R.string.intent_feed_items))!!
            boardId = getString(getString(R.string.intent_board_id), "")
            isBoardActive = getBoolean(getString(R.string.intent_is_board_active))
            position = getInt(getString(R.string.intent_position))
        }
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_feed_item_peek, container, false)

    override fun doOnStart() {
        initRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
        if (Build.VERSION.SDK_INT <= 23) releaseExoPlayer()
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT > 23) releaseExoPlayer()
    }

    private fun releaseExoPlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun enterAnimation(): Int = R.anim.zoom_in

    override fun dismissAnimation(): Int = R.anim.zoom_out

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getDragHandle(): View? = recycler_view_drag_area

    override fun getRootView(): View? = detail_peek

    override fun getBackgroundLayout(): ViewGroup? = root_peek_layout

    override fun onEmojiPosted(emoji: Emoji) {}

    private fun initRecyclerView() {
        if (feedEntry.feedItem.user != null) {
            Glide.with(this)
                    .load(feedEntry.feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerInsideTransform().override(UIDisplayUtil.getDp(20f).toInt(), UIDisplayUtil.getDp(20f).toInt())
                            .error(R.drawable.ic_football))
                    .into(profile_pic)
        } else
            profile_pic.setImageResource(R.drawable.ic_football)

        reaction_count.text = feedEntry.feedItem.interactions.emojiReacts.toString()
        feed_title_text_view.text = if (feedEntry.feedItem.contentType != AppConstants.ROOT_COMMENT) feedEntry.feedItem.title else null
        if (feedEntry.feedItem.contentType == AppConstants.NEWS) {
            if (feedEntry.feedItem.user != null) {
                user_name_text_view.text = feedEntry.feedItem.user?.displayName
            } else {
                user_name_text_view.setText(R.string.juna_user_topic)
            }
        } else {
            if (feedEntry.feedItem.user != null) {
                user_name_text_view.text = feedEntry.feedItem.user?.displayName
            } else {
                user_name_text_view.visibility = View.INVISIBLE
            }
        }

        pin_image_view.setImageResource(
                if (feedEntry.feedInteractions.hasPinned)
                    R.drawable.ic_pin_active
                else
                    R.drawable.ic_pin_inactive
        )

        when (feedEntry.feedItem.contentType) {
            NEWS, IMAGE -> {
                setVisibilities(View.VISIBLE, View.GONE, View.GONE)
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
                                    val width = (UIDisplayUtil.getScreenSize(activity!!.windowManager.defaultDisplay)[0] - UIDisplayUtil.getDp(8f)).toInt()
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
                setVisibilities(View.VISIBLE, View.GONE, View.GONE)
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
                setVisibilities(View.GONE, View.VISIBLE, View.GONE)
                val videoUriString = feedEntry.feedItem.url
                val videoUri = Uri.parse(videoUriString)

                exoPlayer = ExoBuilder
                        .with(this)
                        .withMediaSource(videoUri)
                        .applyTo(video_player)
                        .playWhenReady()
            }
            ROOT_COMMENT -> {
                setVisibilities(View.GONE, View.GONE, View.VISIBLE)
                feed_text_view.setRootCommentPost(feedEntry.feedItem)
            }
        }
        feed_title_text_view.isSelected = true
    }

    private fun setVisibilities(imageViewVisibility: Int, videoViewVisibility: Int, textViewVisibility: Int) {
        feed_image_view.visibility = imageViewVisibility
        video_player_container.visibility = videoViewVisibility
        feed_text_view.visibility = textViewVisibility
    }
}