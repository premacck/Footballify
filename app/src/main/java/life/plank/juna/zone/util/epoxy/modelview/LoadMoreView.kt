package life.plank.juna.zone.util.epoxy.modelview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import com.prembros.facilis.util.onDebouncingClick
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.initLayout

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadMoreView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var isForNextPage: Boolean = true

    init {
        initLayout(R.layout.item_load_more)
    }

    @ModelProp
    fun isForNextPage(isForNextPage: Boolean) {
        this.isForNextPage = isForNextPage
    }

    @ModelProp(value = [ModelProp.Option.DoNotHash])
    fun onClick(listener: (Boolean) -> Unit) = onDebouncingClick { listener(isForNextPage) }
}