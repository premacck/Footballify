package life.plank.juna.zone.view.activity.home

import android.app.Activity
import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.home.HomeFragment
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class HomeActivity : BaseCardActivity() {

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

        pushFragment(HomeFragment.newInstance())
    }

    override fun getFragmentContainer(): Int = R.id.main_fragment_container
}
