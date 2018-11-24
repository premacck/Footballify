package life.plank.juna.zone.view.activity.base

import android.view.ViewGroup
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.util.customview.InAppNotificationLayout
import life.plank.juna.zone.util.facilis.BaseNavigationHelperActivity
import life.plank.juna.zone.util.facilis.getIfPresent
import life.plank.juna.zone.util.facilis.then

abstract class BaseCardActivity : BaseNavigationHelperActivity() {

    fun showInAppNotification(inAppNotification: InAppNotification) {
        val container = findViewById<ViewGroup>(getFragmentContainer())
        val notificationLayout = container.getIfPresent<InAppNotificationLayout>()

        if (notificationLayout == null) {
            addNotificationView(container, inAppNotification)
            return
        }
        notificationLayout.dismiss()?.then {
            container.removeView(notificationLayout)
            addNotificationView(container, inAppNotification)
        }
    }

    private fun addNotificationView(container: ViewGroup, inAppNotification: InAppNotification) {
        val notificationLayout = InAppNotificationLayout(this)
        container.addView(notificationLayout)
        notificationLayout.load(inAppNotification)
    }
}