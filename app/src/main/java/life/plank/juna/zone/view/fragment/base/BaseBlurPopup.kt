package life.plank.juna.zone.view.fragment.base

import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import life.plank.juna.zone.util.facilis.SwipeDownToDismissListener
import life.plank.juna.zone.util.facilis.beginBlur
import life.plank.juna.zone.util.facilis.fadeOut
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread

@Suppress("DeferredResultUnused")
abstract class BaseBlurPopup : BaseDialogFragment() {

    override fun onStart() {
        super.onStart()
        async {
            delay(10)
            runOnUiThread {
                getBlurLayout()?.beginBlur()
                setupPeekRecyclerViewSwipeGesture()
                doOnStart()
            }
        }
        getBlurLayout()?.onClick { dismiss() }
    }

    override fun onStop() {
        getBlurLayout()?.pauseBlur()
        doOnStop()
        super.onStop()
    }

    private fun setupPeekRecyclerViewSwipeGesture() {
        getDragHandle()?.setOnTouchListener(object : SwipeDownToDismissListener(activity!!, getDragHandle()!!, getRootView()!!, getBlurLayout()) {
            override fun onSwipeDown() {
                dismiss()
            }
        })
    }

    fun pushFragment(baseFragment: BaseFragment, isAddToBackStack: Boolean = false) {
        getParentActivity()?.pushFragment(baseFragment, isAddToBackStack)
    }

    private fun getParentActivity(): BaseCardActivity? {
        return activity as? BaseCardActivity
    }

    @CallSuper
    override fun dismiss() {
        getBlurLayout()?.fadeOut()
        doOnDismiss()
        async {
            delay(280)
            try {
                runOnUiThread { super.dismiss() }
            } catch (e: Exception) {
            }
        }
    }

    /**
     * Method for initializing items of the popup AFTER the onStart() is called and the transitions are finished
     */
    open fun doOnStart() {}

    open fun doOnStop() {}

    /**
     * Method for performing dismiss actions just before the dismiss() is called
     */
    open fun doOnDismiss() {}

    abstract fun getBlurLayout(): BlurLayout?

    abstract fun getDragHandle(): View?

    abstract fun getRootView(): View?

    abstract fun getBackgroundLayout(): ViewGroup?
}