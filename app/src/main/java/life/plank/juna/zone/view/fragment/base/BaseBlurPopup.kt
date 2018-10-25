package life.plank.juna.zone.view.fragment.base

import android.support.annotation.CallSuper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import io.alterac.blurkit.BlurLayout
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import life.plank.juna.zone.util.facilis.SwipeDownToDismissListener
import life.plank.juna.zone.util.facilis.listener
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread

@Suppress("DeferredResultUnused")
abstract class BaseBlurPopup : BaseDialogFragment() {

    override fun onStart() {
        super.onStart()
        async {
            delay(10)
            runOnUiThread {
                getBlurLayout().animate()
                        .alpha(1f)
                        .setDuration(100)
                        .listener { getBlurLayout().startBlur() }
                        .start()
                setupPeekRecyclerViewSwipeGesture()
            }
        }
        getBlurLayout().onClick { dismiss() }
    }

    override fun onStop() {
        getBlurLayout().pauseBlur()
        super.onStop()
    }

    private fun setupPeekRecyclerViewSwipeGesture() {
        getDragArea().setOnTouchListener(object : SwipeDownToDismissListener(activity!!, getDragArea(), getRootView(), getBackgroundLayout()) {
            override fun onSwipeDown() {
                dismiss()
            }
        })
    }

    @CallSuper
    override fun dismiss() {
        getBlurLayout().startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_out))
        async {
            delay(280)
            try {
                runOnUiThread { super.dismiss() }
            } catch (e: Exception) {
            }
        }
    }

    abstract fun getBlurLayout(): BlurLayout

    abstract fun getDragArea(): View

    abstract fun getRootView(): View

    abstract fun getBackgroundLayout(): ViewGroup
}