package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_zone_container.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard

class ZoneContainerFragment : BaseCard() {

    companion object {
        fun newInstance() = ZoneContainerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_zone_container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        zone_view_pager.adapter = ZonePagerAdapter(childFragmentManager)
    }

    class ZonePagerAdapter(fm: FragmentManager, private val zoneCount: Int = 1) : FragmentStatePagerAdapter(fm) {

        override fun getItem(index: Int): Fragment? {
            return ZoneFragment.newInstance()
        }

        override fun getCount(): Int {
            return zoneCount
        }
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = null

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area
}
