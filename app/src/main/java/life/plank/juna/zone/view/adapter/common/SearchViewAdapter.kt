package life.plank.juna.zone.view.adapter.common

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_image_and_title.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.User
import life.plank.juna.zone.util.facilis.onDebouncingClick
import life.plank.juna.zone.util.view.UIDisplayUtil.findColor

class SearchViewAdapter(private val userList: MutableList<User>, private val glide: RequestManager) : RecyclerView.Adapter<SearchViewAdapter.SearchViewHolder>() {

    val selectedUsers: MutableSet<User> = HashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
            SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_and_title, parent, false))

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.itemView.run {
            val user = userList[position]
            title.text = user.displayName
            glide.load(user.profilePictureUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_default_profile)
                            .error(R.drawable.ic_default_profile))
                    .into(image)

            setItemSelection(user, holder, false)

            image.onDebouncingClick { setItemSelection(user, holder, true) }
        }
    }

    private fun setItemSelection(user: User, holder: SearchViewHolder, isActionDone: Boolean) {
        holder.itemView.run {
            if (isActionDone) {
                if (selectedUsers.contains(user)) {
                    selectedUsers.remove(user)
                } else {
                    selectedUsers.add(user)
                }
            }
            val isItemSelected = selectedUsers.contains(user)
            follow_image_view.visibility = if (isItemSelected) View.VISIBLE else View.INVISIBLE
            image.imageAlpha = if (isItemSelected) 160 else 255
            title.visibility = if (isItemSelected) View.INVISIBLE else View.VISIBLE
            title.setTextColor(if (isItemSelected) Color.BLACK else findColor(R.color.grey))
            image.colorFilter = (if (isItemSelected) PorterDuffColorFilter(findColor(R.color.red_pink), PorterDuff.Mode.LIGHTEN) else null)
        }
    }

    override fun getItemCount(): Int = userList.size

    fun update(users: List<User>) {
        userList.clear()
        this.userList.addAll(users)
        notifyDataSetChanged()
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
