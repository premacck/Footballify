package life.plank.juna.zone.view.user.card

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.util.onReducingClick
import kotlinx.android.synthetic.main.item_card_wallet_header.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.view.base.initLayout

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CardWalletHeader @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        initLayout(R.layout.item_card_wallet_header)
    }

    @TextProp
    fun withHeader(headerText: CharSequence) {
        header.text = headerText
    }

    @TextProp
    fun withHeaderCount(headerCount: CharSequence?) {
        header_count.text = headerCount
    }

    @TextProp
    fun withHeaderAction(headerActionText: CharSequence) {
        header_action.text = headerActionText
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    fun onClick(action: () -> Unit) = onReducingClick { action() }
}