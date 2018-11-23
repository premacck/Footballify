package life.plank.juna.zone.view.activity.base

import android.support.annotation.IdRes
import android.util.Log
import android.view.ViewGroup
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.notification.InAppNotification
import life.plank.juna.zone.util.customview.InAppNotificationLayout
import life.plank.juna.zone.util.facilis.*
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseCardActivity : BaseActivity() {

    var index: Int = 0

    fun pushPopup(popupDialog: BaseDialogFragment) {
        if (getFragmentContainer() == -1) throw IllegalStateException(getString(R.string.no_id_for_fragment_container))

        if (index < 0) return

        index++
        supportFragmentManager.pushPopup(getFragmentContainer(), popupDialog, popupDialog.javaClass.simpleName + index)
    }

    fun pushFragment(fragment: BaseFragment, isAddToBackStack: Boolean = false) {
        if (getFragmentContainer() == -1) throw IllegalStateException(getString(R.string.no_id_for_fragment_container))

        if (index >= 0) {
            supportFragmentManager.moveCurrentCardToBackground()
        }

        if (isAddToBackStack) index++
        supportFragmentManager.pushFragment(getFragmentContainer(), fragment, fragment.javaClass.simpleName + index, index, isAddToBackStack)
    }

    fun showNotification(inAppNotification: InAppNotification) {
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

    fun popBackStack() {
        if (index <= 0) return
        index--
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.movePreviousCardToForeground()
    }

    private fun startPoppingFragment() {
        supportFragmentManager.findLastFragment()?.run {
            if (this.onBackPressed()) {
                if (index > 0) {
                    popBackStack()
                } else super.onBackPressed()
            } // Do nothing here if the fragment's onBackPressed() returns false
        } ?: super.onBackPressed()
    }

    @IdRes
    abstract fun getFragmentContainer(): Int

    private fun removeActivePopupsIfAny(): Boolean {
        for (popup in supportFragmentManager.fragments.reversed()) {
            if (popup is BaseDialogFragment && popup.isAdded) {
                if (popup.onBackPressed()) {
                    popup.smartDismiss {
                        index--
                        supportFragmentManager.popBackStackImmediate()
                    }
                }
                return false
            }
        }
        return true
    }

    override fun onBackPressed() {
        try {
            if (removeActivePopupsIfAny()) {
                if (index > 0) {
                    startPoppingFragment()
                } else super.onBackPressed()
            }
        } catch (e: Exception) {
            Log.e("onBackPressed()", "ERROR : ", e)
            startPoppingFragment()
        }
    }
}