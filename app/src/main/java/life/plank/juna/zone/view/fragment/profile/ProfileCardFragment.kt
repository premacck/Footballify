package life.plank.juna.zone.view.fragment.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.user_profile_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard
import life.plank.juna.zone.util.facilis.onDebouncingClick

class ProfileCardFragment : BaseCard() {

    companion object {
        val TAG: String = ProfileCardFragment::class.java.simpleName
        fun newInstance(): ProfileCardFragment = ProfileCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.user_profile_card, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect_button.onDebouncingClick {
            pushFragment(ProfileCardDetailFragment.newInstance(), true)
        }
    }

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

}
