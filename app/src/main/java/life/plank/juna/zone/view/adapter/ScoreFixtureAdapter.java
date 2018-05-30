package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixtureModel;
import life.plank.juna.zone.util.DateUtil;
import life.plank.juna.zone.view.holder.ScoreFixtureHolder;

/**
 * Created by plank-sharath on 2/21/2018.
 */

public class ScoreFixtureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ScoreFixtureModel> scoreFixtureModelList;

    public ScoreFixtureAdapter(Context context, List<ScoreFixtureModel> scoreFixtureModelList) {
        this.context = context;
        this.scoreFixtureModelList = scoreFixtureModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ScoreFixtureHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scores_fixture_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScoreFixtureHolder scoreFixtureHolder = (ScoreFixtureHolder) holder;
        scoreFixtureHolder.teamOneTextview.setText(scoreFixtureModelList.get(position).getHomeTeam().getName());
        scoreFixtureHolder.teamTwoTextview.setText(scoreFixtureModelList.get(position).getAwayTeam().getName());
        // Todo: Use a better format
        scoreFixtureHolder.timeTextview.setText( DateUtil.ISO_DATE_FORMAT.format( scoreFixtureModelList.get(position).getMatchStartTime()));
    }

    @Override
    public int getItemCount() {
        return scoreFixtureModelList.size();
    }

}
