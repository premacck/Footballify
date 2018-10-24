package life.plank.juna.zone.util.facilis

import android.support.annotation.AnimRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import life.plank.juna.zone.R

fun FragmentManager.findCard(tag: String): BaseCard? {
    return this.findFragmentByTag(tag) as? BaseCard
}

fun FragmentManager.findLastCard(): BaseCard? {
    return this.fragments[this.backStackEntryCount] as? BaseCard
}

fun FragmentManager.movePreviousCardToBackground() {
    this.findLastCard()?.moveToBackGround()
}

fun FragmentManager.movePreviousCardToForeground() {
    if (this.backStackEntryCount >= 0) this.findLastCard()?.moveToForeGround()
}

fun FragmentManager.pushCard(resId: Int, card: BaseCard, tag: String, index: Int, isAddToBackStack: Boolean) {
    if (index < 0) return
    this.beginTransaction()
            .addCustomAnimations(index, R.anim.float_up, R.anim.sink_up, R.anim.float_down, R.anim.sink_down)
            .add(resId, card, tag)
            .addToBackStack(isAddToBackStack, tag)
            .commit()
    Log.i("pushCard", "Added index: $index")
}

fun FragmentTransaction.addCustomAnimations(index: Int, @AnimRes enter: Int, @AnimRes exit: Int, @AnimRes popEnter: Int, @AnimRes popExit: Int): FragmentTransaction {
    if (index > 0) this.setCustomAnimations(enter, exit, popEnter, popExit)
    return this
}

fun FragmentTransaction.addToBackStack(addFlag: Boolean, tag: String): FragmentTransaction {
    return if (addFlag) this.addToBackStack(tag) else this
}
