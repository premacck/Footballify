package life.plank.juna.zone.util.customview

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_in_app_notification.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTarget
import life.plank.juna.zone.util.facilis.floatDown
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.sinkUp

class InAppNotificationLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var isShowing: Boolean = false
    private val animHandler: Handler = Handler()
    private val animRunnable = Runnable { dismiss() }

    init {
        View.inflate(context, R.layout.item_in_app_notification, this)
        visibility = View.INVISIBLE

        notification_action.onDebouncingClick { dismiss() }
    }

    fun load(inAppNotification: InAppNotification) {
        notification_message.text = inAppNotification.message
        notification_message.isSelected = true

        inAppNotification.imageUrl?.run {
            Glide.with(context).load(this)
                    .apply(RequestOptions.circleCropTransform()
                            .centerCrop()
                            .override(getDp(24f).toInt(), getDp(24f).toInt()))
                    .into(getStartDrawableTarget(notification_message))
        }
        show()
    }

    private fun show() {
        if (!isShowing) {
            floatDown()
            isShowing = true
            animHandler.postDelayed(animRunnable, 4000)
        }
    }

    fun dismiss() {
//        TODO: add notification read API call when it's ready
        if (isShowing) {
            sinkUp()
            isShowing = false
            animHandler.removeCallbacks(animRunnable)
        }
    }
}