package life.plank.juna.zone.view.activity.base

import android.support.v4.app.FragmentManager
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.movePreviousCardToBackground
import life.plank.juna.zone.util.facilis.movePreviousCardToForeground
import life.plank.juna.zone.util.facilis.pushCard

abstract class BaseCardActivity : BaseActivity(), FragmentManager.OnBackStackChangedListener {

    protected var index: Int = 0

    protected fun pushFragment(fragment: BaseCard, isAddToBackStack: Boolean) {
        if (index < 0) return

        if (index > 0) supportFragmentManager.movePreviousCardToBackground()

        supportFragmentManager.pushCard(R.id.main_fragment_container, fragment, fragment.javaClass.simpleName + index, index, isAddToBackStack)
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
        if (supportFragmentManager.backStackEntryCount > 0) {
            popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}