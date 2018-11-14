package life.plank.juna.zone.view.activity.zone

import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.handleMatchBoardIntentIfAny
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.zone.ZoneContainerFragment
import javax.inject.Inject
import javax.inject.Named

class ZoneActivity : BaseCardActivity() {

    @field: [Inject Named("footballData")]
    lateinit var footballRestApi: RestApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone)
        ZoneApplication.getApplication().uiComponent.inject(this)

        pushFragment(ZoneContainerFragment.newInstance())

        handleMatchBoardIntentIfAny(footballRestApi)
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container
}
