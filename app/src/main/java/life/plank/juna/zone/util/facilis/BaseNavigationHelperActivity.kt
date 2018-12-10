package life.plank.juna.zone.util.facilis

import android.support.annotation.IdRes
import android.util.Log
import life.plank.juna.zone.util.view.dismissBoomMenuIfOpen
import life.plank.juna.zone.view.activity.base.BaseActivity
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseNavigationHelperActivity : BaseActivity() {

    var index: Int = 0

    fun pushPopup(popupDialog: BaseDialogFragment) {
        index++
        supportFragmentManager.pushPopup(getFragmentContainer(), popupDialog, popupDialog.javaClass.simpleName + index)
    }

    fun pushFragment(fragment: BaseFragment, isAddToBackStack: Boolean = true) {
        if (index >= 0) {
            supportFragmentManager.moveCurrentCardToBackground()
        }

        if (isAddToBackStack) index++
        supportFragmentManager.pushFragment(getFragmentContainer(), fragment, fragment.javaClass.simpleName + index, index, isAddToBackStack)
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
                if (dismissBoomMenuIfOpen()) {
                    if (index > 0) {
                        startPoppingFragment()
                    } else super.onBackPressed()
                }
            }
        } catch (e: Exception) {
            Log.e("onBackPressed()", "ERROR : ", e)
            startPoppingFragment()
        }
    }
}