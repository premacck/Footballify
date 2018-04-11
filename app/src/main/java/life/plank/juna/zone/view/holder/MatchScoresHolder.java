package life.plank.juna.zone.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-sharath on 4/10/2018.
 */

public class MatchScoresHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.show_scores_rounds)
    public TextView roundsTextview;
    @BindView(R.id.home_team)
    public TextView homeTeamTextview;
    @BindView(R.id.visiting_team)
    public TextView visitingTeamTextview;
    @BindView(R.id.home_team_score)
    public TextView homeTeamScore;
    @BindView(R.id.visiting_team_score)
    public TextView visitingTeamScore;
    @BindView(R.id.match_time)
    public TextView matchTime;

    public MatchScoresHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
