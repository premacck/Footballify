package life.plank.juna.zone.view.adapter.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.model.CoinPack;
import life.plank.juna.zone.util.BaseRecyclerView;
import life.plank.juna.zone.util.ObliqueStrikeTextView;

public class GetCoinsAdapter extends BaseRecyclerView.Adapter<GetCoinsAdapter.GetCoinsViewHolder> {

    private List<CoinPack> coinPackList;

    public GetCoinsAdapter() {
        this.coinPackList = new ArrayList<>();
    }

    @Override
    public GetCoinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GetCoinsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_coins, parent, false));
    }

    @Override
    public void onBindViewHolder(GetCoinsViewHolder holder, int position) {
        holder.bind(coinPackList.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * Use this method to update the recyclerView's contents instead of using and managing a list in the Activity (where it doesn't really belong).
     *
     * @param coinPackList the list to update (usually fetched from the server).
     */
    public void update(List<CoinPack> coinPackList) {
        this.coinPackList = coinPackList;
        notifyDataSetChanged();
    }

    static class GetCoinsViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.coin_amount)
        TextView coinAmount;
        @BindView(R.id.earlier_price)
        ObliqueStrikeTextView earlierPrice;
        @BindView(R.id.current_price)
        TextView currentPrice;

        GetCoinsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CoinPack coinPack) {
            if (coinPack.getCoinCount() > 0) coinAmount.setText(coinPack.getCoinCount());
            if (coinPack.getEarlierPrice() > 0)
                earlierPrice.setText(String.valueOf(coinPack.getEarlierPrice()));
            if (coinPack.getCurrentPrice() > 0)
                currentPrice.setText(String.valueOf(coinPack.getCurrentPrice()));
        }
    }
}