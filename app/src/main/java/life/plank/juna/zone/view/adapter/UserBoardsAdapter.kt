package life.plank.juna.zone.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_image_and_title.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.DataUtil.isNullOrEmpty
import life.plank.juna.zone.util.PreferenceManager
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.launchMatchBoard
import life.plank.juna.zone.util.launchPrivateBoard
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.activity.board.CreateBoardActivity
import java.util.*

class UserBoardsAdapter(
        private val activity: BaseCardActivity,
        private val restApi: RestApi,
        private val footballRestApi: RestApi,
        private val glide: RequestManager,
        private val isTitleShown: Boolean
) : RecyclerView.Adapter<UserBoardsAdapter.UserBoardsViewHolder>() {

    private var boardList: MutableList<Board> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBoardsAdapter.UserBoardsViewHolder {
        return UserBoardsAdapter.UserBoardsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_and_title, parent, false))
    }

    override fun onBindViewHolder(holder: UserBoardsAdapter.UserBoardsViewHolder, position: Int) {
        try {
            holder.itemView.title.visibility = if (isTitleShown) View.VISIBLE else View.GONE
            if (boardList[position].name == findString(R.string.new_)) {
                holder.itemView.title.text = boardList[position].name
                holder.itemView.image.setImageDrawable(ZoneApplication.getContext().getDrawable(R.drawable.new_board_circle))
                holder.itemView.image.borderColor = findColor(R.color.white)
                holder.itemView.image.onDebouncingClick { navigateToBoard(boardList[position].id, boardList[position].name!!) }
            } else {
                when (boardList[position].boardType) {
                    findString(R.string.board_type_football_match) -> {
                        holder.itemView.home_team_logo.visibility = View.VISIBLE
                        holder.itemView.visiting_team_logo.visibility = View.VISIBLE
                        holder.itemView.title.text = boardList[position].name

                        glide.load(R.drawable.circle_background_white)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                                .into(holder.itemView.image)
                        holder.itemView.image.borderColor = findColor(R.color.grey_10)
                        glide.load(boardList[position].boardEvent!!.homeTeamLogo)
                                .into(holder.itemView.home_team_logo)
                        glide.load(boardList[position].boardEvent!!.awayTeamLogo)
                                .into(holder.itemView.visiting_team_logo)
                        boardList[position].id
                        boardList[position].name
                        holder.itemView.image.onDebouncingClick {
                            if (!isNullOrEmpty(boardList[position].boardEvent?.leagueName)) {
                                navigateToBoard(boardList[position].id, boardList[position].boardType,
                                        boardList[position].boardEvent!!.foreignId.toLong(), boardList[position].boardEvent?.leagueName!!)
                            }
                        }
                    }
                    findString(R.string.private_) -> {
                        holder.itemView.home_team_logo.visibility = View.GONE
                        holder.itemView.visiting_team_logo.visibility = View.GONE
                        holder.itemView.title.text = boardList[position].name
                        glide.load(boardList[position].boardIconUrl)
                                .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                                .into(holder.itemView.image!!)
                        holder.itemView.image.borderColor = Color.parseColor(boardList[position].color)
                        holder.itemView.image.onDebouncingClick { navigateToBoard(boardList[position].id, boardList[position].name!!) }
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

    private fun navigateToBoard(boardId: String, boardName: String, matchId: Long = 0, leagueName: String = "") {
        when (boardName) {
            findString(R.string.new_) -> launchBoardMaker()
            findString(R.string.board_type_football_match) -> footballRestApi.launchMatchBoard(matchId, activity, leagueName)
            else -> restApi.launchPrivateBoard(boardId, activity)
        }
    }

    class UserBoardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
