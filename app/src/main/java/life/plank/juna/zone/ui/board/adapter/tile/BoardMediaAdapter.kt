package life.plank.juna.zone.ui.board.adapter.tile

import android.view.*
import android.view.View.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.prembros.asymrecycler.library.base.AsymItem
import com.prembros.asymrecycler.library.widget.WrappedAsymRecyclerAdapter
import com.prembros.facilis.util.isNullOrEmpty
import kotlinx.android.synthetic.main.item_board_tile.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.feed.FeedEntry
import life.plank.juna.zone.ui.base.setRootCommentPost
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.util.view.UIDisplayUtil.setupFeedEntryByMasonryLayout
import org.jetbrains.anko.*

class BoardMediaAdapter(private val glide: RequestManager) : WrappedAsymRecyclerAdapter<BoardMediaAdapter.BoardMediaViewHolder>() {

    val boardFeed: MutableList<FeedEntry> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMediaViewHolder =
            BoardMediaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_board_tile, parent, false))

    override fun onBindViewHolder(holder: BoardMediaViewHolder, position: Int) {
        val feedItem = boardFeed[position].feedItem
        val view = holder.itemView

        if (feedItem.user != null) {
            glide.load(feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.centerCropTransform()
                            .placeholder(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile))
                    .into(view.profile_picture)
        }

        if (isNullOrEmpty(feedItem.contentType)) return

        when (feedItem.contentType) {
            AUDIO -> {
                setVisibility(holder, GONE, VISIBLE, GONE)
                view.tile_image_view.setImageResource(R.drawable.ic_mic_white)
            }
            NEWS, IMAGE -> {
                setVisibility(holder, GONE, VISIBLE, GONE)
                if (feedItem.thumbnail != null) {
                    glide.load(feedItem.thumbnail?.imageUrl)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(view.tile_image_view)
                }
            }
            VIDEO -> {
                setVisibility(holder, GONE, VISIBLE, VISIBLE)
                if (feedItem.thumbnail != null) {
                    glide.load(feedItem.thumbnail?.imageUrl)
                            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                            .into(view.tile_image_view)
                }
            }
            else -> {
                setVisibility(holder, VISIBLE, GONE, GONE)
                view.comment_text_view.setRootCommentPost(feedItem)
            }
        }
    }

    private fun setVisibility(holder: BoardMediaViewHolder, commentTextViewVisibility: Int, tileImageViewVisibility: Int, playBtnVisibility: Int) {
        holder.itemView.comment_text_view.visibility = commentTextViewVisibility
        holder.itemView.tile_image_view.visibility = tileImageViewVisibility
        holder.itemView.play_btn.visibility = playBtnVisibility
    }

    fun update(feedEntryList: List<FeedEntry>) {
        doAsync {
            if (boardFeed.isNotEmpty()) boardFeed.clear()

            boardFeed.addAll(feedEntryList)
            setupFeedEntryByMasonryLayout(boardFeed)
            uiThread {
                notifyDataSetChanged() }
        }
    }

    fun updateNewPost(feedEntry: FeedEntry) {
        doAsync {
            if (!boardFeed.any { it.feedItem.id == feedEntry.feedItem.id }) {
                boardFeed.add(0, feedEntry)
            }
            setupFeedEntryByMasonryLayout(boardFeed)
            uiThread { notifyDataSetChanged() }
        }
    }

    override fun getItem(position: Int): AsymItem = boardFeed[position]

    override fun getItemCount(): Int = boardFeed.size

    class BoardMediaViewHolder(view: View) : RecyclerView.ViewHolder(view)
}