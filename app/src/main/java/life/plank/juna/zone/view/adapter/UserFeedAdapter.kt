package life.plank.juna.zone.view.adapter

import android.support.annotation.ColorRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_board_grid_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.PeekPreviewContainer
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.facilis.onCustomLongClick
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class UserFeedAdapter(private val peekPreviewContainer: PeekPreviewContainer, private val glide: RequestManager) : RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder>() {

    private var userFeed: MutableList<FeedEntry>? = null
    private var isUpdated: Boolean = false

    init {
        this.userFeed = ArrayList()
        isUpdated = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFeedAdapter.UserFeedViewHolder {
        return UserFeedAdapter.UserFeedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_board_grid_row, parent, false))
    }

    override fun onBindViewHolder(holder: UserFeedAdapter.UserFeedViewHolder, position: Int) {
        if (!isNullOrEmpty(userFeed)) {
            updateShimmer(holder, R.color.transparent, false)
            val feedItem = userFeed!![position].feedItem
            if (feedItem.thumbnail != null) {
                glide.load(feedItem.thumbnail!!.imageUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_place_holder))
                        .into(holder.itemView.tile_image_view)
            } else
                holder.itemView.tile_image_view!!.setImageResource(R.drawable.ic_place_holder)

            if (feedItem.user != null) {
                glide.load(feedItem.user!!.profilePictureUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_default_profile)
                                .error(R.drawable.ic_default_profile))
                        .into(holder.itemView.profile_pic)
            } else
                holder.itemView.profile_pic.setImageResource(R.drawable.ic_default_profile)
        } else {
            if (!isUpdated) {
                updateShimmer(holder, R.color.circle_background_color, true)
            } else {
                updateShimmer(holder, R.color.transparent, false)
            }
        }
        holder.itemView.onClick { /*PostDetailActivity.launch(ref.get().fragment, ref.get().userFeed, null, getAdapterPosition(), ref.get().fragment.getScreenshotLayout(), null)*/ }
        holder.itemView.onCustomLongClick { peekPreviewContainer.setBlurBackgroundAndShowFullScreenTiles(true, position) }
    }

    private fun updateShimmer(holder: UserFeedAdapter.UserFeedViewHolder, @ColorRes color: Int, isStarted: Boolean) {
        holder.itemView.comment_text_view.setBackgroundColor(findColor(color))
        if (isStarted) {
            holder.itemView.root_layout.startShimmerAnimation()
        } else {
            holder.itemView.root_layout.stopShimmerAnimation()
        }
    }

    override fun getItemCount(): Int = if (userFeed!!.isEmpty()) 12 else userFeed!!.size

    fun setUserFeed(userFeed: MutableList<FeedEntry>) {
        isUpdated = true
        this.userFeed = userFeed
        notifyDataSetChanged()
    }

    class UserFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}