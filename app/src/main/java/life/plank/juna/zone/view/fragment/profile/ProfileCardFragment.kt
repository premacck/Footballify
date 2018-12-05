package life.plank.juna.zone.view.fragment.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.user_profile_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard

class ProfileCardFragment : BaseCard() {

    companion object {
        val TAG: String = ProfileCardFragment::class.java.simpleName
        fun newInstance(): ProfileCardFragment = ProfileCardFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.user_profile_card, container, false)

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): RelativeLayout? = root_card

    override fun getDragView(): View? = drag_area

}
