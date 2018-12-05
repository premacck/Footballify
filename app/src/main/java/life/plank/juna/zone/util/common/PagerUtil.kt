package life.plank.juna.zone.util.common

import android.support.v4.view.PagerAdapter
import life.plank.juna.zone.R
import life.plank.juna.zone.notification.getLiveFootballNotificationIntentActionFromActivity
import life.plank.juna.zone.notification.getSocialNotificationIntentActionFromActivity
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.view.fragment.base.CardTileFragment
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardFragment

fun CardTileFragment.getPositionFromIntentIfAny(pagerAdapter: PagerAdapter?): Int {
    return getSocialNotificationIntentActionFromActivity()?.run {
        when (this) {
            getString(R.string.intent_post), getString(R.string.intent_react) -> pagerAdapter.positionOf(getString(R.string.tiles))
            getString(R.string.intent_comment) -> pagerAdapter.positionOf(getString(R.string.forum))
            else -> pagerAdapter.positionOf(getString(R.string.tiles))
        }
    } ?: if (this is PrivateBoardFragment) {
        pagerAdapter.positionOf(getString(R.string.tiles))
    } else {
        when (getLiveFootballNotificationIntentActionFromActivity()) {
            MATCH_EVENTS, TIME_STATUS_DATA -> pagerAdapter.positionOf(getString(R.string.stats))
            LINEUPS_DATA -> pagerAdapter.positionOf(getString(R.string.lineups))
            else -> pagerAdapter.positionOf(getString(R.string.tiles))
        }
    }
}

fun PagerAdapter?.positionOf(tabName: String): Int = this?.run { getItemPosition(tabName) } ?: 0