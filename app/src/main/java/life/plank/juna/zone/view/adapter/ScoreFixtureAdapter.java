package life.plank.juna.zone.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.ScoreFixture;
import life.plank.juna.zone.view.holder.ScoreFixtureHeaderHolder;
import life.plank.juna.zone.view.holder.ScoreFixtureHolder;

/**
 * Created by plank-sharath on 2/21/2018.
 */

public class ScoreFixtureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ITEM_VIEW_HEADER = 0;
    private final int ITEM_VIEW_BODY = 1;
    private Context context;
    private List<ScoreFixture> scoreFixtures = new ArrayList<>();
    private String header = "header";

    public ScoreFixtureAdapter(Context context) {
        this.context = context;
        scoreFixtures.addAll(ScoreFixture.getFixture(context));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_VIEW_HEADER ? new ScoreFixtureHeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_fixture_header_row, parent, false)) : new ScoreFixtureHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scores_fixture_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScoreFixtureHeaderHolder) {
            ScoreFixtureHeaderHolder scoreFixtureHeaderHolder = (ScoreFixtureHeaderHolder) holder;
            scoreFixtureHeaderHolder.headerTextView.setText(scoreFixtures.get(position).getHeader());
        } else {
            ScoreFixtureHolder scoreFixtureHolder = (ScoreFixtureHolder) holder;
            if (scoreFixtures.get(position).getRounds().contentEquals(" ")) {
                scoreFixtureHolder.roundsTextview.setVisibility(View.GONE);
            } else {
                scoreFixtureHolder.roundsTextview.setVisibility(View.VISIBLE);
                scoreFixtureHolder.roundsTextview.setText(scoreFixtures.get(position).getRounds());
            }
            scoreFixtureHolder.teamOneTextview.setText(scoreFixtures.get(position).getTeamOne());
            scoreFixtureHolder.teamTwoTextview.setText(scoreFixtures.get(position).getTeamTwo());
            scoreFixtureHolder.timeTextview.setText(scoreFixtures.get(position).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return scoreFixtures.size();
    }

    @Override
    public int getItemViewType(int position) {
        return scoreFixtures.get(position).getTag().contentEquals(header) ? ITEM_VIEW_HEADER : ITEM_VIEW_BODY;
    }
}
