package life.plank.juna.zone.util.common

import android.support.v4.view.PagerAdapter
import life.plank.juna.zone.R
import life.plank.juna.zone.notification.getIntentActionFromActivity
import life.plank.juna.zone.view.fragment.base.CardTileFragment

fun CardTileFragment.getPositionFromIntentIfAny(pagerAdapter: PagerAdapter?): Int {
    return getIntentActionFromActivity()?.run {
        when (this) {
            getString(R.string.intent_post), getString(R.string.intent_react) -> pagerAdapter.positionOf(getString(R.string.tiles))
            getString(R.string.intent_comment) -> pagerAdapter.positionOf(getString(R.string.forum))
            else -> pagerAdapter.positionOf(getString(R.string.tiles))
        }
    } ?: pagerAdapter.positionOf(getString(R.string.tiles))
}

fun PagerAdapter?.positionOf(tabName: String): Int = this?.run { getItemPosition(tabName) } ?: 0