package life.plank.juna.zone.ui.home

import android.app.Activity
import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.component.helper.handleBoardIntentIfAny
import life.plank.juna.zone.component.helper.handleDeepLinkIntentIfAny
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.notification.handleFootballLiveDataNotificationIntentIfAny
import life.plank.juna.zone.notification.handleSocialNotificationIntentIfAny
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import org.jetbrains.anko.*
import javax.inject.Inject

class HomeActivity : BaseJunaCardActivity() {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        fun launch(from: Activity, isClearTop: Boolean = true) {
            if (isClearTop) {
                from.startActivity(from.intentFor<HomeActivity>().clearTop())
            } else {
                from.startActivity<HomeActivity>()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        ZoneApplication.application.uiComponent.inject(this)

        pushFragment(HomeFragment.newInstance(), false)

        handleDeepLinkIntentIfAny()

        handleBoardIntentIfAny()

        handleSocialNotificationIntentIfAny()

        handleFootballLiveDataNotificationIntentIfAny()
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container

    override fun restApi(): RestApi? = restApi
}
