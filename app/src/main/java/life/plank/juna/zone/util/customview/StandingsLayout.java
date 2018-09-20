package life.plank.juna.zone.util.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.model.StandingModel;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;

import static life.plank.juna.zone.util.UIDisplayUtil.getDp;

public class StandingsLayout extends FrameLayout {

    @BindView(R.id.standing_header_layout)
    RelativeLayout standingsHeader;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.no_standings)
    TextView noStandings;
    @BindView(R.id.see_all_standings)
    TextView seeAllStandings;
    @BindView(R.id.standings_progress_bar)
    ProgressBar standingsProgressBar;

    private StandingTableAdapter adapter;

    public StandingsLayout(@NonNull Context context) {
        this(context, null);
    }

    public StandingsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StandingsLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.item_standings, this);
        ButterKnife.bind(this, rootView);
        seeAllStandings.setVisibility(GONE);
    }

    public void setAdapter(StandingTableAdapter adapter) {
        this.adapter = adapter;
        standingRecyclerView.setAdapter(this.adapter);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) standingRecyclerView.getLayoutParams();
        params.height = (int) getDp(65);
    }

    public void update(List<StandingModel> standingModelList) {
        adapter.update(standingModelList);
        standingsProgressBar.setVisibility(GONE);
    }

    public void setLoading(boolean isLoading) {
        standingsProgressBar.setVisibility(isLoading ? VISIBLE : GONE);
        standingsHeader.setVisibility(isLoading ? INVISIBLE : VISIBLE);
        standingRecyclerView.setVisibility(isLoading ? INVISIBLE : VISIBLE);
        seeAllStandings.setVisibility(isLoading ? INVISIBLE : VISIBLE);
    }

    public void notAvailable(@StringRes int message) {
        noStandings.setVisibility(VISIBLE);
        noStandings.setText(message);
        standingRecyclerView.setVisibility(INVISIBLE);
    }
}