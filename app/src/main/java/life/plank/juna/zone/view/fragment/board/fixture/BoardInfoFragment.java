package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Commentary;
import life.plank.juna.zone.data.network.model.Highlights;
import life.plank.juna.zone.data.network.model.Lineups;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.MatchTeamStats;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.util.customview.CommentarySmall;
import life.plank.juna.zone.util.customview.CommentarySmall.CommentarySmallListener;
import life.plank.juna.zone.util.customview.LineupLayout;
import life.plank.juna.zone.util.customview.MatchHighlights;
import life.plank.juna.zone.util.customview.MatchTeamStatsLayout;
import life.plank.juna.zone.view.activity.CommentaryActivity;
import life.plank.juna.zone.view.adapter.CommentaryAdapter;
import life.plank.juna.zone.view.adapter.SubstitutionAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.COMMENTARY_DATA;
import static life.plank.juna.zone.util.AppConstants.LINEUPS_DATA;
import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.DataUtil.extractSubstitutionEvents;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class BoardInfoFragment extends Fragment implements CommentarySmallListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();
    private static final String MATCH_ID = "match_id";
    private static final String HOME_LOGO = "home_logo";
    private static final String VISITING_LOGO = "visiting_logo";
    public static final String HOME_TEAM_NAME = "home_team_name";
    public static final String VISITING_TEAM_NAME = "visiting_team_name";

    @BindView(R.id.item_match_highlights)
    MatchHighlights matchHighlightsLayout;
    @BindView(R.id.commentary_small)
    CommentarySmall commentarySmall;
    @BindView(R.id.team_stats)
    MatchTeamStatsLayout matchTeamStatsLayout;
    @BindView(R.id.item_line_up)
    LineupLayout lineupLayout;

    @Inject
    @Named("footballData")
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;
    private long matchId;
    private String homeLogo;
    private String visitingLogo;
    private String homeTeamName;
    private String visitingTeamName;

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance(long matchId, String homeLogo, String visitingLogo, String homeTeamName, String visitingTeamName) {
        BoardInfoFragment fragment = new BoardInfoFragment();
        Bundle args = new Bundle();
        args.putLong(MATCH_ID, matchId);
        args.putString(HOME_LOGO, homeLogo);
        args.putString(VISITING_LOGO, visitingLogo);
        args.putString(HOME_TEAM_NAME, homeTeamName);
        args.putString(VISITING_TEAM_NAME, visitingTeamName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            matchId = args.getLong(MATCH_ID);
            homeLogo = args.getString(HOME_LOGO);
            visitingLogo = args.getString(VISITING_LOGO);
            homeTeamName = args.getString(HOME_TEAM_NAME);
            visitingTeamName = args.getString(VISITING_TEAM_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);

        getMatchHighlights();
        getCommentaries();
        getMatchTeamStats();
        getLineupFormation();
        getMatchEvents();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        commentarySmall.initListeners(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        commentarySmall.dispose();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void seeAllClicked() {
        if (!isNullOrEmpty(commentarySmall.getCommentaryList())) {
            if (boardParentViewBitmap == null) {
                boardParentViewBitmap = loadBitmap(getActivity().getWindow().getDecorView(), getActivity().getWindow().getDecorView(), getActivity());
            }
            CommentaryActivity.launch(getActivity(), commentarySmall.getRootLayout(), gson.toJson(commentarySmall.getCommentaryList()));
        }
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        List<MatchEvent> substitutionEventList;
        switch (zoneLiveData.getLiveDataType()) {
            case COMMENTARY_DATA:
                commentarySmall.updateNew(zoneLiveData.getCommentaryList());
                break;
            case MATCH_EVENTS:
                substitutionEventList = extractSubstitutionEvents(zoneLiveData.getMatchEventList());
                lineupLayout.updateSubstitutions(substitutionEventList);
                break;
            case LINEUPS_DATA:
                getLineupFormation();
                break;
            default:
                break;
        }
    }

    private void getMatchEvents() {
        lineupLayout.setAdapter(new SubstitutionAdapter());
        restApi.getMatchEvents(matchId)
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
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<MatchEvent> matchEvents = response.body();
                                if (!isNullOrEmpty(matchEvents)) {
                                    lineupLayout.updateEvents(matchEvents);
                                } else
                                    lineupLayout.onMatchYetToStart();
                                break;
                            default:
                                lineupLayout.onMatchYetToStart();
                                break;
                        }
                    }
                });
    }

    public void getMatchHighlights() {
        restApi.getMatchHighlights(matchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<Highlights>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getMatchHighlights() : Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Response<List<Highlights>> response) {
                        List<Highlights> highlights = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (!isNullOrEmpty(highlights) && highlights.get(0).getHighlightsUrl() != null) {
                                    matchHighlightsLayout.setVisibility(View.VISIBLE);
                                    matchHighlightsLayout.setHighlights(picasso, highlights.get(0));
                                } else
                                    matchHighlightsLayout.setVisibility(View.GONE);
                                break;
                            default:
                                matchHighlightsLayout.setVisibility(View.GONE);
                                break;
                        }
                    }
                });
    }

    private void getMatchTeamStats() {
        restApi.getTeamStatsForMatch(matchId)
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
                        matchTeamStatsLayout.notAvailable(R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<MatchTeamStats> response) {
                        MatchTeamStats matchTeamStats = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (matchTeamStats != null) {
                                    matchTeamStatsLayout.update(matchTeamStats, homeLogo, visitingLogo, picasso);
                                } else
                                    matchTeamStatsLayout.notAvailable(R.string.match_yet_to_start);
                                break;
                            default:
                                matchTeamStatsLayout.notAvailable(R.string.match_yet_to_start);
                                break;
                        }
                    }
                });
    }

    private void getCommentaries() {
        commentarySmall.setAdapter(new CommentaryAdapter());
        restApi.getCommentaries(matchId)
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
                        commentarySmall.notAvailable(R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<Commentary>> response) {
                        List<Commentary> commentaryList = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (!isNullOrEmpty(commentaryList)) {
                                    commentarySmall.updateAdapter(commentaryList);
                                } else
                                    commentarySmall.notAvailable(R.string.match_yet_to_start);
                                break;
                            default:
                                commentarySmall.notAvailable(R.string.something_went_wrong);
                                break;
                        }
                    }
                });
    }

    private void getLineupFormation() {
        restApi.getLineUpsData(matchId)
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
                        lineupLayout.notAvailable(R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<Lineups> response) {
                        Lineups lineups = response.body();
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (lineups != null) {
                                    lineupLayout.update(lineups, homeLogo, visitingLogo, picasso, homeTeamName, visitingTeamName);
                                } else
                                    lineupLayout.notAvailable(R.string.match_yet_to_start);
                                break;
                            default:
                                lineupLayout.notAvailable(R.string.line_ups_not_available);
                                break;
                        }
                    }
                });
    }
}