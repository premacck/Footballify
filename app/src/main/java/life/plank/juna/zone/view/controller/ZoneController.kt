package life.plank.juna.zone.view.controller

import life.plank.juna.zone.data.model.UserPreference
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.launch
import life.plank.juna.zone.util.epoxy.EpoxyController
import life.plank.juna.zone.util.epoxy.modelview.ZoneViewModel_
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.zone.ZoneActivity
import life.plank.juna.zone.view.fragment.onboarding.TeamSelectionFragment

class ZoneController(private val activity: BaseCardActivity, private val restApi: RestApi) : EpoxyController<List<UserPreference>>() {

    override fun buildModels(userPreferenceList: List<UserPreference>?) {
        userPreferenceList?.forEach {
            ZoneViewModel_()
                    .id(userPreferenceList.indexOf(it))
                    .withZone(Pair(it, restApi))
                    .onClick {
                        if (isNullOrEmpty(it.zonePreferences)) {
                            (activity as? BaseCardActivity)?.pushFragment(TeamSelectionFragment.newInstance(), true)
                        } else {
                            activity.launch<ZoneActivity>()
                        }
                    }.addTo(this)
        }
    }
}