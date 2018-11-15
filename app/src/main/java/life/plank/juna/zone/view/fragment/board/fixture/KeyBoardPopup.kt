package life.plank.juna.zone.view.fragment.board.fixture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.item_keyboard.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.facilis.sinkDown
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup

class KeyBoardPopup : BaseBlurPopup() {

    companion object {
        val TAG: String = KeyBoardPopup::class.java.simpleName
        fun newInstance() = KeyBoardPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root_card.setOnClickListener(null)
    }

    override fun doOnStart() {
        root_card.floatUp()
    }

    override fun dismiss() {
        root_card.sinkDown()
        super.dismiss()
    }

    override fun getBlurLayout(): BlurLayout? = root_blur_layout


    override fun getDragHandle(): View? = null


    override fun getRootView(): View? = root_card


    override fun getBackgroundLayout(): ViewGroup? = root_blur_layout
}

