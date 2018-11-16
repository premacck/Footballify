package life.plank.juna.zone.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_board_grid_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedEntry
import life.plank.juna.zone.interfaces.FeedEntryContainer
import life.plank.juna.zone.util.facilis.longClickWithVibrate
import life.plank.juna.zone.util.facilis.onDebouncingClick
import java.util.*

class UserFeedAdapter(private val feedEntryContainer: FeedEntryContainer, private val glide: RequestManager) : RecyclerView.Adapter<UserFeedAdapter.UserFeedViewHolder>() {

    private var userFeed: MutableList<FeedEntry> = ArrayList()
    private var isUpdated: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFeedAdapter.UserFeedViewHolder =
            UserFeedAdapter.UserFeedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_board_grid_row, parent, false))

    override fun onBindViewHolder(holder: UserFeedAdapter.UserFeedViewHolder, position: Int) {
        val feedItem = userFeed[position].feedItem
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

        holder.itemView.onDebouncingClick { feedEntryContainer.openFeedEntry(userFeed, "", position, "") }

        holder.itemView.longClickWithVibrate { feedEntryContainer.showFeedItemPeekPopup(position) }
    }

    override fun getItemCount(): Int = userFeed.size

    fun setUserFeed(userFeed: MutableList<FeedEntry>) {
        isUpdated = true
        this.userFeed = userFeed
        notifyDataSetChanged()
    }

    class UserFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}