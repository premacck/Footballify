package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchListModel;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MatchListAdapterViewHolder> {

    private Context context;
    private List<MatchListModel> matchListModel;

    public MatchListAdapter(Context context, List<MatchListModel> matchListModel) {
        this.context = context;
        this.matchListModel = matchListModel;
    }

    @Override
    public MatchListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_row, parent, false);
        return new MatchListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchListAdapterViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return matchListModel.size();
    }

    class MatchListAdapterViewHolder extends RecyclerView.ViewHolder {
        MatchListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
