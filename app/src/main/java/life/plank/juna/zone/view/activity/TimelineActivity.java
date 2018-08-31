package life.plank.juna.zone.view.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.LiveTimeStatus;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.view.adapter.TimelineAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.FT;
import static life.plank.juna.zone.util.AppConstants.HT;
import static life.plank.juna.zone.util.AppConstants.LIVE;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.TIME_STATUS;
import static life.plank.juna.zone.util.DataUtil.getZoneLiveData;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.setSharedElementTransitionDuration;
import static life.plank.juna.zone.util.UIDisplayUtil.setupSwipeGesture;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class TimelineActivity extends AppCompatActivity {

    private static final String TAG = TimelineActivity.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.header)
    TextView headerView;
    @BindView(R.id.timeline_recycler_view)
    RecyclerView timelineRecyclerView;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Gson gson;

    private long currentMatchId;
    private TimelineAdapter adapter;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(getString(R.string.intent_zone_live_data))) {
                setZoneLiveData(intent);
            }
        }
    };

    public static void launch(Activity packageContext, View fromView, long currentMatchId) {
        Intent intent = new Intent(packageContext, TimelineActivity.class);
        intent.putExtra(packageContext.getString(R.string.match_id_string), currentMatchId);
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
        setupSwipeGesture(this, headerView);

        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), boardParentViewBitmap));

        Intent intent = getIntent();
        if (intent != null) {
            currentMatchId = intent.getLongExtra(getString(R.string.match_id_string), 0);
            FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
        }
        initRecyclerView();
        getTimeLineEvents();
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
        super.onDestroy();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getString(R.string.pref_football_match_sub) + currentMatchId);
    }

    private void initRecyclerView() {
        adapter = new TimelineAdapter(this);
        timelineRecyclerView.setAdapter(adapter);
    }

    private void setZoneLiveData(Intent intent) {
        ZoneLiveData zoneLiveData = getZoneLiveData(intent, getString(R.string.intent_zone_live_data), gson);
        switch (zoneLiveData.getLiveDataType()) {
            case MATCH_EVENTS:
                List<MatchEvent> matchEventList = zoneLiveData.getMatchEventList();
                if (adapter != null && !isNullOrEmpty(matchEventList)) {
                    adapter.updateLiveEvents(matchEventList);
                    timelineRecyclerView.smoothScrollToPosition(0);
                }
                break;
            case TIME_STATUS:
                LiveTimeStatus timeStatus = zoneLiveData.getLiveTimeStatus();
                if ((Objects.equals(timeStatus.getTimeStatus(), LIVE) && timeStatus.getMinute() == 0) ||
                        Objects.equals(timeStatus.getTimeStatus(), HT) ||
                        Objects.equals(timeStatus.getTimeStatus(), FT)) {
                    if (adapter != null) {
                        adapter.updateWhistleEvent(timeStatus);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void getTimeLineEvents() {
        restApi.getMatchEvents(currentMatchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<MatchEvent>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getMatchEvents : Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<MatchEvent>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            List<MatchEvent> matchEvents = response.body();
                            if (!isNullOrEmpty(matchEvents)) {
                                adapter.updateEvents(matchEvents);
                            } else
                                Toast.makeText(TimelineActivity.this, R.string.no_match_events_yet, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getLiveTimeStatus() {
        restApi.getLiveTimeStatus(currentMatchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<LiveTimeStatus>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getLiveTimeStatus() :Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<List<LiveTimeStatus>> response) {
                        List<LiveTimeStatus> timeStatusList = response.body();
                        if (response.code() == HttpURLConnection.HTTP_OK && !isNullOrEmpty(timeStatusList)) {
//                            TODO : integrate whistle time events with match events in the adapter.
                        }
                    }
                });
    }
}