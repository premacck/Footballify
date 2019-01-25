package life.plank.juna.zone.ui.base.fragment

import androidx.annotation.IdRes
import com.prembros.facilis.fragment.BaseCardFragment
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.dismissBoomMenuIfOpen

abstract class BaseJunaCard : BaseCardFragment() {

    @IdRes
    override fun dragHandleId(): Int = R.id.drag_handle_image

    override fun onBackPressed(): Boolean = dismissBoomMenuIfOpen()
}