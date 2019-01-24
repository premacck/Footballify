package life.plank.juna.zone.component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bvapp.arcmenulibrary.ArcMenu
import com.bvapp.arcmenulibrary.widget.FloatingActionButton
import kotlinx.android.synthetic.main.boom_menu.view.*
import life.plank.juna.zone.R
import org.jetbrains.anko.find

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
            arc_menu_btn.find<FloatingActionButton>(com.bvapp.arcmenulibrary.R.id.fabArcMenu).performClick()
            return true
        }
        return false
    }

    fun get(): ArcMenu = arc_menu_btn

    fun isOpen() = arc_menu_btn.isOpen

    fun menuIn() = arc_menu_btn.menuIn()

    fun menuOut() = arc_menu_btn.menuOut()

    fun show() {
        arc_menu_btn.show()
        visibility = View.VISIBLE
    }

    fun hide() {
        arc_menu_btn.hide()
        removeCallbacks(hideRunnable)
        postDelayed(hideRunnable, 500)
    }
}