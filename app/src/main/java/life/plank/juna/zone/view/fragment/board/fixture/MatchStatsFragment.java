package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

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
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.MatchStatsListener;
import life.plank.juna.zone.util.facilis.BaseCard;
import life.plank.juna.zone.view.adapter.board.match.MatchStatsAdapter;
import life.plank.juna.zone.view.fragment.base.BaseBoardFragment;
import rx.Subscriber;

import static life.plank.juna.zone.util.common.AppConstants.COMMENTARY_DATA;
import static life.plank.juna.zone.util.common.AppConstants.MATCH_STATS_DATA;
import static life.plank.juna.zone.util.common.DataUtil.findString;
import static life.plank.juna.zone.util.common.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.time.DateUtil.getTimeDiffFromNow;

public class MatchStatsFragment extends BaseBoardFragment implements MatchStatsListener {

    private static final String TAG = MatchStatsFragment.class.getSimpleName();

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_board_info)
    RecyclerView boardInfoRecyclerView;

    @Inject
    RestApi restApi;
    @Inject
    Gson gson;

    private MatchDetails matchDetails;
    private MatchStatsAdapter adapter;

    public MatchStatsFragment() {
    }

    public static MatchStatsFragment newInstance(MatchDetails matchDetails) {
        MatchStatsFragment fragment = new MatchStatsFragment();
        Bundle args = new Bundle();
        args.putParcelable(findString(R.string.match_id_string), matchDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            matchDetails = args.getParcelable(getString(R.string.match_id_string));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        prepareRecyclerView();
        return rootView;
    }

    private void prepareRecyclerView() {
        if (adapter != null && boardInfoRecyclerView.getAdapter() != null) {
            adapter = null;
            boardInfoRecyclerView.setAdapter(null);
        }
        adapter = new MatchStatsAdapter(matchDetails, Glide.with(this), this);
        boardInfoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBoardInfoData(false);
        swipeRefreshLayout.setOnRefreshListener(() -> getBoardInfoData(true));
    }

    private void getBoardInfoData(boolean isRefreshing) {
        long timeDiffOfMatchFromNow = getTimeDiffFromNow(matchDetails.getMatchStartTime());
        if (timeDiffOfMatchFromNow > 0) {
            if (matchDetails.getLeague() != null) {
                if (isRefreshing) swipeRefreshLayout.setRefreshing(false);
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCommentarySeeAllClick(@NonNull View fromView) {
        if (getParentFragment() instanceof BaseCard && !isNullOrEmpty(matchDetails.getCommentary())) {
            ((BaseCard) getParentFragment()).pushPopup(CommentaryPopup.Companion.newInstance((ArrayList<Commentary>) matchDetails.getCommentary()));
        }
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case COMMENTARY_DATA:
                List<Commentary> commentaryList = zoneLiveData.getCommentaryList(gson);
                if (commentaryList != null) {
                    adapter.updateCommentaries(commentaryList, false);
                }
                break;
            case MATCH_STATS_DATA:
                adapter.updateMatchStats(zoneLiveData.getMatchStats(gson), 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void handlePreMatchData(@org.jetbrains.annotations.Nullable Pair<? extends List<Standings>, ? extends List<TeamStats>> pair) {
        if (adapter != null && pair != null) {
            adapter.setPreMatchData(pair.getFirst(), pair.getSecond());
        }
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
                        }
                    }

                    @Override
                    public void onNext(MatchDetails matchDetails) {
                        if (adapter != null) {
                            adapter.setMatchStats();
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