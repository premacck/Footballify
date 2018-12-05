package life.plank.juna.zone.view.fragment.profile

import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_profile_card_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.view.adapter.profile.ProfileCardAdapter

class ProfileCardDetailFragment : BaseCard() {

    private var profileCardAdapter: ProfileCardAdapter? = null

    companion object {
        fun newInstance() = ProfileCardDetailFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_profile_card_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCardRecyclerView()
    }

    private fun initCardRecyclerView() {
        profileCardAdapter = ProfileCardAdapter()
        cards_recycler_view.adapter = profileCardAdapter
    }

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootCard(): CardView? = root_card

    override fun getDragHandle(): View? = drag_area

}