package life.plank.juna.zone.view.fragment.board.fixture.extra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_dart_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.util.facilis.floatUp
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.facilis.sinkDown
import life.plank.juna.zone.util.throwDart
import life.plank.juna.zone.view.fragment.base.BaseBlurPopup

class DartBoardPopup : BaseBlurPopup() {

    private var remainingDarts: Int = 3

    companion object {
        fun newInstance() = DartBoardPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.getApplication().uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.popup_dart_board, container, false)

    override fun doOnStart() {
        dartboard_layout.floatUp()
        dart_remaining_count_text_view.text = remainingDarts.toString()

        dart_layout_one.onDebouncingClick { throwDart(thrown_dart_1, thrown_dart_2, thrown_dart_3) }

        dart_layout_two.onDebouncingClick { throwDart(thrown_dart_4, thrown_dart_5, thrown_dart_6) }
    }

    private fun throwDart(dartOne: ImageView, dartTwo: ImageView, dartThree: ImageView) {
        if (remainingDarts > 0) {
            when (remainingDarts) {
                3 -> dartOne.throwDart(dart_remaining_count_text_view, remainingDarts)
                2 -> dartTwo.throwDart(dart_remaining_count_text_view, remainingDarts)
                1 -> dartThree.throwDart(dart_remaining_count_text_view, remainingDarts)
            }
            remainingDarts--
        }
    }

    override fun doOnDismiss() {
        dartboard_layout.sinkDown()
    }

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = dartboard_layout

    override fun getBackgroundLayout(): ViewGroup? = blur_layout
}