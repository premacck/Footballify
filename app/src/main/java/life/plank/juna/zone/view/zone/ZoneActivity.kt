package life.plank.juna.zone.view.zone

import android.os.Bundle
import life.plank.juna.zone.*
import life.plank.juna.zone.component.helper.handleBoardIntentIfAny
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import javax.inject.Inject

class ZoneActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone)
        ZoneApplication.getApplication().uiComponent.inject(this)

        pushFragment(ZoneContainerFragment.newInstance(), false)

        handleBoardIntentIfAny()
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container

    override fun restApi(): RestApi? = restApi
}
