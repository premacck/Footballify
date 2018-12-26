package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_board_grid_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItem

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class FeedEntryGridView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val glide = Glide.with(this)

    init {
        View.inflate(context, R.layout.item_board_grid_row, this)
    }

    @ModelProp
    fun withFeedItem(feedItem: FeedItem) {
        if (feedItem.thumbnail != null) {
            glide.load(feedItem.thumbnail!!.imageUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder)
                            .error(R.drawable.ic_place_holder))
                    .into(tile_image_view)
        } else
            tile_image_view.setImageResource(R.drawable.ic_place_holder)

        if (feedItem.user != null) {
            glide.load(feedItem.user!!.profilePictureUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.shimmer_circle)
                            .error(R.drawable.ic_football))
                    .into(profile_pic)
        } else
            profile_pic.setImageResource(R.drawable.ic_football)
    }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onClick(listener: () -> Unit) = onDebouncingClick { listener() }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onLongClick(listener: () -> Unit) = longClickWithVibrate { listener() }
}