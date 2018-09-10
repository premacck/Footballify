package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchFixture;
import life.plank.juna.zone.data.network.model.MatchTeamStats;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.util.customview.CommentarySmall.CommentarySmallListener;
import life.plank.juna.zone.util.customview.ScrubberLayout.ScrubberLayoutListener;
import life.plank.juna.zone.view.activity.CommentaryActivity;
import life.plank.juna.zone.view.activity.TimelineActivity;
import life.plank.juna.zone.view.adapter.BoardInfoAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.COMMENTARY_DATA;
import static life.plank.juna.zone.util.AppConstants.LINEUPS_DATA;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class BoardInfoFragment extends Fragment implements CommentarySmallListener, ScrubberLayoutListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();

    @BindView(R.id.list_board_info)
    RecyclerView boardInfoRecyclerView;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;
    private MatchFixture fixture;
    private BoardInfoAdapter adapter;

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance(MatchFixture fixture, Gson gson) {
        BoardInfoFragment fragment = new BoardInfoFragment();
        Bundle args = new Bundle();
        args.putString(ZoneApplication.getContext().getString(R.string.intent_board), gson.toJson(fixture));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            fixture = gson.fromJson(args.getString(getString(R.string.intent_board)), MatchFixture.class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new BoardInfoAdapter(this, getContext(), picasso, true, fixture);
        boardInfoRecyclerView.setAdapter(adapter);

        getCommentaries();
        getMatchTeamStats();
        getLineupFormation();
        getMatchEvents();

        return rootView;
    }

    @Override
    public void onScrubberClick(View view) {
        if (boardParentViewBitmap == null) {
            boardParentViewBitmap = loadBitmap(Objects.requireNonNull(getActivity()).getWindow().getDecorView(), getActivity().getWindow().getDecorView(), getContext());
        }
        TimelineActivity.launch(getActivity(), view, fixture.getForeignId());
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void seeAllClicked(View view) {
        if (!isNullOrEmpty(adapter.getCommentaryList())) {
            if (boardParentViewBitmap == null) {
                boardParentViewBitmap = loadBitmap(getActivity().getWindow().getDecorView(), getActivity().getWindow().getDecorView(), getActivity());
            }
            CommentaryActivity.launch(getActivity(), view, gson.toJson(adapter.getCommentaryList()));
        }
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case COMMENTARY_DATA:
                adapter.setCommentaries(zoneLiveData.getCommentaryList(), false);
                break;
            case MATCH_EVENTS:
                adapter.setMatchEvents(zoneLiveData.getMatchEventList(), false);
                break;
            case LINEUPS_DATA:
                getLineupFormation();
                break;
            default:
                break;
        }
    }

    private void getMatchEvents() {
        restApi.getMatchEvents(fixture.getForeignId())
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
                        adapter.setMatchEvents(null, true);
                    }

                    @Override
                    public void onNext(Response<List<MatchEvent>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<MatchEvent> matchEvents = response.body();
                                if (!isNullOrEmpty(matchEvents)) {
                                    adapter.setMatchEvents(matchEvents, false);
                                } else
                                    adapter.setMatchEvents(null, true);
                                break;
                            default:
                                adapter.setMatchEvents(null, true);
                                break;
                        }
                    }
                });
    }

    private void getMatchTeamStats() {
        restApi.getTeamStatsForMatch(fixture.getForeignId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MatchTeamStats>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        adapter.setMatchTeamStats(null, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<MatchTeamStats> response) {
                        MatchTeamStats matchTeamStats = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (matchTeamStats != null) {
                                    adapter.setMatchTeamStats(matchTeamStats, 0);
                                } else
                                    adapter.setMatchTeamStats(null, R.string.match_yet_to_start);
                                break;
                            default:
                                adapter.setMatchTeamStats(null, R.string.something_went_wrong);
                                break;
                        }
                    }
                });
    }

    private void getCommentaries() {
        restApi.getCommentaries(fixture.getForeignId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Commentary>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getCommentaries() : Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        adapter.setCommentaries(null, true);
                    }

                    @Override
                    public void onNext(Response<List<Commentary>> response) {
                        List<Commentary> commentaryList = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (!isNullOrEmpty(commentaryList)) {
                                    adapter.setCommentaries(commentaryList, false);
                                } else
                                    adapter.setCommentaries(null, true);
                                break;
                            default:
                                adapter.setCommentaries(null, true);
                                break;
                        }
                    }
                });
    }

    private void getLineupFormation() {
        restApi.getLineUpsData(fixture.getForeignId())
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
                        adapter.setLineups(null, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<Lineups> response) {
                        Lineups lineups = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (lineups != null) {
                                    adapter.setLineups(lineups, 0);
                                } else
                                    adapter.setLineups(null, R.string.match_yet_to_start);
                                break;
                            default:
                                adapter.setLineups(null, R.string.line_ups_not_available);
                                break;
                        }
                    }
                });
    }
}