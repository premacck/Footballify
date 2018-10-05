package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.PlayerStatsAdapter;
import life.plank.juna.zone.view.adapter.StandingTableAdapter;
import life.plank.juna.zone.view.adapter.TeamStatsAdapter;

import static life.plank.juna.zone.util.AppConstants.PLAYER_STATS;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TEAM_STATS;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.LeagueInfoActivity.matchStatsParentViewBitmap;

public class LeagueInfoDetailActivity extends AppCompatActivity {

    private static final String TAG = LeagueInfoDetailActivity.class.getSimpleName();
    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;

    @BindView(R.id.root_card)
    CardView rootCard;
    @BindView(R.id.standing_recycler_view)
    RecyclerView standingRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.standing_header_layout)
    RelativeLayout standingsHeader;
    @BindView(R.id.team_stats_header_layout)
    RelativeLayout teamStatsHeader;
    @BindView(R.id.player_stats_header)
    RelativeLayout playerStatsHeader;
    @BindView(R.id.header)
    TextView headerTextView;
    @BindView(R.id.no_data)
    TextView noDataTextView;

    private StandingTableAdapter standingTableAdapter;
    private TeamStatsAdapter teamStatsAdapter;
    private PlayerStatsAdapter playerStatsAdapter;

    public static void launch(Activity fromActivity, String viewToLoad, ArrayList<? extends Parcelable> list, View fromView) {
        Intent intent = new Intent(fromActivity, LeagueInfoDetailActivity.class);
        intent.putParcelableArrayListExtra(fromActivity.getString(R.string.intent_list), list);
        intent.putExtra(fromActivity.getString(R.string.intent_load_view), viewToLoad);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(fromActivity, Pair.create(fromView, fromActivity.getString(R.string.pref_match)));
        fromActivity.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_result_detail);
        ButterKnife.bind(this);
        ((ZoneApplication) getApplication()).getUiComponent().inject(this);
        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerTextView, rootCard);

        Intent intent = getIntent();
        if (intent != null) {
            String viewToLoad = intent.getStringExtra(getString(R.string.intent_load_view));
            headerTextView.setText(viewToLoad);
            switch (viewToLoad) {
                case STANDINGS:
                    standingTableAdapter = new StandingTableAdapter(picasso);
                    prepareRecyclerView(standingTableAdapter);
                    standingTableAdapter.update(intent.getParcelableArrayListExtra(getString(R.string.intent_list)));
                    toggleStatsHeaderVisibility(LinearLayout.VISIBLE, LinearLayout.GONE, LinearLayout.GONE);
                    break;
                case TEAM_STATS:
                    teamStatsAdapter = new TeamStatsAdapter(picasso);
                    prepareRecyclerView(teamStatsAdapter);
                    teamStatsAdapter.update(intent.getParcelableArrayListExtra(getString(R.string.intent_list)));
                    toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.VISIBLE, LinearLayout.GONE);
                    break;
                case PLAYER_STATS:
                    playerStatsAdapter = new PlayerStatsAdapter();
                    prepareRecyclerView(playerStatsAdapter);
                    playerStatsAdapter.update(intent.getParcelableArrayListExtra(getString(R.string.intent_list)));
                    toggleStatsHeaderVisibility(LinearLayout.GONE, LinearLayout.GONE, LinearLayout.VISIBLE);
                    break;
            }
        }
    }

    private void toggleStatsHeaderVisibility(int standingsHeaderVisibility, int teamStatsHeaderVisibility, int playerStatsHeaderVisibility) {
        standingsHeader.setVisibility(standingsHeaderVisibility);
        teamStatsHeader.setVisibility(teamStatsHeaderVisibility);
        playerStatsHeader.setVisibility(playerStatsHeaderVisibility);
    }

    private void prepareRecyclerView(RecyclerView.Adapter adapter) {
        standingRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        standingTableAdapter = null;
        teamStatsAdapter = null;
        playerStatsAdapter = null;
        super.onDestroy();
    }
}