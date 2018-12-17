package life.plank.juna.zone.view.adapter.board.user

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_image_and_title.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.common.AppConstants.LIVE
import life.plank.juna.zone.util.common.AppConstants.MatchTimeVal.*
import life.plank.juna.zone.util.common.AppConstants.SOON
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.common.launchPrivateOrMatchBoard
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.util.time.DateUtil.getMatchTimeValue
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor
import life.plank.juna.zone.util.view.UIDisplayUtil.findDrawable
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.board.CreateBoardActivity
import java.util.*

class UserBoardsAdapter(
        private val activity: BaseCardActivity,
        private val restApi: RestApi,
        private val glide: RequestManager,
        private val isTitleShown: Boolean
) : RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder>() {

    private var boardList: MutableList<Board> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBoardsViewHolder =
            UserBoardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_and_title, parent, false))

    override fun onBindViewHolder(holder: UserBoardsViewHolder, position: Int) {
        try {
            holder.itemView.image.isBorderOverlay = true
            holder.itemView.title.visibility = if (isTitleShown) View.VISIBLE else View.GONE
            val board = boardList[position]
            if (board.name == findString(R.string.new_)) {
                holder.itemView.title.text = board.name
                holder.itemView.image.setImageDrawable(ZoneApplication.getContext().getDrawable(R.drawable.new_board_circle))
                holder.itemView.image.borderColor = findColor(R.color.white)
                holder.itemView.onDebouncingClick { navigateToBoard(board, board.name!!) }
            } else {
                when (board.boardType) {
                    findString(R.string.board_type_football_match) -> {
                        holder.itemView.home_team_logo.visibility = View.VISIBLE
                        holder.itemView.visiting_team_logo.visibility = View.VISIBLE
                        holder.itemView.title.text = board.name

                        glide.load(R.drawable.circle_background_white).into(holder.itemView.image)

                        glide.load(board.boardEvent?.homeTeamLogo)
                                .into(holder.itemView.home_team_logo)
                        glide.load(board.boardEvent?.awayTeamLogo)
                                .into(holder.itemView.visiting_team_logo)
                        holder.itemView.onDebouncingClick {
                            if (!isNullOrEmpty(board.boardEvent?.leagueName)) {
                                navigateToBoard(board, board.boardType)
                            }
                        }
                        when (getMatchTimeValue(board.boardEvent?.matchStartTime, true)) {
                            MATCH_LIVE -> {
                                holder.itemView.badge.run {
                                    visibility = View.VISIBLE
                                    background = findDrawable(R.drawable.bg_board_badge_red)
                                    text = LIVE
                                }
                                holder.itemView.image.borderColor = findColor(R.color.badge_red)
                            }
                            MATCH_SCHEDULED_TODAY -> {
                                holder.itemView.badge.run {
                                    visibility = View.VISIBLE
                                    background = findDrawable(R.drawable.bg_board_badge_blue)
                                    text = SOON
                                }
                                holder.itemView.image.borderColor = findColor(R.color.twilight_blue)
                            }
                            MATCH_PAST, MATCH_COMPLETED_TODAY, MATCH_SCHEDULED_LATER,
                            MATCH_ABOUT_TO_START_BOARD_ACTIVE, MATCH_ABOUT_TO_START -> {
                                holder.itemView.badge.visibility = View.GONE
                                holder.itemView.image.borderColor = findColor(R.color.black_76_opaque)
                            }
                        }
                    }
                    findString(R.string.private_) -> {
                        holder.itemView.badge.visibility = View.GONE
                        holder.itemView.home_team_logo.visibility = View.GONE
                        holder.itemView.visiting_team_logo.visibility = View.GONE
                        holder.itemView.title.text = board.name
                        glide.load(board.boardIconUrl)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                                .into(holder.itemView.image)
                        holder.itemView.image.borderColor = Color.parseColor(board.color)
                        holder.itemView.onDebouncingClick { navigateToBoard(board, board.name!!) }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("onBindViewHolder() ", "ERROR: ", e)
        }
    }

    override fun getItemCount(): Int = boardList.size

    fun setUserBoards(boards: MutableList<Board>) {
        boardList = boards
        notifyDataSetChanged()
    }

    private fun launchBoardMaker() {
        PreferenceManager.CurrentUser.getDisplayName()?.run { CreateBoardActivity.launch(activity, this) }
    }

    private fun navigateToBoard(board: Board, boardName: String) {
        if (boardName == findString(R.string.new_)) {
            launchBoardMaker()
        } else {
            activity.launchPrivateOrMatchBoard(restApi, board)
        }
    }

    class UserBoardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
