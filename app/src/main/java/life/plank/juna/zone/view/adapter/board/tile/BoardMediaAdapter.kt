package life.plank.juna.zone.view.adapter.board.tile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.prembros.asymmetricrecyclerview.base.AsymmetricItem
import com.prembros.asymmetricrecyclerview.widget.WrappedAsymmetricRecyclerAdapter
import kotlinx.android.synthetic.main.item_board_tile.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.util.AppConstants.*
import life.plank.juna.zone.util.UIDisplayUtil.getCommentColor
import life.plank.juna.zone.util.UIDisplayUtil.getCommentText
import java.util.*

/**
 * Created by plank-prachi on 4/10/2018.
 */
class BoardMediaAdapter(private val glide: RequestManager) : WrappedAsymmetricRecyclerAdapter<BoardMediaAdapter.BoardMediaViewHolder>() {

    private val boardFeed: MutableList<FeedEntry> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMediaViewHolder {
        return BoardMediaViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_board_tile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BoardMediaViewHolder, position: Int) {
        val feedItem = boardFeed[position].feedItem
        val view = holder.itemView

        //TODO: remove this null check after the backend returns the user profile picture
        if (feedItem.user != null) {
            glide.load(feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile))
                    .into(view.profile_picture)
        }

        if (feedItem.contentType == null) return

        when (feedItem.contentType) {
            AUDIO -> {
                setVisibility(holder, GONE, VISIBLE, GONE)
                view.tile_image_view.setImageResource(R.drawable.ic_mic_white)
            }
            IMAGE -> {
                setVisibility(holder, GONE, VISIBLE, GONE)
                if (feedItem.thumbnail != null) {
                    glide.load(feedItem.thumbnail!!.imageUrl)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(view.tile_image_view)
                }
            }
            VIDEO -> {
                setVisibility(holder, GONE, VISIBLE, VISIBLE)
                if (feedItem.thumbnail != null) {
                    glide.load(feedItem.thumbnail!!.imageUrl)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(view.tile_image_view)
                }
            }
            else -> {
                setVisibility(holder, VISIBLE, GONE, GONE)
                if (feedItem.title != null) {
                    val comment = feedItem.title!!.replace("^\"|\"$".toRegex(), "")
                    view.comment_text_view.background = getCommentColor(comment)
                    view.comment_text_view.text = getCommentText(comment)
                }
            }
        }
    }

    private fun setVisibility(holder: BoardMediaViewHolder, commentTextViewVisibility: Int, tileImageViewVisibility: Int, playBtnVisibility: Int) {
        holder.itemView.comment_text_view.visibility = commentTextViewVisibility
        holder.itemView.tile_image_view.visibility = tileImageViewVisibility
        holder.itemView.play_btn.visibility = playBtnVisibility
    }

    fun update(boardFeed: List<FeedEntry>) {
        if (!this.boardFeed.isEmpty()) {
            this.boardFeed.clear()
        }
        this.boardFeed.addAll(boardFeed)
        notifyDataSetChanged()
    }

    fun updateNewPost(feedItem: FeedEntry) {
        if (!boardFeed.contains(feedItem)) {
            boardFeed.add(0, feedItem)
            notifyItemInserted(0)
        }
    }

    fun getBoardFeed(): List<FeedEntry> = boardFeed

    override fun getItem(position: Int): AsymmetricItem = boardFeed[position]

    override fun getItemCount(): Int = boardFeed.size

    class BoardMediaViewHolder(view: View) : RecyclerView.ViewHolder(view)
}