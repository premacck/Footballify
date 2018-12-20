package life.plank.juna.zone.view.latestMatch

import android.app.Activity

import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.DataListManager
import com.ahamed.multiviewadapter.RecyclerAdapter

import life.plank.juna.zone.data.model.NextMatch
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.facilis.addDataManagerAndRegisterBinder

class FootballZoneAdapter(activity: Activity, restApi: RestApi, onItemClickListener: OnItemClickListener) : RecyclerAdapter() {

    private val topMatchDataManager: DataListManager<NextMatch> = DataListManager(this)
    private val lowerMatchDataManager: DataListManager<NextMatch> = DataListManager(this)
    private val leagueDataManager: DataItemManager<LeagueModel> = DataItemManager(this)

    init {
        addDataManagerAndRegisterBinder(topMatchDataManager, NextMatchBinder(activity, restApi))
        addDataManagerAndRegisterBinder(leagueDataManager, LeagueBinder(onItemClickListener, activity))
        addDataManager(lowerMatchDataManager)
    }

    fun setMatches(nextMatches: List<NextMatch>) {
        if (!isNullOrEmpty(nextMatches)) {
            nextMatches.run {
                topMatchDataManager.set(subList(0, if (size <= 3) size else 3))
                if (size > 3) {
                    lowerMatchDataManager.set(subList(3, size))
                }
            }
        }
        leagueDataManager.setItem(LeagueModel())
    }
}
