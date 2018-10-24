package life.plank.juna.zone.view.activity.base

import android.support.v4.app.FragmentManager
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.findLastFragment
import life.plank.juna.zone.util.facilis.movePreviousCardToBackground
import life.plank.juna.zone.util.facilis.movePreviousCardToForeground
import life.plank.juna.zone.util.facilis.pushFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

abstract class BaseCardActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {

    var index: Int = 0

    protected fun pushFragment(fragment: BaseFragment, isAddToBackStack: Boolean = false) {
        if (index < 0) return

        if (index > 0) supportFragmentManager.movePreviousCardToBackground()

        supportFragmentManager.pushFragment(R.id.main_fragment_container, fragment, fragment.javaClass.simpleName + index, index, isAddToBackStack)
        index++
    }

    protected fun popBackStack() {
        if (index <= 0) return
        if (supportFragmentManager.backStackEntryCount > 0) {
            index--
            supportFragmentManager.popBackStackImmediate()
            supportFragmentManager.movePreviousCardToForeground()
        }
    }

    override fun onBackStackChanged() {
        index = supportFragmentManager.backStackEntryCount
    }

    override fun onDestroy() {
        index = 0
        super.onDestroy()
    }

    override fun onBackPressed() {
        try {
            if (supportFragmentManager.findLastFragment()!!.onBackPressed()) {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    popBackStack()
                } else {
                    super.onBackPressed()
                }
            } else super.onBackPressed()
        } catch (e: Exception) {
            super.onBackPressed()
        }
    }
}