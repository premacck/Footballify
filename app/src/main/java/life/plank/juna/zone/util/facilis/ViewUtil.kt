package life.plank.juna.zone.util.facilis

import android.animation.*
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.*
import android.view.*
import android.view.MotionEvent.*
import android.view.animation.*
import android.widget.*
import androidx.annotation.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.ahamed.multiviewadapter.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.leocardz.link.preview.library.*
import io.alterac.blurkit.BlurLayout
import kotlinx.coroutines.*
import life.plank.juna.zone.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.FeedItem
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.DataUtil.*
import life.plank.juna.zone.util.view.UIDisplayUtil.*
import life.plank.juna.zone.view.adapter.common.EmojiAdapter
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.sdk27.coroutines.*

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

fun <T : View> Array<T>.makeVisible() = forEach { if (it.visibility != View.VISIBLE) it.visibility = View.VISIBLE }

fun <T : View> Array<T>.makeInvisible() = forEach { if (it.visibility != View.INVISIBLE) it.visibility = View.INVISIBLE }

fun <T : View> Array<T>.makeGone() = forEach { if (it.visibility != View.GONE) it.visibility = View.GONE }

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

fun Context.animatorOf(@AnimatorRes animatorRes: Int): Animator = AnimatorInflater.loadAnimator(this, animatorRes)

fun View.animatorOf(@AnimatorRes animatorRes: Int): Animator {
    val animator = context.animatorOf(animatorRes)
    animator.setTarget(this)
    return animator
}

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

fun View.onFancyClick(launchDelay: Long = 100, action: () -> Unit) {
    lateinit var originPoint: ArrayList<Float>
    onTouch { _, event ->
        when (event.action) {
            ACTION_DOWN -> {
                originPoint = arrayListOf(event.rawX, event.rawY)
                animatorOf(R.animator.reduce_size).start()
            }
            ACTION_CANCEL, ACTION_UP -> animatorOf(R.animator.original_size).start()
            ACTION_MOVE -> {
                if (isNullOrEmpty(originPoint)) originPoint = arrayListOf(event.rawX, event.rawY)

                if (!isNullOrEmpty(originPoint)) {
                    val deltaX = Math.abs(originPoint[0] - event.rawX)
                    val deltaY = Math.abs(originPoint[1] - event.rawY)
                    if (deltaX > 1 && deltaY > 1) {
                        animatorOf(R.animator.original_size).start()
                    }
                }
            }
        }
    }
    onDebouncingClick { this.context.doAfterDelay(launchDelay) { action() } }
}

fun View.onElevatingClick(launchDelay: Long = 100, action: () -> Unit) {
    lateinit var originPoint: ArrayList<Float>
    val originalElevation = elevation
    val finalElevation = elevation + getDp(4f)
    val elevateUpAnimation = ValueAnimator.ofFloat(originalElevation, finalElevation).setDuration(80)
    elevateUpAnimation.interpolator = DecelerateInterpolator()
    elevateUpAnimation.addUpdateListener { elevation = it.animatedValue as Float }

    val elevateDownAnimation = ValueAnimator.ofFloat(finalElevation, originalElevation).setDuration(100)
    elevateDownAnimation.addUpdateListener { elevation = it.animatedValue as Float }
    elevateDownAnimation.interpolator = DecelerateInterpolator()
    elevateDownAnimation.startDelay = 80
    onTouch { _, event ->
        when (event.action) {
            ACTION_DOWN -> {
                originPoint = arrayListOf(event.rawX, event.rawY)
                elevateUpAnimation.start()
            }
            ACTION_CANCEL, ACTION_UP -> elevateDownAnimation.start()
            ACTION_MOVE -> {
                if (isNullOrEmpty(originPoint)) originPoint = arrayListOf(event.rawX, event.rawY)

                if (!isNullOrEmpty(originPoint)) {
                    val deltaX = Math.abs(originPoint[0] - event.rawX)
                    val deltaY = Math.abs(originPoint[1] - event.rawY)
                    if (deltaX > 1 && deltaY > 1) {
                        elevateDownAnimation.start()
                    }
                }
            }
        }
    }
    onDebouncingClick { this.context.doAfterDelay(launchDelay) { action() } }
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

fun Fragment.doAfterDelay(delayMillis: Long, action: () -> Unit) = context?.doAfterDelay(delayMillis, action)

@Suppress("DeferredResultUnused")
fun Context?.doAfterDelay(delayMillis: Long, action: () -> Unit) {
    GlobalScope.async {
        delay(delayMillis)
        this@doAfterDelay?.run { runOnUiThread { action() } }
    }
}

fun BottomSheetBehavior<*>.show(peekHeight: Int = 850) {
    state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
    this.peekHeight = peekHeight
}

fun BottomSheetBehavior<*>.showFor(emojiAdapter: EmojiAdapter?, id: String?, peekHeight: Int = 850) {
    emojiAdapter?.update(id!!)
    show(peekHeight)
}

fun BottomSheetBehavior<*>.hide() {
    state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
    peekHeight = 0
}

fun BottomSheetBehavior<*>.hideIfShown(): Boolean {
    if (peekHeight == 0 || state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN || state == com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED) {
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

fun View.setGradientBackground(drawableText: String) {
    when (drawableText) {
        findString(R.string.blue_color) -> background = findDrawable(R.drawable.blue_gradient)
        findString(R.string.purple_color) -> background = findDrawable(R.drawable.purple_gradient)
        findString(R.string.green_color) -> background = findDrawable(R.drawable.green_gradient)
        findString(R.string.orange_color) -> background = findDrawable(R.drawable.orange_gradient)
    }
}

fun TextView.setRootCommentPost(feedItem: FeedItem) {
    if (!isNullOrEmpty(feedItem.backgroundColor)) {
        feedItem.backgroundColor?.run { setGradientBackground(get(0)) }
        text = feedItem.title.formatLinks().formatMentions()
    } else {
//        TODO: remove comment $ prepend usage
        val comment: String = feedItem.title.replace("^\"|\"$".toRegex(), "")
        background = getCommentColor(comment)
        text = getCommentText(comment)?.toString()?.formatLinks()?.formatMentions()
    }
}