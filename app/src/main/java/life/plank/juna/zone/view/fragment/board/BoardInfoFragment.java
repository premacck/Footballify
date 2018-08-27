package life.plank.juna.zone.view.fragment.board;

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
import life.plank.juna.zone.data.network.model.LiveScoreData;
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
import retrofit2.Response;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class BoardInfoFragment extends Fragment implements CommentarySmallListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();
    private static final String MATCH_ID = "match_id";
    private static final String HOME_LOGO = "home_logo";
    private static final String VISITING_LOGO = "visiting_logo";

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

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance(long matchId, String homeLogo, String visitingLogo) {
        BoardInfoFragment fragment = new BoardInfoFragment();
        Bundle args = new Bundle();
        args.putLong(MATCH_ID, matchId);
        args.putString(HOME_LOGO, homeLogo);
        args.putString(VISITING_LOGO, visitingLogo);
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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);

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

    @Override
    public void seeAllClicked() {
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        LiveScoreData liveScoreData;
        List<MatchEvent> matchEventList;
        switch (zoneLiveData.getLiveDataType()) {
            case SCORE_DATA:
                liveScoreData = zoneLiveData.getScoreData();
                break;
            case MATCH_EVENTS:
                matchEventList = zoneLiveData.getMatchEventList();
                break;
            default:
                break;
        }
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
                        if (response.code() == HttpURLConnection.HTTP_OK && highlights != null) {
                            if (!isNullOrEmpty(highlights) && highlights.get(0).getHighlightsUrl() != null) {
                                matchHighlightsLayout.setVisibility(View.VISIBLE);
                                matchHighlightsLayout.setHighlights(highlights.get(0));
//                                TODO : prepare ExoPlayer.
                            } else {
                                matchHighlightsLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}