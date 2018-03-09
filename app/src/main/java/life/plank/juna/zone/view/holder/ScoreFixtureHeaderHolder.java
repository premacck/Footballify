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

public class ScoreFixtureHeaderHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.score_fixture_header_text_view)
    public TextView headerTextView;

    public ScoreFixtureHeaderHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
