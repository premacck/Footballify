package life.plank.juna.zone.util.facilis

import android.app.Activity
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import life.plank.juna.zone.R
import life.plank.juna.zone.util.view.UIDisplayUtil
import life.plank.juna.zone.util.view.UIDisplayUtil.hideSoftKeyboard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseCard : BaseFragment() {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getRootView()?.run {
            setTopMargin(UIDisplayUtil.getDp(if (getParentActivity().index > 0) 20f else 0f).toInt())
            isClickable = true
        }
        activity?.let { setupSwipeDownGesture(it) }
        (getBackgroundBlurLayout() as? BlurLayout)?.beginBlur()
    }

    private fun setupSwipeDownGesture(activity: Activity) = getDragView()?.setSwipeDownListener(activity, getRootView()!!, getBackgroundBlurLayout())

    fun moveToBackGround() {
        getRootView()?.moveToBackGround(getParentActivity().index)
        dragHandle()?.fadeOut()
    }

    fun moveToForeGround() {
        getRootView()?.moveToForeGround()
        dragHandle()?.fadeIn()
    }

    fun dispose() {
        getDragView()?.setOnTouchListener(null)
    }

    fun pushFragment(baseFragment: BaseFragment, isAddToBackStack: Boolean = false) {
        getParentActivity().pushFragment(baseFragment, isAddToBackStack)
    }

    fun pushPopup(dialogFragment: BaseDialogFragment) = getParentActivity().pushPopup(dialogFragment)

    abstract fun getBackgroundBlurLayout(): ViewGroup?

    abstract fun getRootView(): ViewGroup?

    abstract fun getDragView(): View?

    @IdRes
    open fun dragHandleId(): Int = R.id.drag_handle_image

    fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }

    override fun onDestroyView() {
        hideSoftKeyboard(getRootView())
        (getBackgroundBlurLayout() as? BlurLayout)?.pauseBlur()
        dispose()
        super.onDestroyView()
    }
}