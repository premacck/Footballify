package life.plank.juna.zone.view.adapter.board.user

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.util.common.DataUtil.findString
import life.plank.juna.zone.util.customview.CustomPopup
import life.plank.juna.zone.util.sharedpreference.PreferenceManager
import life.plank.juna.zone.view.activity.base.BaseCardActivity
import life.plank.juna.zone.view.fragment.base.BaseFragment
import life.plank.juna.zone.view.fragment.board.user.PrivateBoardInfoFragment
import life.plank.juna.zone.view.fragment.onboarding.SearchUserPopup
import org.jetbrains.anko.sdk27.coroutines.onClick

class BoardMembersViewAdapter(
        private var userList: MutableList<User>,
        private val boardId: String,
        private val fragment: BaseFragment,
        private val displayName: String,
        private val boardName: String
) : RecyclerView.Adapter<BoardMembersViewAdapter.BoardMembersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardMembersViewHolder =
            BoardMembersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun onBindViewHolder(holder: BoardMembersViewHolder, position: Int) {
        holder.itemView.title.text = userList[position].displayName

        Glide.with(fragment)
                .load(userList[position].profilePictureUrl)
                .into(holder.itemView.image)

        if (userList[position].displayName == findString(R.string.invite_string)) {
            holder.itemView.image.background = ZoneApplication.getContext().getDrawable(R.drawable.new_board_circle)
        }

        holder.itemView.image.onClick {
            if (userList[position].displayName == findString(R.string.invite_string)) {
                showInviteToBoardPopup()
            } else if (userList[position].displayName != displayName) {
                showOptionsPopup(holder, position)
            }
        }
    }

    private fun showInviteToBoardPopup() {
        (fragment.activity as? BaseCardActivity)?.pushPopup(SearchUserPopup.newInstance(boardId, boardName))
    }

    private fun showOptionsPopup(holder: BoardMembersViewHolder, position: Int) {
        val userDisplayName = PreferenceManager.CurrentUser.getDisplayName()
        if (userDisplayName == displayName) {
            if (fragment is PrivateBoardInfoFragment) {
                fragment.userPosition = position
                CustomPopup.showPrivateBoardOptionPopup(holder.itemView.image, fragment, userList[position].objectId!!)
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    fun update(users: MutableList<User>) {
        userList = users
        notifyDataSetChanged()
    }

    class BoardMembersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
