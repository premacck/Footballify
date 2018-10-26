@file:Suppress("DEPRECATION")

package life.plank.juna.zone.util.facilis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.graphics.Point
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import life.plank.juna.zone.util.UIDisplayUtil.getDp

fun Display.getScreenSize(): IntArray {
    val size = Point()
    getSize(size)
    return intArrayOf(size.x, size.y)
}

fun View.toggleInteraction(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.isClickable = isEnabled
}

fun CardView.moveToBackGround() {
    translateScaleAnimation(24f, 0.92f).prepare().start()
    elevation = 0f
    toggleInteraction(false)
    toggleInteraction(false)
}

fun CardView.moveToForeGround() {
    translateScaleAnimation(0f, 1f).prepare().start()
    elevation = getDp(8f)
    toggleInteraction(true)
    toggleInteraction(true)
}

fun View.translateScaleAnimation(translateValue: Float, scaleValue: Float): ObjectAnimator {
    return ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, translateValue),
            PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
    )
}

fun ObjectAnimator.prepare(): ObjectAnimator {
    this.duration = 200
    this.interpolator = DecelerateInterpolator()
    return this
}

fun View.setSwipeDownListener(activity: Activity, rootView: View, backgroundLayout: ViewGroup? = null) {
    this.setOnTouchListener(object : SwipeDownToDismissListener(activity, this@setSwipeDownListener, rootView, backgroundLayout) {
        override fun onSwipeDown() {
            activity.onBackPressed()
        }
    })
}

fun ViewPropertyAnimator.listener(onAnimationEnd: () -> Unit): ViewPropertyAnimator {
    return this.setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) = onAnimationEnd()
    })
}

fun View.fadeOut() {
    animate().alpha(0f).setDuration(280).start()
}

fun View.setTopMargin(topMargin: Int) {
    val params = layoutParams
    params?.run {
        when (params) {
            is RelativeLayout.LayoutParams -> params.topMargin = topMargin
            is CoordinatorLayout.LayoutParams -> params.topMargin = topMargin
            is FrameLayout.LayoutParams -> params.topMargin = topMargin
            is LinearLayout.LayoutParams -> params.topMargin = topMargin
        }
        layoutParams = params
    }
}