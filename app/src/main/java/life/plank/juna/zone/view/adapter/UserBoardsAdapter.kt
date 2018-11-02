package life.plank.juna.zone.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import kotlinx.android.synthetic.main.item_image_and_title.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.Board
import life.plank.juna.zone.data.network.interfaces.RestApi
import life.plank.juna.zone.util.DataUtil.findString
import life.plank.juna.zone.util.PreferenceManager.getSharedPrefs
import life.plank.juna.zone.util.UIDisplayUtil.findColor
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.launchPrivateBoard
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
                holder.itemView.title.text = boardList[position].name
                glide.load(boardList[position].boardIconUrl).into(holder.itemView.image!!)
                holder.itemView.image.borderColor = Color.parseColor(boardList[position].color)
                holder.itemView.image.onDebouncingClick { navigateToBoard(boardList[position].id, boardList[position].name!!) }
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
        val editor = getSharedPrefs(findString(R.string.pref_user_details))
        val username = editor.getString(findString(R.string.pref_display_name), findString(R.string.na))
        CreateBoardActivity.launch(activity, username!!)
    }

    private fun navigateToBoard(boardId: String, boardName: String) {
        if (boardName == findString(R.string.new_)) {
            launchBoardMaker()
        } else {
            restApi.launchPrivateBoard(boardId, activity)
        }
    }

    class UserBoardsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
