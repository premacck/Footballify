package life.plank.juna.zone.view.controller

import androidx.annotation.StringRes
import com.airbnb.epoxy.AutoModel
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.*
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.epoxy.EpoxyController3
import life.plank.juna.zone.util.epoxy.modelview.*
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.board.CreateBoardActivity

class BoardController(
        private val activity: BaseCardActivity,
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
        if (board.displayName != null && board.displayName == DataUtil.findString(R.string.new_)) {
            PreferenceManager.CurrentUser.getDisplayName()?.run { CreateBoardActivity.launch(activity, this) }
        } else {
            activity.launchPrivateOrMatchBoard(restApi, board)
        }
    }
}