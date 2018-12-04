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
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.notification.triggerNotificationAction
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.facilis.onSwipeDown
import life.plank.juna.zone.util.facilis.sinkDown
import life.plank.juna.zone.util.facilis.then
import life.plank.juna.zone.util.view.UIDisplayUtil.getDp
import life.plank.juna.zone.view.activity.base.BaseCardActivity

class InAppNotificationLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var isShowing: Boolean = false
    private val animHandler: Handler = Handler()
    private val animRunnable = Runnable { detach() }
    private var parentActivity: BaseCardActivity? = null

    init {
        View.inflate(context, R.layout.item_in_app_notification, this)
        visibility = View.INVISIBLE
    }

    fun load(inAppNotification: InAppNotification, activity: BaseCardActivity? = null, dismissDelay: Long = 10000) {
        notification_message.text = inAppNotification.message
        notification_sub_message.text = inAppNotification.subMessage
        notification_sub_message.isSelected = true
        parentActivity = activity

//        Check if notification image URL is null or empty, assign imageUrl, or feedItemThumbnailUrl, or boardIconUrl
//        (whichever is not null gets assigned) in that precedence order
        inAppNotification.validateImageUrl()

//        If notification image URL is still null or empty, hide image view and expand the width of the message layout
        if (isNullOrEmpty(inAppNotification.imageUrl)) {
            notification_image.visibility = View.GONE
            (notification_message_layout.layoutParams as FrameLayout.LayoutParams).marginStart = 0
        }

        inAppNotification.imageUrl?.run {
            Glide.with(context).load(this)
                    .apply(RequestOptions.overrideOf(getDp(90f).toInt(), getDp(90f).toInt()))
                    .into(notification_image)
        }
        setListeners(inAppNotification)
        show(dismissDelay)
    }

    private fun InAppNotification.validateImageUrl() {
        if (!isNullOrEmpty(imageUrl)) {
            return
        }
        junaNotification?.run {
            if (!isNullOrEmpty(imageUrl)) {
                this@validateImageUrl.imageUrl = imageUrl
            }
            if (!isNullOrEmpty(feedItemThumbnailUrl)) {
                this@validateImageUrl.imageUrl = feedItemThumbnailUrl
            }
            if (!isNullOrEmpty(boardIconUrl)) {
                this@validateImageUrl.imageUrl = boardIconUrl
            }
        }
        if (isNullOrEmpty(imageUrl)) {
            zoneLiveData?.run {
                //                TODO: display homeTeamLogo and awayTeam logo separately
                imageUrl = homeTeamLogo
            }
        }
    }

    private fun setListeners(inAppNotification: InAppNotification) {
        parentActivity?.run {
            in_app_notification_card.onSwipeDown(this, null, null, { detach() }, {
                dismiss()?.then {
                    inAppNotification.run {
                        triggerNotificationAction(parentActivity)
                        parentActivity = null
                    }
                    detachFormParent()
                }
                return@onSwipeDown true
            })
        }
    }

    private fun show(dismissDelay: Long) {
        if (!isShowing) {
            floatUp()
            isShowing = true
            animHandler.postDelayed(animRunnable, dismissDelay)
        }
    }

    private fun detach() = dismiss()?.then {
        detachFormParent()
        parentActivity = null
    }

    private fun detachFormParent() = (parent as? ViewGroup)?.removeView(this)

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