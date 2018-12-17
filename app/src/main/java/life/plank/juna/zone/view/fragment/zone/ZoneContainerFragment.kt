package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_zone_container.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCardContainerFragment
import life.plank.juna.zone.view.fragment.base.BaseFragment

class ZoneContainerFragment : BaseCardContainerFragment() {

    companion object {
        fun newInstance() = ZoneContainerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_zone_container, container, false)

    override fun getBackgroundBlurLayout(): ViewGroup? = null

    override fun getDragView(): View? = drag_area

    override fun baseCardCount(): Int = 1

    override fun viewPager(): androidx.viewpager.widget.ViewPager = zone_view_pager

    override fun baseCardToInflate(position: Int): BaseFragment = ZoneFragment.newInstance()
}
