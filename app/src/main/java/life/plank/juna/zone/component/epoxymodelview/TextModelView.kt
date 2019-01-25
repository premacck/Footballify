package life.plank.juna.zone.component.epoxymodelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.util.setMargin
import kotlinx.android.synthetic.main.item_text_view.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ui.base.initLayout

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TextModelView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        initLayout(R.layout.item_text_view)
    }

    @ModelProp(options = [ModelProp.Option.GenerateStringOverloads])
    fun withText(charSequence: CharSequence?) {
        text_view.text = charSequence
    }

    @ModelProp
    fun withVerticalMargins(margin: Int) {
        text_view.setMargin(null, margin, null, margin)
    }

    @ModelProp
    fun withHorizontalMargins(margin: Int) {
        text_view.setMargin(margin, null, margin, null)
    }
}
