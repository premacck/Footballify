package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Pair;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.LiveScoreData;
import life.plank.juna.zone.data.model.LiveTimeStatus;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.MatchEvent;
import life.plank.juna.zone.data.model.MatchFixture;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.util.FixtureListUpdateTask;
import life.plank.juna.zone.util.facilis.BaseCard;
import life.plank.juna.zone.view.adapter.board.info.BoardInfoAdapter;
import life.plank.juna.zone.view.adapter.board.info.BoardMediaAdapter;
import life.plank.juna.zone.view.fragment.base.BaseBoardFragment;

import static life.plank.juna.zone.util.AppConstants.HIGHLIGHTS_DATA;
import static life.plank.juna.zone.util.AppConstants.MATCH_STATS_DATA;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.AppConstants.TIME_STATUS_DATA;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.DataUtil.updateScoreLocally;
import static life.plank.juna.zone.util.DataUtil.updateTimeStatusLocally;
import static life.plank.juna.zone.util.DateUtil.getTimeDiffFromNow;

public class MatchMediaFragment extends BaseBoardFragment implements BoardInfoAdapter.BoardInfoAdapterListener {

    private static final String TAG = MatchMediaFragment.class.getSimpleName();

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_board_info)
    RecyclerView boardInfoRecyclerView;

    @Inject
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;

    private MatchDetails matchDetails;
    private BoardMediaAdapter adapter;
    private long timeDiffOfMatchFromNow;

    public MatchMediaFragment() {
    }

    public static MatchMediaFragment newInstance(String matchDetailsObjectString) {
        MatchMediaFragment fragment = new MatchMediaFragment();
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
        adapter = new BoardMediaAdapter(matchDetails, getActivity(), this::onScrubberClick);
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
    }

    @Override
    public void onSeeAllStandingsClick(View fromView) {
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                LiveScoreData scoreData = zoneLiveData.getScoreData(gson);
                updateScoreLocally(matchDetails, scoreData);
                FixtureListUpdateTask.update(MatchFixture.Companion.from(matchDetails), scoreData, null, true);
                break;
            case TIME_STATUS_DATA:
                LiveTimeStatus timeStatus = zoneLiveData.getLiveTimeStatus(gson);
                updateTimeStatusLocally(matchDetails, timeStatus);
                FixtureListUpdateTask.update(MatchFixture.Companion.from(matchDetails), null, timeStatus, false);
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
    }

    @Override
    public void onDetach() {
        adapter = null;
        super.onDetach();
    }
}