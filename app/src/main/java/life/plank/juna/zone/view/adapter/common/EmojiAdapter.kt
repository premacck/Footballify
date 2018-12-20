package life.plank.juna.zone.view.adapter.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.item_emoji.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Emoji
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.EmojiContainer
import life.plank.juna.zone.util.common.errorToast
import life.plank.juna.zone.util.common.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.util.facilis.hide
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.setEmoji
import life.plank.juna.zone.util.sharedpreference.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.time.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.view.UIDisplayUtil.addDefaultEmojis
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import java.net.HttpURLConnection.*

class EmojiAdapter(
        private val restApi: RestApi,
        private var boardId: String,
        private val emojiBottomSheetBehavior: BottomSheetBehavior<*>?,
        private var feedItemId: String? = null,
        private val isFeedItem: Boolean,
        private val emojiContainer: EmojiContainer
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    private var emojiList: MutableList<Emoji> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder =
            EmojiViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false))

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val emoji = emojiList[position]
        holder.itemView.run {
            if (emoji.emoji == 0) {
                emoji_root_layout.visibility = View.INVISIBLE
            } else {
                emoji_root_layout.visibility = View.VISIBLE
                if (emoji.emojiCount > 0) {
                    emoji_count.visibility = View.VISIBLE
                    emoji_count.text = emoji.emojiCount.toString()
                    (emoji_root_layout.layoutParams as? GridLayoutManager.LayoutParams)?.run {
                        bottomMargin = getDp(10f).toInt()
                        emoji_root_layout.layoutParams = this
                    }
                } else {
                    emoji_count.visibility = View.GONE
                    (emoji_root_layout.layoutParams as? GridLayoutManager.LayoutParams)?.run {
                        bottomMargin = getDp(2f).toInt()
                        emoji_root_layout.layoutParams = this
                    }
                }
            }
            emoji_text_view.setEmoji(emoji.emoji)

            onDebouncingClick { postEmoji(emoji.emoji) }
        }
    }

    fun update(id: String) {
        emojiList.clear()
        if (isFeedItem) {
            this.feedItemId = id
            if (this.feedItemId != null) getTopEmoji()
        } else {
            this.boardId = id
            getTopEmoji()
        }
    }

    private fun getTopEmoji() {
        val topEmojiCall = if (isFeedItem) {
            restApi.getTopFeedItemEmoji(feedItemId, getToken())
        } else {
            restApi.getTopBoardEmoji(boardId, getToken())
        }
        topEmojiCall.setObserverThreadsAndSmartSubscribe({
            Log.e("getTopFeedItemEmoji()", "ERROR: ", it)
            useDefaultEmoji()
        }, {
            when (it.code()) {
                HTTP_OK -> {
                    this.emojiList.addAll(it.body() as ArrayList)
                    appendDefaultEmoji()
                    notifyDataSetChanged()
                }
                HTTP_NOT_FOUND -> useDefaultEmoji()
                else -> {
                    useDefaultEmoji()
                    errorToast(R.string.failed_to_get_top_emoji, it)
                }
            }
        }, emojiContainer)
    }

    private fun appendDefaultEmoji() {
        while (emojiList.size % 5 != 0) {
            emojiList.add(Emoji())
        }
        addDefaultEmojis(emojiList)
    }

    private fun useDefaultEmoji() {
        addDefaultEmojis(emojiList)
        notifyDataSetChanged()
    }

    private fun postEmoji(emoji: Int) {
        val postEmojiCall = if (isFeedItem) {
            restApi.postEmojiOnFeedItem(feedItemId, boardId, emoji, getRequestDateStringOfNow(), getToken())
        } else {
            restApi.postEmojiOnBoard(boardId, emoji, getRequestDateStringOfNow(), getToken())
        }
        postEmojiCall.setObserverThreadsAndSmartSubscribe({
            Log.e("postEmoji()", "onError: ", it)
        }, {
            when (it.code()) {
                HTTP_CREATED -> {
                    emojiBottomSheetBehavior?.hide()
                    emojiContainer.onEmojiPosted(Emoji(emoji, 1))
                }
                else -> errorToast(R.string.something_went_wrong, it)
            }
        }, emojiContainer)
    }

    override fun getItemCount(): Int = emojiList.size

    class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
