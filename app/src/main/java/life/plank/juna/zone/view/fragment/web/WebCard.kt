package life.plank.juna.zone.view.fragment.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.fragment_web_card.*
import life.plank.juna.zone.R
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.facilis.BaseCard

class WebCard : BaseCard() {

    companion object {
        fun newInstance(url: String) = WebCard().apply { arguments = Bundle().apply { putString(findString(R.string.backend_base_url), url) } }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_web_card, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = arguments?.getString(getString(R.string.backend_base_url))
        web_view.webChromeClient = WebChromeClient()
        web_view.settings.javaScriptEnabled = true
        web_view.loadUrl(url)
    }

    override fun getBackgroundBlurLayout(): ViewGroup? = root_blur_layout

    override fun getRootView(): ViewGroup? = root_card

    override fun getDragView(): View? = drag_area
}
