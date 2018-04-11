package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ShowScores;
import life.plank.juna.zone.view.holder.ShowScoreHeaderHolder;
import life.plank.juna.zone.view.holder.ShowScoresHolder;

/**
 * Created by plank-sharath on 4/10/2018.
 */
public class ShowScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_VIEW_HEADER = 0;
    private final int ITEM_VIEW_BODY = 1;
    private Context context;
    private List<ShowScores> showScores = new ArrayList<>();
    private String header = "header";

    public ShowScoreAdapter(Context context) {
        this.context = context;
        showScores.addAll(ShowScores.getScores(context));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_VIEW_HEADER ? new ShowScoreHeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_scores_header_row, parent, false)) : new ShowScoresHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_scores_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShowScoreHeaderHolder) {
            ShowScoreHeaderHolder showScoreHeaderHolder = (ShowScoreHeaderHolder) holder;
            showScoreHeaderHolder.headerTextView.setText(showScores.get(position).getHeader());
        } else {
            ShowScoresHolder showScoresHolder = (ShowScoresHolder) holder;
            if (showScores.get(position).getRounds().contentEquals(" ")) {
                showScoresHolder.roundsTextview.setVisibility(View.GONE);
            } else {
                showScoresHolder.roundsTextview.setVisibility(View.VISIBLE);
                showScoresHolder.roundsTextview.setText(showScores.get(position).getRounds());
            }
            showScoresHolder.teamOneTextview.setText(showScores.get(position).getTeamOne());
            showScoresHolder.teamTwoTextview.setText(showScores.get(position).getTeamTwo());
            showScoresHolder.teamOneScore.setText(showScores.get(position).getTeamOneScores());
            showScoresHolder.teamTwoScore.setText(showScores.get(position).getTeamTwoScore());
            showScoresHolder.time.setText(showScores.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return showScores.size();
    }

    @Override
    public int getItemViewType(int position) {
        return showScores.get(position).getTag().contentEquals(header) ? ITEM_VIEW_HEADER : ITEM_VIEW_BODY;
    }
}
