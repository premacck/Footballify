package life.plank.juna.zone.component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bvapp.arcmenulibrary.ArcMenu
import kotlinx.android.synthetic.main.boom_menu.view.*
import life.plank.juna.zone.R

class BoomMenu @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val hideRunnable = Runnable { visibility = View.INVISIBLE }

    init {
        View.inflate(context, R.layout.boom_menu, this)

        boom_menu_container.setOnTouchListener { _, _ -> toggleBoomMenu() }
    }

    fun toggleBoomMenu(): Boolean {
        if (isOpen()) {
            arc_menu.fabArcMenu.performClick()
            return true
        }
        return false
    }

    fun get(): ArcMenu = arc_menu

    fun isOpen() = arc_menu.isOpen

    fun menuIn() = arc_menu.menuIn()

    fun menuOut() = arc_menu.menuOut()

    fun show() {
        arc_menu.show()
        visibility = View.VISIBLE
    }

    fun hide() {
        arc_menu.hide()
        removeCallbacks(hideRunnable)
        postDelayed(hideRunnable, 500)
    }
}