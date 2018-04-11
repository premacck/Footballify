package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchScores;
import life.plank.juna.zone.view.holder.MatchScoreHeaderHolder;
import life.plank.juna.zone.view.holder.MatchScoresHolder;

/**
 * Created by plank-sharath on 4/10/2018.
 */
public class MatchScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_VIEW_HEADER = 0;
    private final int ITEM_VIEW_BODY = 1;
    private Context context;
    private List<MatchScores> matchScores = new ArrayList<>();
    private String header = "header";

    public MatchScoreAdapter(Context context) {
        this.context = context;
        matchScores.addAll(MatchScores.getScores(context));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_VIEW_HEADER ? new MatchScoreHeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_scores_header_row, parent, false)) : new MatchScoresHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_scores_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchScoreHeaderHolder) {
            MatchScoreHeaderHolder matchScoreHeaderHolder = (MatchScoreHeaderHolder) holder;
            matchScoreHeaderHolder.headerTextView.setText(matchScores.get(position).getHeader());
        } else {
            MatchScoresHolder matchScoresHolder = (MatchScoresHolder) holder;
            if (matchScores.get(position).getRounds().contentEquals(" ")) {
                matchScoresHolder.roundsTextview.setVisibility(View.GONE);
            } else {
                matchScoresHolder.roundsTextview.setVisibility(View.VISIBLE);
                matchScoresHolder.roundsTextview.setText(matchScores.get(position).getRounds());
            }
            matchScoresHolder.teamOneTextview.setText(matchScores.get(position).getTeamOne());
            matchScoresHolder.teamTwoTextview.setText(matchScores.get(position).getTeamTwo());
            matchScoresHolder.teamOneScore.setText(matchScores.get(position).getTeamOneScores());
            matchScoresHolder.teamTwoScore.setText(matchScores.get(position).getTeamTwoScore());
            matchScoresHolder.time.setText(matchScores.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return matchScores.size();
    }

    @Override
    public int getItemViewType(int position) {
        return matchScores.get(position).getTag().contentEquals(header) ? ITEM_VIEW_HEADER : ITEM_VIEW_BODY;
    }
}
