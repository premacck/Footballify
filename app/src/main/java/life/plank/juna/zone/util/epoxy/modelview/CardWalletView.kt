package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_card_wallet.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CardWalletView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        initLayout(R.layout.item_card_wallet)
    }

    @TextProp
    fun withName(name: CharSequence) {
        name_text_view.text = name
    }

    @TextProp
    fun withHandle(handle: CharSequence) {
        handle_text_view.text = handle
    }

    @ModelProp
    fun withProfilePic(url: String?) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.placeholderOf(R.drawable.shimmer_rectangle).error(R.drawable.ic_place_holder))
                .into(profile_pic)
    }

    @ModelProp
    fun withBorder(borderColor: String) {
        wallet_layout.setBorder(borderColor.getCardColor())
    }
}