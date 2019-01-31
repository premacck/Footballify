package life.plank.juna.zone.ui.board.adapter.user

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.ZoneApplication
import life.plank.juna.zone.component.customview.CustomPopup
import life.plank.juna.zone.data.model.user.User
import life.plank.juna.zone.service.CommonDataService.findString
import life.plank.juna.zone.sharedpreference.CurrentUser
import life.plank.juna.zone.ui.base.BaseJunaCardActivity
import life.plank.juna.zone.ui.base.fragment.BaseJunaFragment
import life.plank.juna.zone.ui.board.fragment.user.PrivateBoardInfoFragment
import life.plank.juna.zone.ui.onboarding.SearchUserPopup
import org.jetbrains.anko.sdk27.coroutines.onClick

class BoardMembersViewAdapter(
        private var userList: MutableList<User>,
        private val boardId: String,
        private val fragment: BaseJunaFragment,
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
            holder.itemView.image.background = ZoneApplication.appContext.getDrawable(R.drawable.new_board_circle)
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
        (fragment.activity as? BaseJunaCardActivity)?.pushPopup(SearchUserPopup.newInstance(boardId, boardName))
    }

    private fun showOptionsPopup(holder: BoardMembersViewHolder, position: Int) {
        val userDisplayName = CurrentUser.displayName
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
