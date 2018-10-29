@file:Suppress("DEPRECATION")

package life.plank.juna.zone.util.facilis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Vibrator
import android.support.design.widget.CoordinatorLayout
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import life.plank.juna.zone.ZoneApplication
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

fun View.moveToBackGround(index: Int) {
    translateScaleAnimation(-getDp(if (index > 1) 44f else 24f), 0.92f).prepare().start()
    elevation = 0f
    toggleInteraction(false)
    toggleInteraction(false)
}

fun View.moveToForeGround() {
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

fun View.onCustomLongClick(longClickDelay: Int = 300, action: () -> Unit) {
    setOnTouchListener(getCustomOnLongClickListener(longClickDelay) { action() })
}

private fun View.getCustomOnLongClickListener(longClickDelay: Int = 300, action: () -> Unit): View.OnTouchListener {
    return object : View.OnTouchListener {
        private var isLongPress = false

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if (event?.action == MotionEvent.ACTION_DOWN) {
                isLongPress = true
                this@getCustomOnLongClickListener.postDelayed({
                    if (isLongPress) {
                        isLongPress = false
                        val vibrator = ZoneApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(50)
                        action()
                    }
                }, longClickDelay.toLong())
            } else if (event?.action == MotionEvent.ACTION_UP) {
                if (isLongPress) {
                    isLongPress = false
                    performClick()
                }
                return false
            }
            return true
        }
    }
}