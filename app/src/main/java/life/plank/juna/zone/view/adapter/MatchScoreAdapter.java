package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.view.holder.MatchScoreHolder;

/**
 * Created by plank-sharath on 4/10/2018.
 */
public class MatchScoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ScoreFixtureModel> scoreFixtureModelList;
    private Context context;

    public MatchScoreAdapter(Context context, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.context = context;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MatchScoreHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_scores_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MatchScoreHolder matchScoreHolder = (MatchScoreHolder) holder;
        matchScoreHolder.homeTeamScore.setText(String.valueOf(scoreFixtureModelList.get(position).getHomeTeamScore()));
        matchScoreHolder.visitingTeamScore.setText(String.valueOf(scoreFixtureModelList.get(position).getAwayTeamScore()));
        matchScoreHolder.matchTime.setText(scoreFixtureModelList.get(position).getTimeStatus());
    }

    @Override
    public int getItemCount() {
        return scoreFixtureModelList.size();
    }
}
