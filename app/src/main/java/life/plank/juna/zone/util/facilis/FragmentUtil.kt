package life.plank.juna.zone.util.facilis

import android.support.annotation.AnimRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import life.plank.juna.zone.R
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup
import life.plank.juna.zone.view.fragment.base.BaseDialogFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

fun FragmentManager.findCard(tag: String): BaseCard? {
    return this.findFragmentByTag(tag) as? BaseCard
}

fun FragmentManager.findLastCard(): BaseCard? {
    return fragments[fragments.size - 1] as? BaseCard
}

fun FragmentManager.findLastFragment(): BaseFragment? {
    return this.fragments[this.backStackEntryCount] as? BaseFragment
}

fun FragmentManager.findLastFragment(tag: String?): BaseFragment? {
    return findFragmentByTag(tag) as? BaseFragment
}

fun FragmentManager.findPopupDialog(tag: String): BaseBlurPopup? {
    return findFragmentByTag(tag) as? BaseBlurPopup
}

fun FragmentManager.removeActivePopupsIfAny(): Boolean {
    for (popup in fragments.reversed()) {
        if (popup is BaseBlurPopup && popup.isAdded) {
            popup.dismiss()
            beginTransaction().remove(popup).commit()
            return false
        }
    }
    return true
}

fun FragmentManager.removeActiveCardsIfAny(): Boolean {
    for (card in fragments.reversed()) {
        if (card is BaseCard && card.isAdded) {
            return if (card.onBackPressed()) {
                beginTransaction().remove(card).commit()
                false
            } else true
        }
    }
    return true
}

fun FragmentManager.moveCurrentCardToBackground(tag: String?) {
    val lastFragment = findFragmentByTag(tag)
    lastFragment?.run {
        if (lastFragment is BaseCard) {
            lastFragment.moveToBackGround()
        }
        onPause()
    }
}

fun FragmentManager.movePreviousCardToForeground(tag: String?): String? {
    val lastFragment = findLastFragment(tag)
    lastFragment?.run {
        if (lastFragment is BaseCard) {
            lastFragment.moveToForeGround()
        }
        onResume()
        return lastFragment.previousFragmentTag
    }
    return null
}

fun FragmentManager.pushFragment(resId: Int, card: BaseFragment, tag: String, index: Int, isAddToBackStack: Boolean = true) {
    if (index < 0) return
    this.beginTransaction()
            .addCustomAnimations(R.anim.float_up, R.anim.sink_up, R.anim.float_down, R.anim.sink_down, index)
            .add(resId, card, tag)
            .addToBackStack(isAddToBackStack, tag)
            .commit()
    Log.i("pushFragment", "Added index: $index")
}

fun FragmentManager.pushPopup(resId: Int, popup: BaseDialogFragment, tag: String) {
    this.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(resId, popup, tag)
            .addToBackStack(tag)
            .commit()
}

fun FragmentTransaction.addCustomAnimations(@AnimRes enter: Int, @AnimRes exit: Int, @AnimRes popEnter: Int, @AnimRes popExit: Int, index: Int = 0): FragmentTransaction {
    if (index > 0) this.setCustomAnimations(enter, exit, popEnter, popExit)
    return this
}

fun FragmentTransaction.addToBackStack(addFlag: Boolean = true, tag: String): FragmentTransaction {
    return if (addFlag) this.addToBackStack(tag) else this
}
