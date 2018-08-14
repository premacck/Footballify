package life.plank.juna.zone.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.LastTransaction;
import life.plank.juna.zone.util.BaseRecyclerView;

public class LastTransactionsAdapter extends BaseRecyclerView.Adapter<LastTransactionsAdapter.LastTransactionsViewHolder> {

    private List<LastTransaction> lastTransactionsList;

    public LastTransactionsAdapter() {
        this.lastTransactionsList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? R.layout.item_user_transactions_header : R.layout.item_user_transactions_body;
    }

    @Override
    public LastTransactionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LastTransactionsViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(LastTransactionsViewHolder holder, int position) {
        if (position > 0) {
            holder.bind(lastTransactionsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return lastTransactionsList.size() + 1;
    }

    /**
     * Use this method to update the recyclerView's contents instead of using and managing a list in the Activity (where it doesn't really belong).
     *
     * @param lastTransactionsList the list to update (usually fetched from the server).
     */
    public void update(List<LastTransaction> lastTransactionsList) {
        this.lastTransactionsList = lastTransactionsList;
        notifyDataSetChanged();
    }

    static class LastTransactionsViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView dateView;
        @BindView(R.id.type)
        TextView typeView;
        @BindView(R.id.debit)
        TextView debitView;
        @BindView(R.id.credit)
        TextView creditView;
        @BindView(R.id.balance)
        TextView balanceView;

        LastTransactionsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(LastTransaction lastTransaction) {
            dateView.setText(lastTransaction.getDate());
            typeView.setText(lastTransaction.getType());
            debitView.setText(String.valueOf(lastTransaction.getDebit()));
            creditView.setText(String.valueOf(lastTransaction.getCredit()));
            balanceView.setText(String.valueOf(lastTransaction.getBalance()));
        }
    }
}