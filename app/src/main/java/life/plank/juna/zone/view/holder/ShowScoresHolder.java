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

public class ShowScoresHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.show_scores_rounds)
    public TextView roundsTextview;
    @BindView(R.id.first_team)
    public TextView teamOneTextview;
    @BindView(R.id.second_team)
    public TextView teamTwoTextview;
    @BindView(R.id.first_team_score)
    public TextView teamOneScore;
    @BindView(R.id.second_team_score)
    public TextView teamTwoScore;
    @BindView(R.id.match_time)
    public TextView time;

    public ShowScoresHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
