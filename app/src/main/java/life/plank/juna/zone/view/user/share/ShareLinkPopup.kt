package life.plank.juna.zone.view.user.share

import android.os.Bundle
import android.view.*
import com.prembros.facilis.dialog.BaseBlurPopup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_share_link.*
import life.plank.juna.zone.*


class ShareLinkPopup : BaseBlurPopup() {
    
    companion object {
        val TAG: String = ShareLinkPopup::class.java.simpleName
        fun newInstance() = ShareLinkPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_share_link, container, false)

    override fun getBlurLayout(): BlurLayout? = root_blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = root_card

    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout
}
