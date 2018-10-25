package life.plank.juna.zone.view.activity.zone

import android.os.Bundle
import life.plank.juna.zone.R
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.zone.ZoneContainerFragment

class ZoneActivity : BaseCardActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone)
        pushFragment(ZoneContainerFragment.newInstance())
    }
}
