package life.plank.juna.zone.view.user.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_get_coins.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.user.CoinPack
import java.util.*

class GetCoinsAdapter : RecyclerView.Adapter<GetCoinsAdapter.GetCoinsViewHolder>() {

    private var coinPackList: List<CoinPack> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetCoinsViewHolder =
            GetCoinsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_get_coins, parent, false))

    override fun onBindViewHolder(holder: GetCoinsViewHolder, position: Int) {
        holder.itemView.run {
            val coinPack = coinPackList[position]
            if (coinPack.coinCount > 0) coin_amount.setText(coinPack.coinCount)
            if (coinPack.earlierPrice > 0)
                earlier_price.text = coinPack.earlierPrice.toString()
            if (coinPack.currentPrice > 0)
                current_price.text = coinPack.currentPrice.toString()
        }
    }

    override fun getItemCount(): Int = coinPackList.size

    fun update(coinPackList: List<CoinPack>) {
        this.coinPackList = coinPackList
        notifyDataSetChanged()
    }

    class GetCoinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}