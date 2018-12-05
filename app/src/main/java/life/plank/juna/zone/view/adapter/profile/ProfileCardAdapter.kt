package life.plank.juna.zone.view.adapter.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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