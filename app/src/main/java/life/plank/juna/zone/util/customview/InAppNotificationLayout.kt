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

        inAppNotification.updateImageViews()

        setListeners(inAppNotification)
        show(dismissDelay)
    }

    private fun InAppNotification.updateImageViews() {
        when {
            junaNotification != null -> {
                image_layout.visibility = View.INVISIBLE

                if (isNullOrEmpty(imageUrl)) {
                    notification_image.visibility = View.GONE
                    (notification_message_layout.layoutParams as FrameLayout.LayoutParams).marginStart = 0
                }

                imageUrl?.run {
                    Glide.with(context).load(this)
                            .apply(RequestOptions.overrideOf(getDp(85f).toInt(), getDp(85f).toInt()))
                            .into(notification_image)
                }
            }
            zoneLiveData != null -> {
                notification_image.setImageDrawable(resources.getDrawable(R.drawable.ic_match_bg, null))
                Glide.with(context).load(zoneLiveData?.homeTeamLogo)
                        .apply(RequestOptions.overrideOf(getDp(30f).toInt(), getDp(30f).toInt())
                                .placeholder(R.drawable.shimmer_circle))
                        .into(home_team_logo)
                Glide.with(context).load(zoneLiveData?.visitingTeamLogo)
                        .apply(RequestOptions.overrideOf(getDp(30f).toInt(), getDp(30f).toInt())
                                .placeholder(R.drawable.shimmer_circle))
                        .into(visiting_team_logo)
            }
        }
    }

    private fun setListeners(inAppNotification: InAppNotification) {
        parentActivity?.run {
            in_app_notification_card.onSwipeDown(this, null, null, { detach() }, {
                dismiss().then {
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

    private fun detach() = dismiss().then {
        detachFormParent()
        parentActivity = null
    }

    private fun detachFormParent() = (parent as? ViewGroup)?.removeView(this)

    fun dismiss(): Animation {
//        TODO: add notification read API call when it's ready
        if (isShowing) {
            isShowing = false
            animHandler.removeCallbacks(animRunnable)
        } else {
            detachFormParent()
            parentActivity = null
        }
        return sinkDown()
    }
}