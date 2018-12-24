package life.plank.juna.zone.view.fragment.zone

import android.os.Bundle
import android.view.*
import androidx.viewpager.widget.ViewPager
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

    override fun backgroundBlurLayout(): ViewGroup? = null

    override fun baseCardCount(): Int = 1

    override fun viewPager(): ViewPager = zone_view_pager

    override fun baseCardToInflate(position: Int): BaseFragment = ZoneFragment.newInstance()
}
