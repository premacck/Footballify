package life.plank.juna.zone.view.fragment.base

import life.plank.juna.zone.view.activity.base.BaseCardActivity

abstract class FlatFragment : BaseFragment() {

    protected fun pushFragment(fragment: BaseFragment) = getParentActivity().pushFragment(fragment, true)

    protected fun pushPopup(dialogFragment: BaseDialogFragment) = getParentActivity().pushPopup(dialogFragment)

    protected fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }
}