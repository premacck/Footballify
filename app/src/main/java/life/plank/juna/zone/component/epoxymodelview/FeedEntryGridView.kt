package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.activity.BaseCardActivity
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.util.*
import kotlinx.android.synthetic.main.item_board_grid_row.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.feed.FeedItem
import life.plank.juna.zone.view.base.initLayout

@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class FeedEntryGridView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val glide = Glide.with(this)

    init {
        initLayout(R.layout.item_board_grid_row)
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
    fun onLongClick(pair: Pair<BaseBlurPopup, BaseCardActivity>) = LongPopupClickListener.inside(pair.second)
            .withVibration()
            .withPopup(pair.first)
            .setOn(this)
}