package life.plank.juna.zone.view.activity.home

import android.app.Activity
import android.os.Bundle
import life.plank.juna.zone.*
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.notification.*
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.view.activity.base.BaseJunaCardActivity
import life.plank.juna.zone.view.fragment.home.HomeFragment
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
        ZoneApplication.getApplication().uiComponent.inject(this)

        pushFragment(HomeFragment.newInstance(), false)

        handleDeepLinkIntentIfAny()

        handleBoardIntentIfAny()

        handleSocialNotificationIntentIfAny()

        handleFootballLiveDataNotificationIntentIfAny()
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container

    override fun restApi(): RestApi? = restApi
}
