package life.plank.juna.zone.view.activity.base

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.*
import life.plank.juna.zone.view.activity.home.HomeActivity
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseCardActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {

    var index: Int = 0
    private var previousFragmentTag: String? = null
    private var currentFragmentTag: String? = null

    fun pushPopup(popupDialog: BaseDialogFragment) {
        if (getFragmentContainer() == -1) throw IllegalStateException(getString(R.string.no_id_for_fragment_container))

        supportFragmentManager.pushPopup(getFragmentContainer(), popupDialog, popupDialog.javaClass.simpleName)
    }

    fun pushFragment(fragment: BaseFragment, isAddToBackStack: Boolean = false) {
        if (getFragmentContainer() == -1) throw IllegalStateException(getString(R.string.no_id_for_fragment_container))

        if (index < 0) return

        if (index > 0) {
            previousFragmentTag = currentFragmentTag
            fragment.previousFragmentTag = previousFragmentTag
            supportFragmentManager.moveCurrentCardToBackground(previousFragmentTag)
        }

        currentFragmentTag = fragment.javaClass.simpleName + index
        supportFragmentManager.pushFragment(getFragmentContainer(), fragment, currentFragmentTag!!, index, isAddToBackStack)
        index++
    }

    fun popBackStack() {
        if (index <= 0) return
        index--
        supportFragmentManager.popBackStackImmediate()
        currentFragmentTag = previousFragmentTag
        previousFragmentTag = supportFragmentManager.movePreviousCardToForeground(previousFragmentTag)
    }

    override fun onBackStackChanged() {
        index = supportFragmentManager.backStackEntryCount
    }

    private fun startPoppingFragment() {
        val lastFragment = supportFragmentManager.findLastFragment(currentFragmentTag)
        if (lastFragment != null) {
            if (lastFragment.onBackPressed()) {
                if (index > 0) {
                    popBackStack()
                } else super.onBackPressed()
            } // Do nothing here if the fragment's onBackPressed() returns false
        } else super.onBackPressed()
    }

    @IdRes
    abstract fun getFragmentContainer(): Int

    override fun onBackPressed() {
        try {
            if (this is HomeActivity) {
                supportFragmentManager.findLastFragment()?.run {
                    if (onBackPressed()) {
                        if (supportFragmentManager.backStackEntryCount > 0) {
                            popBackStack()
                        } else {
                            super.onBackPressed()
                        }
                    }
                }
                return
            }
            if (!isNullOrEmpty(previousFragmentTag)) {
                startPoppingFragment()
            } else super.onBackPressed()
        } catch (e: Exception) {
            Log.e("onBackPressed()", "ERROR : ", e)
            startPoppingFragment()
        }
    }
}