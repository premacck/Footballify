package life.plank.juna.zone.util.facilis

import android.app.Activity
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import life.plank.juna.zone.util.UIDisplayUtil
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseCard : BaseFragment() {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getRootCard()?.run {
            setTopMargin(UIDisplayUtil.getDp(if (getParentActivity().index > 0) 20f else 0f).toInt())
            isClickable = true
        }
        activity?.let { setupSwipeDownGesture(it) }
        (getBackgroundBlurLayout() as? BlurLayout)?.beginBlur()
    }

    private fun setupSwipeDownGesture(activity: Activity) = getDragHandle()?.setSwipeDownListener(activity, getRootCard()!!, getBackgroundBlurLayout())

    fun moveToBackGround() {
        getRootCard()?.moveToBackGround(getParentActivity().index)
    }

    fun moveToForeGround() {
        getRootCard()?.moveToForeGround()
    }

    fun dispose() {
        getDragHandle()?.setOnTouchListener(null)
    }

    fun pushFragment(baseFragment: BaseFragment, isAddToBackStack: Boolean = false) {
        getParentActivity().pushFragment(baseFragment, isAddToBackStack)
    }

    fun pushPopup(dialogFragment: BaseDialogFragment) = getParentActivity().pushPopup(dialogFragment)

    abstract fun getBackgroundBlurLayout(): ViewGroup?

    abstract fun getRootCard(): ViewGroup?

    abstract fun getDragHandle(): View?

    fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }

    override fun onDestroyView() {
        (getBackgroundBlurLayout() as? BlurLayout)?.pauseBlur()
        dispose()
        super.onDestroyView()
    }
}