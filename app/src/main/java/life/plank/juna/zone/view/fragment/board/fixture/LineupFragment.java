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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import kotlin.Pair;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Lineups;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.Standings;
import life.plank.juna.zone.data.model.TeamStats;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.board.match.LineupAdapter;
import life.plank.juna.zone.view.fragment.base.BaseBoardFragment;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.LINEUPS_DATA;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.DataUtil.findString;
import static life.plank.juna.zone.util.RestUtilKt.errorToast;

public class LineupFragment extends BaseBoardFragment {

    private static final String TAG = LineupFragment.class.getSimpleName();

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
    private LineupAdapter adapter;

    public LineupFragment() {
    }

    public static LineupFragment newInstance(MatchDetails matchDetails) {
        LineupFragment fragment = new LineupFragment();
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
        adapter = new LineupAdapter(matchDetails, picasso, getActivity());
        boardInfoRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLineupFormation(false);
        swipeRefreshLayout.setOnRefreshListener(() -> getLineupFormation(true));
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case MATCH_EVENTS:
                adapter.updateMatchEventsAndSubstitutions(zoneLiveData.getMatchEventList(gson), false);
                break;
            case LINEUPS_DATA:
                getLineupFormation(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void handlePreMatchData(@org.jetbrains.annotations.Nullable Pair<? extends List<Standings>, ? extends List<TeamStats>> pair) {
    }

    /**
     * Method for fetching lineups live. invoked by receiving the ZoneLiveData's lineup broadcast
     */
    private void getLineupFormation(boolean isRefreshing) {
        restApi.getLineUpsData(matchDetails.getMatchId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> {if (isRefreshing) swipeRefreshLayout.setRefreshing(false);})
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
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                Lineups lineups = response.body();
//                                Updating the adapter only if live lineups fetch is successful
                                if (lineups != null && adapter != null) {
                                    matchDetails.setLineups(lineups);
                                    adapter.setLineups();
                                }
                                break;
                            default:
                                errorToast(R.string.line_ups_not_available, response);
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