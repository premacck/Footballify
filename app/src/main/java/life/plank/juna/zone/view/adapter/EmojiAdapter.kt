package life.plank.juna.zone.view.adapter

import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_emoji.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DateUtil.getRequestDateStringOfNow
import life.plank.juna.zone.util.PreferenceManager.Auth.getToken
import life.plank.juna.zone.util.UIDisplayUtil.emoji
import life.plank.juna.zone.util.errorToast
import life.plank.juna.zone.util.facilis.hide
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.setObserverThreadsAndSmartSubscribe
import java.net.HttpURLConnection.HTTP_OK

class EmojiAdapter(
        private val restApi: RestApi,
        private val boardId: String,
        private val emojiBottomSheetBehavior: BottomSheetBehavior<*>?
) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    companion object {
        var feedId: String? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emoji, parent, false)
        return EmojiViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.itemView.emoji_view.setImageDrawable(ZoneApplication.getContext().getDrawable(emoji[position].emojiDrawable!!))
        holder.itemView.emoji_view.onDebouncingClick {
            emojiBottomSheetBehavior?.hide()
            postEmoji(emoji[position].reaction)
        }
    }

    private fun postEmoji(reaction: Int?) {
        restApi.postReaction(feedId, boardId, reaction, getRequestDateStringOfNow(), getToken()).setObserverThreadsAndSmartSubscribe({
            Log.e("postEmoji()", "onError: ", it)
        }, {
            if (it.code() != HTTP_OK) errorToast(R.string.something_went_wrong, it)
        })
    }

    override fun getItemCount(): Int = emoji.size

    class EmojiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
