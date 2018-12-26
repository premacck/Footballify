package life.plank.juna.zone.view.fragment.base

import com.prembros.facilis.fragment.BaseCardListChildFragment
import life.plank.juna.zone.R

abstract class BaseJunaCardChild : BaseCardListChildFragment() {
    override fun dragHandleId(): Int = R.id.drag_handle_image
}