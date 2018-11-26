package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Pair;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.RestApiAggregator;
import life.plank.juna.zone.data.model.Commentary;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.FixtureListUpdateTask;
import life.plank.juna.zone.util.facilis.BaseCard;
import life.plank.juna.zone.view.adapter.board.match.BoardInfoAdapter;
import life.plank.juna.zone.view.fragment.base.BaseBoardFragment;
import life.plank.juna.zone.view.fragment.football.LeagueInfoDetailPopup;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.COMMENTARY_DATA;
import static life.plank.juna.zone.util.AppConstants.HIGHLIGHTS_DATA;
import static life.plank.juna.zone.util.AppConstants.LINEUPS_DATA;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.MATCH_STATS_DATA;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.AppConstants.STANDINGS;
import static life.plank.juna.zone.util.AppConstants.TIME_STATUS_DATA;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;
import static life.plank.juna.zone.util.DateUtil.getTimeDiffFromNow;

public class BoardInfoFragment extends BaseBoardFragment implements BoardInfoAdapter.BoardInfoAdapterListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_board_info)
    RecyclerView boardInfoRecyclerView;

    @Inject
    RestApi restApi;
    @Inject
    Gson gson;

    private MatchDetails matchDetails;
    private BoardInfoAdapter adapter;
    private long timeDiffOfMatchFromNow;

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance(String matchDetailsObjectString) {
        BoardInfoFragment fragment = new BoardInfoFragment();
        Bundle args = new Bundle();
        args.putString(ZoneApplication.getContext().getString(R.string.intent_board), matchDetailsObjectString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            matchDetails = gson.fromJson(args.getString(getString(R.string.intent_board)), MatchDetails.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        timeDiffOfMatchFromNow = getTimeDiffFromNow(matchDetails.getMatchStartTime());
        prepareRecyclerView();
        return rootView;
    }

    private void prepareRecyclerView() {
        if (adapter != null && boardInfoRecyclerView.getAdapter() != null) {
            adapter = null;
            boardInfoRecyclerView.setAdapter(null);
        }
        adapter = new BoardInfoAdapter(matchDetails, Glide.with(this), getActivity(), this, false);
        boardInfoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBoardInfoData(false);
        swipeRefreshLayout.setOnRefreshListener(() -> getBoardInfoData(true));
    }

    private void getBoardInfoData(boolean isRefreshing) {
        if (timeDiffOfMatchFromNow > 0) {
            if (matchDetails.getLeague() != null) {
                getPreMatchData(
                        matchDetails.getLeague(),
                        Objects.requireNonNull(matchDetails.getHomeTeam().getName()),
                        Objects.requireNonNull(matchDetails.getAwayTeam().getName())
                );
            }
        } else {
            getPostMatchData(isRefreshing);
        }
    }

    @Override
    public void onScrubberClick(View view) {
        if (getParentFragment() instanceof BaseCard && matchDetails != null && !isNullOrEmpty(matchDetails.getMatchEvents())) {
            ((BaseCard) getParentFragment()).pushPopup(TimelinePopup.Companion.newInstance(matchDetails.getMatchId(), (ArrayList<MatchEvent>) matchDetails.getMatchEvents(), matchDetails));
        } else
            Toast.makeText(getContext(), R.string.no_match_events_yet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentarySeeAllClick(View fromView) {
        if (getParentFragment() instanceof BaseCard && !isNullOrEmpty(matchDetails.getCommentary())) {
            ((BaseCard) getParentFragment()).pushPopup(CommentaryPopup.Companion.newInstance((ArrayList<Commentary>) matchDetails.getCommentary()));
        }
    }

    @Override
    public void onSeeAllStandingsClick(View fromView) {
        if (getParentFragment() instanceof BaseCard && !isNullOrEmpty(matchDetails.getStandingsList())) {
            ((BaseCard) getParentFragment()).pushPopup(LeagueInfoDetailPopup.Companion.newInstance(STANDINGS, (ArrayList<? extends Parcelable>) matchDetails.getStandingsList()));
        }
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                LiveScoreData scoreData = zoneLiveData.getScoreData(gson);
                updateScoreLocally(matchDetails, scoreData);
                FixtureListUpdateTask.update(matchDetails, scoreData, null, true);
                break;
            case TIME_STATUS_DATA:
                LiveTimeStatus timeStatus = zoneLiveData.getLiveTimeStatus(gson);
                updateTimeStatusLocally(matchDetails, timeStatus);
                FixtureListUpdateTask.update(matchDetails, null, timeStatus, false);
                break;
            case COMMENTARY_DATA:
                adapter.updateCommentaries(zoneLiveData.getCommentaryList(gson), false);
                break;
            case MATCH_EVENTS:
                adapter.updateMatchEventsAndSubstitutions(zoneLiveData.getMatchEventList(gson), false);
                break;
            case LINEUPS_DATA:
                getLineupFormation();
                break;
            case MATCH_STATS_DATA:
                adapter.updateMatchStats(zoneLiveData.getMatchStats(gson), 0);
                break;
            case HIGHLIGHTS_DATA:
                adapter.updateHighlights(zoneLiveData.getHighlightsList(gson), false);
                break;
            default:
                break;
        }
    }

    @Override
    public void handlePreMatchData(@org.jetbrains.annotations.Nullable Pair<? extends List<Standings>, ? extends List<TeamStats>> pair) {
        if (pair != null) {
            matchDetails.setStandingsList(pair.getFirst());
            matchDetails.setTeamStatsList(pair.getSecond());
        }
        if (adapter != null) adapter.setPreMatchData();
    }

    private void getPostMatchData(boolean isRefreshing) {
        RestApiAggregator.getPostMatchBoardData(matchDetails, restApi)
                .doOnSubscribe(() -> {
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(true);
                })
                .doOnTerminate(() -> {
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(new Subscriber<MatchDetails>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted : getPostMatchData");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getPostMatchData : " + e.getMessage());
                        if (adapter != null) {
                            adapter.setMatchStats();
                            adapter.setLineups();
                        }
                    }

                    @Override
                    public void onNext(MatchDetails matchDetails) {
                        if (adapter != null) {
                            adapter.setMatchStats();
                            adapter.setLineups();
                        }
                    }
                });
    }

    /**
     * Method for fetching lineups live. invoked by receiving the ZoneLiveData's lineup broadcast
     */
    private void getLineupFormation() {
        restApi.getLineUpsData(matchDetails.getMatchId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<Lineups>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("", "onError: " + e);
                    }

                    @Override
                    public void onNext(Response<Lineups> response) {
                        Lineups lineups = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
//                                Updating the adapter only if live lineups fetch is successful
                                if (lineups != null && adapter != null) {
                                    prepareRecyclerView();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    public void onDetach() {
        adapter = null;
        super.onDetach();
    }
}