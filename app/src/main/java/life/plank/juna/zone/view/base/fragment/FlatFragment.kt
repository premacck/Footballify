package life.plank.juna.zone.view.base.fragment

import com.prembros.facilis.dialog.BaseDialogFragment
import com.prembros.facilis.fragment.BaseFragment
import life.plank.juna.zone.view.base.BaseJunaCardActivity

abstract class FlatFragment : BaseJunaFragment() {

    protected fun pushFragment(fragment: BaseFragment) = getParentActivity().pushFragment(fragment, true)

    protected fun pushPopup(dialogFragment: BaseDialogFragment) = getParentActivity().pushPopup(dialogFragment)

    protected fun getParentActivity(): BaseJunaCardActivity {
        if (activity is BaseJunaCardActivity)
            return activity as BaseJunaCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseJunaCardActivity")
    }
}