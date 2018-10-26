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
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -getDp(24f)),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 0.92f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.92f)
    ).prepare()
//    objectAnimator.addUpdateListener {
//        this.setCardBackgroundColor(
//                ColorUtils.blendARGB(
//                        Color.TRANSPARENT,
//                        this.resources.getColor(R.color.others_color_grey),
//                        it.animatedFraction
//                )
//        )
//    }
    objectAnimator.start()
    elevation = 0f
    toggleInteraction(false)
    toggleInteraction(false)
}

fun CardView.moveToForeGround() {
    val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
    ).prepare()
//    objectAnimator.addUpdateListener {
//        this.setCardBackgroundColor(
//                ColorUtils.blendARGB(
//                        this.resources.getColor(R.color.others_color_grey),
//                        Color.TRANSPARENT,
//                        it.animatedFraction
//                )
//        )
//    }
    objectAnimator.start()
    elevation = getDp(8f)
    toggleInteraction(true)
    toggleInteraction(true)
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