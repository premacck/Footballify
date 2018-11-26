package life.plank.juna.zone.view.adapter.common

import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_emoji.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.UIDisplayUtil
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.hide
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.setEmoji
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK

class EmojiAdapter(
        private val restApi: RestApi,
        private val boardId: String,
        private val emojiBottomSheetBehavior: BottomSheetBehavior<*>?,
        private var feedId: String? = null
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    private var emojiList = UIDisplayUtil.emoji

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val emoji = emojiList[position]

        holder.itemView.emoji_count.text = emoji.emojiCount.toString()
        holder.itemView.emoji_text_view.setEmoji(emoji.emoji)

        holder.itemView.onDebouncingClick { postEmoji(emoji.emoji) }
    }

    fun update(feedItemId: String?) {
        feedId = feedItemId
        if (feedId != null) getTopEmoji()
    }

    private fun getTopEmoji() {
        restApi.getTopEmoji(feedId, getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("getTopEmoji()", "ERROR: ", it)
        }, {
            when (it.code()) {
                HTTP_OK -> {
                    this.emojiList = it.body() as ArrayList
                    notifyDataSetChanged()
                }
                else -> errorToast(R.string.failed_to_get_top_emoji, it)
            }
        })
    }

    private fun postEmoji(emoji: Int) {
        restApi.postReaction(feedId, boardId, emoji, getRequestDateStringOfNow(), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("postEmoji()", "onError: ", it)
        }, {
            when (it.code()) {
                HTTP_CREATED -> emojiBottomSheetBehavior?.hide()
                else -> errorToast(R.string.something_went_wrong, it)
            }
        })
    }

    override fun getItemCount(): Int = emojiList.size

    class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
