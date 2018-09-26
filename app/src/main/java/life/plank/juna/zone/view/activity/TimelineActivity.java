package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.LiveScoreData;
import life.plank.juna.zone.data.network.model.LiveTimeStatus;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.util.DataUtil.ScrubberLoader;
import life.plank.juna.zone.util.FixtureListUpdateTask;
import life.plank.juna.zone.view.adapter.TimelineAdapter;

import static life.plank.juna.zone.util.AppConstants.FT;
import static life.plank.juna.zone.util.AppConstants.HT;
import static life.plank.juna.zone.util.AppConstants.LIVE;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.AppConstants.TIME_STATUS;
import static life.plank.juna.zone.util.DataUtil.getDisplayTimeStatus;
import static life.plank.juna.zone.util.DataUtil.getSeparator;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;
import static life.plank.juna.zone.util.DateUtil.getTimelineDateHeader;
import static life.plank.juna.zone.util.UIDisplayUtil.getDp;
import static life.plank.juna.zone.util.UIDisplayUtil.getEndDrawableTarget;
import static life.plank.juna.zone.util.UIDisplayUtil.getStartDrawableTarget;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.base.BaseBoardActivity.boardParentViewBitmap;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.timeline_header_layout)
    RelativeLayout headerLayout;
    @BindView(R.id.venue_name)
    TextView venueNameTextView;
    @BindView(R.id.time_status)
    TextView timeStatusTextView;
    @BindView(R.id.score)
    TextView scoreTextView;
    @BindView(R.id.win_pointer)
    ImageView winPointer;
    @BindView(R.id.date)
    TextView dateTextView;
    @BindView(R.id.timeline_recycler_view)
    RecyclerView timelineRecyclerView;
    @BindView(R.id.scrubber)
    LineChart scrubber;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Picasso picasso;
    @Inject
    Gson gson;

    private long currentMatchId;
    private TimelineAdapter adapter;
    private List<MatchEvent> matchEvents;
    private MatchFixture fixture;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent);
            }
        }
    };

    public static void launch(Activity packageContext, View fromView, long currentMatchId, String matchEvents, String matchFixture) {
        Intent intent = new Intent(packageContext, TimelineActivity.class);
        intent.putExtra(packageContext.getString(R.string.match_id_string), currentMatchId);
        intent.putExtra(packageContext.getString(R.string.intent_match_event_list), matchEvents);
        intent.putExtra(packageContext.getString(R.string.intent_match_fixture), matchFixture);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(packageContext, Pair.create(fromView, packageContext.getString(R.string.timeline_activity)));
        packageContext.startActivity(intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        ZoneApplication.getApplication().getUiComponent().inject(this);
        setSharedElementTransitionDuration(this, getResources().getInteger(R.integer.shared_element_animation_duration));
        setupSwipeGesture(this, headerLayout);

        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), boardParentViewBitmap));

        Intent intent = getIntent();
        if (intent != null) {
            currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
            FixtureAndMatchEventParser.parse(this);
        }
        initRecyclerView();
    }

    private void prepareViews() {
        venueNameTextView.setText(fixture.getVenue() != null ? fixture.getVenue().getName() : null);
        timeStatusTextView.setText(getDisplayTimeStatus(fixture.getTimeStatus()));
        dateTextView.setText(getTimelineDateHeader(fixture.getMatchStartTime()));
        scoreTextView.setText(getSeparator(fixture, winPointer, false));

        picasso.load(fixture.getHomeTeam().getLogoLink())
                .resize((int) getDp(24), (int) getDp(24))
                .into(getEndDrawableTarget(timeStatusTextView));
        picasso.load(fixture.getAwayTeam().getLogoLink())
                .resize((int) getDp(24), (int) getDp(24))
                .into(getStartDrawableTarget(dateTextView));
        ScrubberLoader.prepare(scrubber, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mMessageReceiver, new IntentFilter(getString(R.string.intent_board)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        adapter = null;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
        super.onDestroy();
    }

    private void initRecyclerView() {
        adapter = new TimelineAdapter(this);
        timelineRecyclerView.setAdapter(adapter);
    }

    private void setZoneLiveData(Intent intent) {
        ZoneLiveData zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson);
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                LiveScoreData scoreData = zoneLiveData.getScoreData(gson);
                updateScoreLocally(fixture, scoreData);
                FixtureListUpdateTask.update(fixture, scoreData, null, true);
                scoreTextView.setText(getSeparator(fixture, winPointer, false));
                break;
            case MATCH_EVENTS:
                List<MatchEvent> matchEventList = zoneLiveData.getMatchEventList(gson);
                if (adapter != null && !isNullOrEmpty(matchEventList)) {
                    adapter.updateLiveEvents(matchEventList);
                    timelineRecyclerView.smoothScrollToPosition(0);
                }
                break;
            case TIME_STATUS:
                LiveTimeStatus timeStatus = zoneLiveData.getLiveTimeStatus(gson);
                if ((Objects.equals(timeStatus.getTimeStatus(), LIVE) && timeStatus.getMinute() == 0) ||
                        Objects.equals(timeStatus.getTimeStatus(), HT) ||
                        Objects.equals(timeStatus.getTimeStatus(), FT)) {
                    if (adapter != null) {
                        adapter.updateWhistleEvent(timeStatus);
                    }
                }
                updateTimeStatusLocally(fixture, timeStatus);
                FixtureListUpdateTask.update(fixture, null, timeStatus, false);
                timeStatusTextView.setText(getDisplayTimeStatus(fixture.getTimeStatus()));
                break;
            default:
                break;
        }
    }

    static class FixtureAndMatchEventParser extends AsyncTask<Intent, Void, Void> {

        private WeakReference<TimelineActivity> ref;

        static void parse(TimelineActivity activity) {
            new FixtureAndMatchEventParser(activity).execute(activity.getIntent());
        }

        private FixtureAndMatchEventParser(TimelineActivity activity) {
            this.ref = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            ref.get().matchEvents = ref.get().gson.fromJson(
                    intents[0].getStringExtra(ref.get().getString(R.string.intent_match_event_list)),
                    new TypeToken<List<MatchEvent>>() {
                    }.getType()
            );
            ref.get().fixture = ref.get().gson.fromJson(
                    intents[0].getStringExtra(ref.get().getString(R.string.intent_match_fixture)),
                    MatchFixture.class
            );
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ref.get().adapter.updateEvents(ref.get().matchEvents);
            ref.get().prepareViews();
            FirebaseMessaging.getInstance().subscribeToTopic(ref.get().getString(R.string.pref_football_match_sub) + ref.get().currentMatchId);
            ref.get().progressBar.setVisibility(View.GONE);
        }
    }
}