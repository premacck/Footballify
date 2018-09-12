package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.model.FixtureByMatchDay;
import life.plank.juna.zone.view.adapter.FixtureMatchdayAdapter;

import static life.plank.juna.zone.util.AppConstants.PAST_MATCHES;
import static life.plank.juna.zone.util.AppConstants.TODAY_MATCHES;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.LeagueInfoActivity.fixtureByMatchDayList;
import static life.plank.juna.zone.view.activity.LeagueInfoActivity.matchStatsParentViewBitmap;

public class FixtureActivity extends AppCompatActivity {

    @BindView(R.id.root_layout)
    FrameLayout rootLayout;
    @BindView(R.id.fixtures_section_list)
    RecyclerView fixtureRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data)
    TextView noMatchesView;
    @BindView(R.id.header)
    TextView headerView;

    @Inject
    public Picasso picasso;
    @Inject
    public Gson gson;
    public boolean isCup;

    private FixtureMatchdayAdapter fixtureMatchdayAdapter;

    public static void launch(Activity packageContext, View fromView, boolean isCup) {
        Intent intent = new Intent(packageContext, FixtureActivity.class);
        intent.putExtra(packageContext.getString(R.string.intent_is_cup), isCup);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(packageContext, Pair.create(fromView, "match_fixture_result"));
        packageContext.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_and_result);
        ButterKnife.bind(this);
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerView);

        rootLayout.setBackground(new BitmapDrawable(getResources(), matchStatsParentViewBitmap));

        ((ZoneApplication) getApplication()).getUiComponent().inject(this);

        Intent intent = getIntent();
        if (intent == null) {
            onNoMatchesFound();
            return;
        }
        isCup = intent.getBooleanExtra(getString(R.string.intent_is_cup), false);

        UpdateAdapterTask.parse(this);
        prepareRecyclerView();
    }

    public void prepareRecyclerView() {
        fixtureMatchdayAdapter = new FixtureMatchdayAdapter(this);
        fixtureRecyclerView.setAdapter(fixtureMatchdayAdapter);
    }

    private void onNoMatchesFound() {
        noMatchesView.setVisibility(View.VISIBLE);
        fixtureRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        fixtureMatchdayAdapter = null;
        super.onDestroy();
    }

    private static class UpdateAdapterTask extends AsyncTask<Intent, Void, Void> {

        private WeakReference<FixtureActivity> ref;
        private int recyclerViewScrollIndex = 0;

        private static void parse(FixtureActivity activity) {
            new UpdateAdapterTask(activity).execute(activity.getIntent());
        }

        private UpdateAdapterTask(FixtureActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            ref.get().progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            if (!isNullOrEmpty(fixtureByMatchDayList)) {
                for (FixtureByMatchDay matchDay : fixtureByMatchDayList) {
                    if (Objects.equals(matchDay.getDaySection(), PAST_MATCHES) || Objects.equals(matchDay.getDaySection(), TODAY_MATCHES)) {
                        recyclerViewScrollIndex = fixtureByMatchDayList.indexOf(matchDay);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (ref.get() != null) {
                if (!isNullOrEmpty(fixtureByMatchDayList) && ref.get().fixtureMatchdayAdapter != null) {
                    ref.get().fixtureMatchdayAdapter.update(fixtureByMatchDayList);
                }
                ref.get().progressBar.setVisibility(View.GONE);
                ref.get().fixtureRecyclerView.scrollToPosition(recyclerViewScrollIndex);
            }
        }
    }
}