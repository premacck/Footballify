package life.plank.juna.zone.view.user.adapter

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_user_transactions_body.view.*
import life.plank.juna.zone.R
import life.plank.juna.zone.data.model.user.LastTransaction
import java.util.*

class LastTransactionsAdapter : RecyclerView.Adapter<LastTransactionsAdapter.LastTransactionsViewHolder>() {

    private var lastTransactionsList: List<LastTransaction> = ArrayList()

    override fun getItemViewType(position: Int): Int = if (position == 0) R.layout.item_user_transactions_header else R.layout.item_user_transactions_body

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastTransactionsViewHolder = LastTransactionsViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))

    override fun onBindViewHolder(holder: LastTransactionsViewHolder, position: Int) {
        if (position > 0) {
            val lastTransaction = lastTransactionsList[position]
            holder.itemView.run {
                date.text = lastTransaction.date
                type.text = lastTransaction.type
                debit.text = lastTransaction.debit.toString()
                credit.text = lastTransaction.credit.toString()
                balance.text = lastTransaction.balance.toString()
            }
        }
    }

    override fun getItemCount(): Int = lastTransactionsList.size + 1

    /**
     * Use this method to update the recyclerView's contents instead of using and managing a list in the Activity (where it doesn't really belong).
     *
     * @param lastTransactionsList the list to update (usually fetched from the server).
     */
    fun update(lastTransactionsList: List<LastTransaction>) {
        this.lastTransactionsList = lastTransactionsList
        notifyDataSetChanged()
    }

    class LastTransactionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}