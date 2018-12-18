package life.plank.juna.zone.view.adapter.common

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.football_feed_detail_row.view.*
import kotlinx.android.synthetic.main.layout_interaction_component.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.setRootCommentPost
import life.plank.juna.zone.util.facilis.showFor
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.PinManager.isFeedItemPinned
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.PinManager.toggleFeedItemPin
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.view.EmojiHashMap
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.util.view.UIDisplayUtil.getScreenSize
import org.jetbrains.anko.windowManager
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.net.HttpURLConnection.*
import java.util.*

/**
 * Created by plank-prachi on 1/30/2018.
 */

class BoardFeedDetailAdapter(private val restApi: RestApi,
                             private val boardId: String?,
                             private val isBoardActive: Boolean,
                             private val emojiBottomSheetBehavior: BottomSheetBehavior<*>?,
                             private val emojiAdapter: EmojiAdapter?,
                             private val target: String?,
                             private val glide: RequestManager) : RecyclerView.Adapter<BoardFeedDetailAdapter.FootballFeedDetailViewHolder>() {

    private val mediaPlayer = MediaPlayer()
    private val TAG = BoardFeedDetailAdapter::class.java.canonicalName
    private var feedsListItem: List<FeedEntry>? = null

    init {
        this.feedsListItem = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FootballFeedDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.football_feed_detail_row, parent, false)
        return FootballFeedDetailViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: FootballFeedDetailViewHolder, position: Int) {
        val (feedItem, feedInteractions) = feedsListItem!![position]

        feedInteractions.hasPinned = isFeedItemPinned(feedItem)

        if (feedsListItem!![position].feedInteractions.myReaction != 0) {
            holder.itemView.reaction_view.setImageResource(EmojiHashMap.getEmojiHashMap().get(feedsListItem!![position].feedInteractions.myReaction))
        }

        if (feedItem.user != null) {

            glide.load(feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerInsideTransform().override(getDp(20f).toInt(), getDp(20f).toInt()))
                    .into(holder.itemView.profile_pic)
        } else {
            holder.itemView.profile_pic.setImageResource(R.drawable.ic_football)
        }

        if (feedItem.user != null) {
            holder.itemView.user_name_text_view.text = feedItem.user!!.displayName
        } else {
            holder.itemView.user_name_text_view.setText(R.string.juna_user)
        }

        holder.itemView.feed_title_text_view.text = feedItem.title

        if (!isNullOrEmpty(feedItem.interactions)) {
            holder.itemView.reaction_count.text = feedItem.interactions!!.emojiReacts!!.toString()
        }

        holder.itemView.pin_image_view.setImageResource(
                if (feedInteractions.hasPinned)
                    R.drawable.ic_pin_active
                else
                    R.drawable.ic_pin_inactive
        )

        if (feedItem.contentType != null) {
            when (feedItem.contentType) {
                NEWS, IMAGE -> {

                    mediaPlayer.stop()
                    holder.setVisibilities(View.VISIBLE, View.GONE, View.GONE)
                    try {
                        glide.load(feedItem.thumbnail!!.imageUrl)
                                .apply(RequestOptions.errorOf(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder))
                                .into(holder.itemView.feed_image_view)
                    } catch (e: Exception) {
                        holder.itemView.feed_image_view.setImageResource(R.drawable.ic_place_holder)
                    }

                    mediaPlayer.stop()
                    holder.setVisibilities(View.VISIBLE, View.GONE, View.GONE)
                    try {
                        val urlToLoad = if (feedItem.contentType == NEWS)
                            feedItem.thumbnail!!.imageUrl
                        else
                            feedItem.url

                        glide.asBitmap().load(urlToLoad)
                                .apply(RequestOptions.errorOf(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder))
                                .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {

                                        val width = (getScreenSize(ZoneApplication.getContext().windowManager.defaultDisplay)[0] - getDp(8f)).toInt()
                                        val params = holder.itemView.feed_image_view.layoutParams as RelativeLayout.LayoutParams
                                        params.height = width * bitmap.height / bitmap.width
                                        holder.itemView.feed_image_view.layoutParams = params
                                        holder.itemView.feed_image_view.setImageBitmap(bitmap)
                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                        holder.itemView.feed_image_view.setImageDrawable(errorDrawable)
                                    }
                                })
                    } catch (e: Exception) {
                        holder.itemView.feed_image_view.setImageResource(R.drawable.ic_place_holder)
                    }


                }
                AUDIO -> {
                    mediaPlayer.stop()
                    holder.setVisibilities(View.VISIBLE, View.GONE, View.GONE)
                    holder.itemView.feed_image_view.setImageResource(R.drawable.ic_mic_white)

                    val uri = feedItem.url
                    val videoUri = Uri.parse(uri)

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    try {
                        mediaPlayer.setDataSource(ZoneApplication.getContext(), videoUri)
                        mediaPlayer.prepare()
                    } catch (e: IOException) {
                        mediaPlayer.stop()
                    }

                    mediaPlayer.start()

                    try {
                        glide.load(feedItem.url)
                                .apply(RequestOptions.errorOf(R.drawable.ic_place_holder).placeholder(R.drawable.ic_place_holder))
                                .into(holder.itemView.feed_image_view)

                    } catch (e: Exception) {
                        holder.itemView.feed_image_view.setImageResource(R.drawable.ic_place_holder)
                    }

                }
                VIDEO -> {
                    mediaPlayer.stop()
                    holder.setVisibilities(View.GONE, View.VISIBLE, View.GONE)
                    val uri = feedItem.url
                    val videoUri = Uri.parse(uri)
                    holder.itemView.captured_video_view.setVideoURI(videoUri)
                    holder.itemView.captured_video_view.start()
                }
                ROOT_COMMENT -> {
                    mediaPlayer.stop()
                    holder.setVisibilities(View.GONE, View.GONE, View.VISIBLE)
                    holder.itemView.feed_text_view.setRootCommentPost(feedItem)
                }
            }
        }

        holder.itemView.pin_image_view.onDebouncingClick {
            if (feedInteractions.hasPinned) {
                unpinItem(feedsListItem!![position], position)
            } else {
                pinItem(feedsListItem!![position], position)
            }
        }

        holder.itemView.reaction_view.setOnClickListener {
            emojiBottomSheetBehavior!!.showFor(emojiAdapter, feedsListItem!![position].feedItem.id, 850)
        }
    }

    override fun getItemCount(): Int {
        return feedsListItem!!.size
    }

    fun update(feedEntryList: List<FeedEntry>) {
        this.feedsListItem = feedEntryList
        notifyDataSetChanged()
    }

    private fun pinItem(feedEntry: FeedEntry, position: Int) {
        restApi.pinFeedItem(feedEntry.feedItem.id, BOARD, boardId, getRequestDateStringOfNow(), getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<String>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted : pinItem()")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "pinItem() " + e.message)
                        errorToast(R.string.failed_to_pin_feed, e)
                    }

                    override fun onNext(response: Response<String>) {
                        when (response.code()) {
                            HTTP_OK, HTTP_CREATED -> {
                                feedEntry.feedInteractions.hasPinned = true
                                feedEntry.feedInteractions.pinId = response.body()
                                feedEntry.feedInteractions.previousPosition = position
                                toggleFeedItemPin(feedEntry, true)
                                pinFeedEntry(feedsListItem!!, feedEntry)
                                notifyItemChanged(position)
                                notifyItemMoved(position, 0)
                            }
                            HTTP_NOT_FOUND -> errorToast(R.string.failed_to_find_feed, response)
                            HTTP_INTERNAL_ERROR -> errorToast(R.string.already_pinned_feed, response)
                            else -> errorToast(R.string.failed_to_pin_feed, response)
                        }
                    }
                })
    }

    private fun unpinItem(feedEntry: FeedEntry, position: Int) {
        restApi.unpinFeedItem(boardId, feedEntry.feedInteractions.pinId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Response<JsonObject>>() {
                    override fun onCompleted() {
                        Log.i(TAG, "onCompleted : unpinItem()")
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "unpinItem() " + e.message)
                        errorToast(R.string.failed_to_unpin_feed, e)
                    }

                    override fun onNext(response: Response<JsonObject>) {
                        when (response.code()) {
                            HTTP_OK, HTTP_NO_CONTENT -> {
                                feedEntry.feedInteractions.hasPinned = false
                                feedEntry.feedInteractions.pinId = null
                                toggleFeedItemPin(feedEntry, false)
                                unpinFeedEntry(feedsListItem, feedEntry)
                                notifyItemChanged(position)
                                notifyItemMoved(position, feedEntry.feedInteractions.previousPosition)
                            }
                            HTTP_NOT_FOUND -> errorToast(R.string.failed_to_find_feed, response)
                            HTTP_INTERNAL_ERROR -> errorToast(R.string.already_removed_pin, response)
                            else -> errorToast(R.string.failed_to_unpin_feed, response)
                        }
                    }
                })
    }

    class FootballFeedDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setVisibilities(imageViewVisibility: Int, videoViewVisibility: Int, textViewVisibility: Int) {
            itemView.feed_image_view.visibility = imageViewVisibility
            itemView.captured_video_view.visibility = videoViewVisibility
            itemView.feed_text_view.visibility = textViewVisibility
        }
    }
}
