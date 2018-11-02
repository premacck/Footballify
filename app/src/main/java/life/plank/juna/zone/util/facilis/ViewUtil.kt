@file:Suppress("DEPRECATI()ON")

package life.plank.juna.zone.util.facilis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.SystemClock
import android.os.Vibrator
import android.support.design.widget.CoordinatorLayout
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import io.alterac.blurkit.BlurLayout
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

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

fun View.floatUp() {
    postDelayed({
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.float_up))
        visibility = View.VISIBLE
    }, 20)
}

fun View.sinkDown() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.sink_down))
}

fun BlurLayout.beginBlur() {
    postDelayed({
        startBlur()
        visibility = View.VISIBLE
    }, 10)
}

fun View.zoomOut() {
    val zoomOutAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
    zoomOutAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            visibility = View.INVISIBLE
        }
    })
    startAnimation(zoomOutAnimation)
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

fun vibrate(millis: Long) {
    val vibrator = ZoneApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(millis)
}

fun View.longClickWithVibrate(action: () -> Unit) {
    setOnLongClickListener {
        vibrate(20)
        action()
        return@setOnLongClickListener true
    }
}

fun View.onCustomLongClick(longClickDelay: Int = 300, action: () -> Unit) {
    setOnTouchListener(getCustomOnLongClickListener(longClickDelay) { action() })
}

fun View.onDebouncingClick(action: () -> Unit) {
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            action()
            postDelayed({ isEnabled = true }, 100)
        }
    }
}

private fun View.getCustomOnLongClickListener(longClickDelay: Int = 300, action: () -> Unit): View.OnTouchListener {
    return object : View.OnTouchListener {
        private var isLongPress = false
        private lateinit var originPoint: FloatArray
        private lateinit var latestPoint: FloatArray
        private var downTime: Long = 0

        private fun arePointsWithinBounds(): Boolean {
            val deltaX = Math.abs(originPoint[0] - latestPoint[0])
            val deltaY = Math.abs(originPoint[1] - latestPoint[1])
            return deltaX <= 15 && deltaY <= 15
        }

        private fun isTimeWithinBounds(): Boolean {
            return Math.abs(downTime - SystemClock.elapsedRealtime()) <= longClickDelay
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    downTime = SystemClock.elapsedRealtime()
                    originPoint = floatArrayOf(event.rawX, event.rawY)
                    latestPoint = originPoint
                    isLongPress = true
                    this@getCustomOnLongClickListener.postDelayed({
                        if (isLongPress && arePointsWithinBounds()) {
                            isLongPress = false
                            vibrate(20)
                            action()
                        }
                    }, longClickDelay.toLong())
                }
                MotionEvent.ACTION_UP -> {
                    if (isLongPress && arePointsWithinBounds() && isTimeWithinBounds()) {
                        isLongPress = false
                        performClick()
                        return false
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    latestPoint = floatArrayOf(event.rawX, event.rawY)
                }
            }
            return true
        }
    }
}

inline fun <reified T : View> Array<T>.onClick(crossinline action: (view: View) -> Unit) {
    for (view in this) {
        view.onDebouncingClick { action(view) }
    }
}

inline fun <reified T : View> Array<T>.onTextChanged(crossinline action: () -> Unit) {
    for (view in this) {
        if (view is EditText) {
            view.textChangedListener { onTextChanged { _, _, _, _ -> action() } }
        }
    }
}