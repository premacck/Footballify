package life.plank.juna.zone.util.facilis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.annotation.AnimRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.*
import com.ahamed.multiviewadapter.*
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import io.alterac.blurkit.BlurLayout
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.common.URL_PATTERN
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.adapter.common.EmojiAdapter
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

fun Display.getScreenSize(): IntArray {
    val size = Point()
    getSize(size)
    return intArrayOf(size.x, size.y)
}

fun View.makeVisible() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.makeInvisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.makeGone() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun View.toggleInteraction(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    this.isClickable = isEnabled
}

fun View.moveToBackGround(index: Int) {
    translateScaleAnimation(-getDp(if (index > 0) 44f else 24f), 0.92f).prepare().start()
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
    onSwipeDown(activity, rootView, backgroundLayout, { activity.onBackPressed() })
}

fun View.onSwipeDown(activity: Activity, rootView: View? = null, backgroundLayout: ViewGroup? = null, swipeDownAction: () -> Unit, singleTapAction: () -> Boolean = { false }) {
    setOnTouchListener(object : SwipeDownToDismissListener(
            activity,
            this@onSwipeDown,
            rootView ?: this@onSwipeDown,
            backgroundLayout
    ) {
        override fun onSwipeDown() = swipeDownAction()
        override fun onSingleTap(): Boolean = singleTapAction()
    })
}

fun ViewPropertyAnimator.listener(onAnimationEnd: () -> Unit): ViewPropertyAnimator {
    return this.setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) = onAnimationEnd()
    })
}

fun View.animate(@AnimRes anim: Int, delayMillis: Long = 0): Animation {
    val animation = AnimationUtils.loadAnimation(context, anim)
    postDelayed({ startAnimation(animation) }, delayMillis)
    return animation
}

fun Animation.then(action: (animation: Animation) -> Unit): Animation {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            action(animation)
        }
    })
    return this
}

fun View.fadeIn() = animate().alpha(1f).setDuration(280).start()

fun View.fadeOut() = animate().alpha(0f).setDuration(280).start()

fun View.floatDown(delayMillis: Long = 50) = animate(R.anim.float_down, delayMillis).then { visibility = View.VISIBLE }

fun View.floatUp(delayMillis: Long = 20) = animate(R.anim.float_up, delayMillis).then { visibility = View.VISIBLE }

fun View.sinkDown() = animate(R.anim.sink_down).then { visibility = View.INVISIBLE }

fun View.sinkUp() = animate(R.anim.sink_up).then { visibility = View.INVISIBLE }

fun BlurLayout.beginBlur() {
    postDelayed({
        startBlur()
        visibility = View.VISIBLE
    }, 10)
}

fun View.zoomOut() = animate(R.anim.zoom_out)

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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(millis)
    }
}

fun View.longClickWithVibrate(action: () -> Unit) {
    setOnLongClickListener {
        vibrate(20)
        action()
        return@setOnLongClickListener true
    }
}

fun View.onCustomLongClick(longClickDelay: Int = 300, action: () -> Unit) = setOnTouchListener(getCustomOnLongClickListener(longClickDelay) { action() })

fun View.onDebouncingClick(action: () -> Unit) {
    setOnClickListener {
        if (isEnabled) {
            isEnabled = false
            action()
            postDelayed({ isEnabled = true }, 100)
        }
    }
}

fun View.clearOnClickListener() = setOnClickListener(null)

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

@Suppress("DeferredResultUnused")
fun Context?.doAfterDelay(delayMillis: Int, action: () -> Unit) {
    async {
        delay(delayMillis)
        this@doAfterDelay?.run { runOnUiThread { action() } }
    }
}

fun BottomSheetBehavior<*>.show(peekHeight: Int = 850) {
    state = BottomSheetBehavior.STATE_EXPANDED
    this.peekHeight = peekHeight
}

fun BottomSheetBehavior<*>.showFor(emojiAdapter: EmojiAdapter?, id: String?, peekHeight: Int = 850) {
    emojiAdapter?.update(id!!)
    show(peekHeight)
}

fun BottomSheetBehavior<*>.hide() {
    state = BottomSheetBehavior.STATE_HIDDEN
    peekHeight = 0
}

fun BottomSheetBehavior<*>.hideIfShown(): Boolean {
    if (peekHeight == 0 || state == BottomSheetBehavior.STATE_HIDDEN || state == BottomSheetBehavior.STATE_COLLAPSED) {
        return true
    }
    hide()
    return false
}

fun TextView.setEmoji(emoji: Int) {
    text = StringBuilder().appendCodePoint(emoji).toString()
}

inline fun <reified T : View> ViewGroup.getIfPresent(): T? {
    if (childCount <= 0) {
        return null
    }
    for (i in 0..childCount) {
        val child = getChildAt(i)
        if (child is T) {
            return child
        }
    }
    return null
}

fun <BM> RecyclerAdapter.addDataManagerAndRegisterBinder(dataManager: DataItemManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
    addDataManager(dataManager)
    registerBinder(binderToRegister)
}

fun <BM> RecyclerAdapter.addDataManagerAndRegisterBinder(dataManager: DataListManager<BM>, binderToRegister: ItemBinder<BM, out ItemViewHolder<BM>>) {
    addDataManager(dataManager)
    registerBinder(binderToRegister)
}

fun View.dragHandle(): ImageView? = findViewById(R.id.drag_handle_image)

fun BaseCard.dragHandle(): ImageView? = getRootView()?.dragHandle()

fun TextCrawler.beginPreview(url: String, onPostAction: (sourceContent: SourceContent?, isNull: Boolean) -> Unit, onPreAction: () -> Unit = {}) {
    val matcher = URL_PATTERN.matcher(url)
    var isFirstLinkRecognized = false
    while (matcher.find()) {
        if (!isFirstLinkRecognized) {
            isFirstLinkRecognized = true
            makePreview(object : LinkPreviewCallback {
                override fun onPre() = onPreAction()
                override fun onPos(sourceContent: SourceContent?, isNull: Boolean) = onPostAction(sourceContent, isNull)
            }, url)
        }
    }
}