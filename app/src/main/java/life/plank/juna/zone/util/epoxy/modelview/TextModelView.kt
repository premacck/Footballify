package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.item_text_view.view.*
import life.plank.juna.zone.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TextModelView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        View.inflate(context, R.layout.item_text_view, this)
    }

    @ModelProp(options = [ModelProp.Option.GenerateStringOverloads])
    fun withText(charSequence: CharSequence?) {
        text_view.text = charSequence
    }
}
