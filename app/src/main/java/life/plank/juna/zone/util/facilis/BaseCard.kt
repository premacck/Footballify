package life.plank.juna.zone.util.facilis

import android.app.Activity
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.view.activity.base.BaseCardActivity

abstract class BaseCard : Fragment() {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adjustWithIndex(getParentActivity().index)
        activity?.let { setupSwipeDownGesture(it) }
    }

    private fun adjustWithIndex(index: Int) {
        getRootFadedCardLayout().visibility = if (index > 1) View.VISIBLE else View.GONE

        getRootCard().run {
            val params = layoutParams as RelativeLayout.LayoutParams
            params.topMargin = getDp(if (index > 1) 20f else 0f).toInt()
            layoutParams = params
        }
    }

    private fun setupSwipeDownGesture(activity: Activity) {
        getDragHandle().setSwipeDownListener(activity, getRootCard(), getRootFadedCardLayout())
    }

    fun moveToBackGround() {
        getRootCard().moveToBackGround()
    }

    fun moveToForeGround() {
        getRootCard().moveToForeGround()
    }

    fun dispose() {
        getDragHandle().setOnTouchListener(null)
    }

    abstract fun getRootFadedCardLayout(): ViewGroup

//    TODO: un-comment if required
//    abstract fun getFadedCard(): CardView

    abstract fun getRootCard(): CardView

    abstract fun getDragHandle(): View

    protected fun getParentActivity(): BaseCardActivity {
        if (activity is BaseCardActivity)
            return activity as BaseCardActivity
        else throw IllegalStateException("Fragment must be attached to a BaseCardActivity")
    }

    override fun onDestroyView() {
        dispose()
        super.onDestroyView()
    }
}