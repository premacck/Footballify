package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prembros.facilis.util.onElevatingClick
import kotlinx.android.synthetic.main.item_card_wallet.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.card.CardUser
import life.plank.juna.zone.ui.base.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CardWalletView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        initLayout(R.layout.item_card_wallet)
    }

    @ModelProp
    fun withUser(cardUser: CardUser) {
        name_text_view.text = cardUser.displayName
        handle_text_view.text = cardUser.handle
        Glide.with(this)
                .load(cardUser.profilePictureUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.shimmer_rectangle).error(R.drawable.ic_place_holder))
                .into(profile_pic)
    }

    @ModelProp
    fun withBorder(borderColor: String) {
        wallet_layout.setBorder(borderColor.getCardColor())
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    fun onClick(action: () -> Unit) = wallet_root_card.onElevatingClick { action() }
}