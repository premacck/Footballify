package life.plank.juna.zone.ui.user.profile

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import life.plank.juna.zone.R

class ProfileCardAdapter : RecyclerView.Adapter<ProfileCardAdapter.ProfileCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCardViewHolder =
            ProfileCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_profile_card, parent, false))

    override fun onBindViewHolder(holder: ProfileCardViewHolder, position: Int) {
    }

    //TODO: Remove hardcoded data ,when backend returns data
    override fun getItemCount(): Int = 15


    class ProfileCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}