package life.plank.juna.zone.view.fragment.base

import android.support.annotation.IdRes
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.pushPopup
import life.plank.juna.zone.view.activity.base.BaseCardActivity

abstract class FlatFragment : BaseFragment() {

    protected fun pushFragment(fragment: BaseCard) {
        getParentActivity().pushFragment(fragment, true)
    }

    protected fun pushPopup(dialogFragment: BaseDialogFragment) {
        childFragmentManager.pushPopup(
                getPopupFragmentId(),
                dialogFragment,
                dialogFragment::class.java.simpleName
        )
    }

    @IdRes
    abstract fun getPopupFragmentId(): Int

    protected fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }
}