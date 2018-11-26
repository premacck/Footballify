package life.plank.juna.zone.util.customview

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_in_app_notification.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.notification.getNotificationIntent
import life.plank.juna.zone.util.UIDisplayUtil.getDp
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.sinkDown
import life.plank.juna.zone.util.facilis.then

class InAppNotificationLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var isShowing: Boolean = false
    private val animHandler: Handler = Handler()
    private val animRunnable = Runnable { dismiss()?.then { (parent as? ViewGroup)?.removeView(this) } }

    init {
        View.inflate(context, R.layout.item_in_app_notification, this)
        visibility = View.INVISIBLE
    }

    fun load(inAppNotification: InAppNotification) {
        notification_message.text = inAppNotification.message
        notification_sub_message.text = inAppNotification.subMessage
        notification_sub_message.isSelected = true

        inAppNotification.imageUrl?.run {
            Glide.with(context).load(this)
                    .apply(RequestOptions.overrideOf(getDp(90f).toInt(), getDp(90f).toInt()))
                    .into(notification_image)
        }
        in_app_notification_card.onDebouncingClick {
            ZoneApplication.getContext().startActivity(inAppNotification.junaNotification?.getNotificationIntent())
        }
        show()
    }

    private fun show() {
        if (!isShowing) {
            floatUp()
            isShowing = true
            animHandler.postDelayed(animRunnable, 4000)
        }
    }

    fun dismiss(): Animation? {
//        TODO: add notification read API call when it's ready
        if (isShowing) {
            isShowing = false
            animHandler.removeCallbacks(animRunnable)
            return sinkDown()
        }
        return null
    }
}