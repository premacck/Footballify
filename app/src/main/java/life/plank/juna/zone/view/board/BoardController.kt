package life.plank.juna.zone.view.board

import androidx.annotation.StringRes
import com.airbnb.epoxy.AutoModel
import com.prembros.facilis.util.isNullOrEmpty
import life.plank.juna.zone.R
import life.plank.juna.zone.component.epoxymodelview.*
import life.plank.juna.zone.component.helper.launchPrivateOrMatchBoard
import life.plank.juna.zone.data.api.RestApi
import life.plank.juna.zone.data.model.board.Board
import life.plank.juna.zone.service.CommonDataService
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.view.base.BaseJunaCardActivity
import life.plank.juna.zone.view.base.component.EpoxyController3

class BoardController(
        private val activity: BaseJunaCardActivity,
        private val restApi: RestApi,
        private val isTitleShown: Boolean
) : EpoxyController3<List<Board>, Boolean, Int>() {

    @AutoModel
    lateinit var textView: TextModelViewModel_

    override fun buildModels(boardList: List<Board>?, hasError: Boolean, @StringRes errorMessage: Int?) {
        if (hasError) {
//            Add error layout
            errorMessage?.run { textView.withText(this).addTo(this@BoardController) }
        } else {
            if (isNullOrEmpty(boardList)) {
//                Add empty layout
                errorMessage?.run { textView.withText(this).addTo(this@BoardController) }
            } else {
                boardList?.forEach {
                    BoardViewModel_()
                            .id(boardList.indexOf(it))
                            .prepare(it)
                            .name(if (isTitleShown) it.displayName else null)
                            .onClick { navigateToBoard(it) }
                            .addTo(this)
                }
            }
        }
    }

    private fun navigateToBoard(board: Board) {
        if (board.displayName != null && board.displayName == CommonDataService.findString(R.string.new_)) {
            CurrentUser.displayName?.run { CreateBoardActivity.launch(activity, this) }
        } else {
            activity.launchPrivateOrMatchBoard(restApi, board)
        }
    }
}