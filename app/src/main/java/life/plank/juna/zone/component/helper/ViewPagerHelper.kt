package life.plank.juna.zone.component.helper

import androidx.viewpager.widget.PagerAdapter
import life.plank.juna.zone.R
import life.plank.juna.zone.notification.*
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.util.common.AppConstants.*
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.base.fragment.CardTileFragment
import life.plank.juna.zone.ui.board.fragment.user.PrivateBoardFragment

// todo: doc: describe purpose here //
fun CardTileFragment.getPositionFromIntentIfAny(pagerAdapter: PagerAdapter?): Int {
    return activity?.getActionFromIntent()?.run {
        (activity as? BaseJunaCardActivity)?.intent?.removeExtra(findString(R.string.intent_action))
        when (this) {
            getString(R.string.intent_post), getString(R.string.intent_react) -> pagerAdapter.positionOf(getString(R.string.tiles))
            getString(R.string.intent_comment) -> pagerAdapter.positionOf(getString(R.string.forum))
            else -> pagerAdapter.positionOf(getString(R.string.tiles))
        }
    } ?: if (this is PrivateBoardFragment) {
        pagerAdapter.positionOf(getString(R.string.tiles))
    } else {
        activity?.getLiveDataTypeFromIntent()?.run {
            (activity as? BaseJunaCardActivity)?.intent?.removeExtra(findString(R.string.intent_live_data_type))
            when (this) {
                MATCH_EVENTS, TIME_STATUS_DATA -> pagerAdapter.positionOf(getString(R.string.stats))
                LINEUPS_DATA -> pagerAdapter.positionOf(getString(R.string.lineups))
                else -> pagerAdapter.positionOf(getString(R.string.tiles))
            }
        } ?: pagerAdapter.positionOf(getString(R.string.tiles))
    }
}

fun PagerAdapter?.positionOf(tabName: String): Int = this?.run { getItemPosition(tabName) } ?: 0