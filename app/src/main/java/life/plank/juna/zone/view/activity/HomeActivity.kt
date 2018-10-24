package life.plank.juna.zone.view.activity

import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.home.HomeFragment

class HomeActivity : BaseCardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pushFragment(HomeFragment.newInstance())
    }
}
