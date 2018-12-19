package life.plank.juna.zone.util.view

import android.content.Context
import android.util.AttributeSet

import androidx.recyclerview.widget.GridLayoutManager

class HorizontalGridLayoutManager @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : GridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    init {
        orientation = HORIZONTAL
    }

    override fun getOrientation(): Int = HORIZONTAL
}
