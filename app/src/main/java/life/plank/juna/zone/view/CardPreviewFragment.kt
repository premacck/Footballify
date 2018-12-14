package life.plank.juna.zone.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.fragment_profile_card_detail.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.facilis.BaseCard

class CardPreviewFragment : BaseCard() {
    companion object {
        fun newInstance() = CardPreviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.card_preview, container, false)

    override fun getBackgroundBlurLayout(): BlurLayout? = blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area

}