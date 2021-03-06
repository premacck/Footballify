package life.plank.juna.zone.ui.board.fragment.fixture.extra

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import com.prembros.facilis.dialog.BaseBlurPopup
import com.prembros.facilis.util.onDebouncingClick
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.popup_dart_board.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.api.setObserverThreadsAndSmartSubscribe
import life.plank.juna.zone.data.model.user.User
import javax.inject.Inject

class DartBoardPopup : BaseBlurPopup() {

    @Inject
    lateinit var restApi: RestApi

    private var remainingDarts: Int = 3

    companion object {
        fun newInstance() = DartBoardPopup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoneApplication.application.uiComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.popup_dart_board, container, false)

    override fun doOnStart() {
        dart_remaining_count_text_view.text = remainingDarts.toString()

        //TODO: Replace with playerId
        dart_layout_one.tag = "PlayerOne"
        dart_layout_two.tag = "PlayerTwo"

        dart_count_one.text = getString(R.string.n_darts, 45)
        dart_count_two.text = getString(R.string.n_darts, 84)

        dart_layout_one.onDebouncingClick {
            postDart(dart_layout_one.tag.toString())
            throwDart(thrown_dart_1, thrown_dart_2, thrown_dart_3)
        }

        dart_layout_two.onDebouncingClick {
            postDart(dart_layout_two.tag.toString())
            throwDart(thrown_dart_4, thrown_dart_5, thrown_dart_6)
        }
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

    override fun getBlurLayout(): BlurLayout? = blur_layout

    override fun getDragHandle(): View? = drag_area

    override fun getRootView(): View? = dartboard_layout

    override fun getBackgroundLayout(): ViewGroup? = blur_layout

    private fun postDart(playerId: String) {
        //TODO: Replace with boardId
        restApi.postDart(playerId, User(id = playerId))
                .setObserverThreadsAndSmartSubscribe({
                    Log.e("postClap()", "ERROR: ", it)
                }, {
                    //TODO: Handle response
                    Log.d("postClap()", "Response: " + it.code())
                }, this)
    }
}