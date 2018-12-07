package life.plank.juna.zone.view.latestMatch

import android.app.Activity

import com.ahamed.multiviewadapter.DataItemManager
import com.ahamed.multiviewadapter.DataListManager
import com.ahamed.multiviewadapter.RecyclerAdapter

import life.plank.juna.zone.data.model.NextMatch
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.interfaces.OnItemClickListener

class MultiListAdapter(activity: Activity, restApi: RestApi, onItemClickListener: OnItemClickListener) : RecyclerAdapter() {

    private val topMatchDataManager: DataListManager<NextMatch>
    private val lowerMatchDataManager: DataListManager<NextMatch>
    private val leagueDataManager: DataItemManager<LeagueModel>

    init {
        topMatchDataManager = DataListManager(this)
        lowerMatchDataManager = DataListManager(this)
        leagueDataManager = DataItemManager(this)

        addDataManager(topMatchDataManager)
        addDataManager(leagueDataManager)
        addDataManager(lowerMatchDataManager)

        registerBinder(NextMatchBinder(activity, restApi))
        registerBinder(LeagueBinder(onItemClickListener, activity))
    }

    fun addTopMatch(dataList: List<NextMatch>) {
        topMatchDataManager.set(dataList)
    }

    fun addLowerMatch(dataList: List<NextMatch>) {
        lowerMatchDataManager.set(dataList)
    }

    fun addLeague(leagueModel: LeagueModel) {
        leagueDataManager.setItem(leagueModel)
    }
}
