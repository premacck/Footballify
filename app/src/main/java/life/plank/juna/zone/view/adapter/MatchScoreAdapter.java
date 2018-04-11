package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.MatchScore;
import life.plank.juna.zone.view.holder.MatchScoreHeaderHolder;
import life.plank.juna.zone.view.holder.MatchScoreHolder;

/**
 * Created by plank-sharath on 4/10/2018.
 */
public class MatchScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_VIEW_HEADER = 0;
    private final int ITEM_VIEW_BODY = 1;
    private Context context;
    private List<MatchScore> matchScores = new ArrayList<>();
    private String header = "header";

    public MatchScoreAdapter(Context context) {
        this.context = context;
        matchScores.addAll(MatchScore.getScores(context));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_VIEW_HEADER ? new MatchScoreHeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_scores_header_row, parent, false)) : new MatchScoreHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_scores_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MatchScoreHeaderHolder) {
            MatchScoreHeaderHolder matchScoreHeaderHolder = (MatchScoreHeaderHolder) holder;
            matchScoreHeaderHolder.headerTextView.setText(matchScores.get(position).getHeader());
        } else {
            MatchScoreHolder matchScoreHolder = (MatchScoreHolder) holder;
            if (matchScores.get(position).getRounds().contentEquals(" ")) {
                matchScoreHolder.roundsTextview.setVisibility(View.GONE);
            } else {
                matchScoreHolder.roundsTextview.setVisibility(View.VISIBLE);
                matchScoreHolder.roundsTextview.setText(matchScores.get(position).getRounds());
            }
            matchScoreHolder.homeTeamTextview.setText(matchScores.get(position).gethomeTeam());
            matchScoreHolder.visitingTeamTextview.setText(matchScores.get(position).getvisitingTeam());
            matchScoreHolder.homeTeamScore.setText(matchScores.get(position).gethomeTeamScores());
            matchScoreHolder.visitingTeamScore.setText(matchScores.get(position).getvisitingTeamScore());
            matchScoreHolder.matchTime.setText(matchScores.get(position).getmatchTime());
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
