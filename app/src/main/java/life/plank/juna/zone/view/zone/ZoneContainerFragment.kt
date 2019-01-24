package life.plank.juna.zone.view.zone

import android.os.Bundle
import android.view.*
import androidx.viewpager.widget.ViewPager
import com.prembros.facilis.fragment.*
import kotlinx.android.synthetic.main.fragment_zone_container.*
import life.plank.juna.zone.R

class ZoneContainerFragment : BaseCardListContainerFragment() {

    companion object {
        fun newInstance() = ZoneContainerFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_zone_container, container, false)

    override fun backgroundBlurLayout(): ViewGroup? = null

    override fun baseCardCount(): Int = 1

    override fun viewPager(): ViewPager = zone_view_pager

    override fun baseCardToInflate(position: Int): BaseCardFragment = ZoneFragment.newInstance()
}
