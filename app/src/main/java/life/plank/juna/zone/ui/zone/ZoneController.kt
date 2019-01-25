package life.plank.juna.zone.ui.zone

import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.component.epoxymodelview.ZoneViewModel_
import life.plank.juna.zone.component.helper.launch
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.user.UserPreference
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.base.component.EpoxyController
import life.plank.juna.zone.ui.onboarding.TeamSelectionFragment

class ZoneController(private val activity: BaseJunaCardActivity, private val restApi: RestApi) : EpoxyController<List<UserPreference>>() {

    override fun buildModels(userPreferenceList: List<UserPreference>?) {
        userPreferenceList?.forEach {
            ZoneViewModel_()
                    .id(userPreferenceList.indexOf(it))
                    .withZone(Pair(it, restApi))
                    .onClick {
                        if (isNullOrEmpty(it.zonePreferences)) {
                            (activity as? BaseJunaCardActivity)?.pushFragment(TeamSelectionFragment.newInstance(), true)
                        } else {
                            activity.launch<ZoneActivity>()
                        }
                    }.addTo(this)
        }
    }
}