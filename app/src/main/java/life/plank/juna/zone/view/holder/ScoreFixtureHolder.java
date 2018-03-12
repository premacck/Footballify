package life.plank.juna.zone.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

/**
 * Created by plank-sharath on 2/21/2018.
 */

public class ScoreFixtureHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.score_fixture_rounds_textview)
    public TextView roundsTextview;
    @BindView(R.id.score_fixture_team_one)
    public TextView teamOneTextview;
    @BindView(R.id.score_fixture_team_two)
    public TextView teamTwoTextview;
    @BindView(R.id.score_fixture_time_text_view)
    public TextView timeTextview;

    public ScoreFixtureHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
