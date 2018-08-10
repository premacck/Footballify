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
import life.plank.juna.zone.data.network.model.GetCoins;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.ObliqueStrikeTextView;

public class GetCoinsAdapter extends BaseRecyclerView.Adapter<GetCoinsAdapter.GetCoinsViewHolder> {

    private List<GetCoins> getCoinsList;

    public GetCoinsAdapter() {
        this.getCoinsList = new ArrayList<>();
    }

    @Override public GetCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GetCoinsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_coins, parent, false));
    }

    @Override public void onBindViewHolder(GetCoinsViewHolder holder, int position) {
        holder.bind(getCoinsList.get(position));
    }

    @Override public int getItemCount() {
        return 0;
    }

    /**
     * Use this method to update the recyclerView's contents instead of using and managing a list in the Activity (where it doesn't really belong).
     * @param getCoinsList the list to update (usually fetched from the server).
     */
    public void update(List<GetCoins> getCoinsList) {
        this.getCoinsList = getCoinsList;
        notifyDataSetChanged();
    }

    static class GetCoinsViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.coin_amount) TextView coinAmount;
        @BindView(R.id.earlier_price) ObliqueStrikeTextView earlierPrice;
        @BindView(R.id.current_price) TextView currentPrice;

        GetCoinsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GetCoins getCoins) {
            if (getCoins.getCoinCount() > 0) coinAmount.setText(getCoins.getCoinCount());
            if (getCoins.getEarlierPrice() > 0) earlierPrice.setText(String.valueOf(getCoins.getEarlierPrice()));
            if (getCoins.getCurrentPrice() > 0) currentPrice.setText(String.valueOf(getCoins.getCurrentPrice()));
        }
    }
}