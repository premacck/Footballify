package life.plank.juna.zone.view.fragment.board;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.data.network.model.Highlights;
import life.plank.juna.zone.data.network.model.LiveScoreData;
import life.plank.juna.zone.data.network.model.MatchEvent;
import life.plank.juna.zone.data.network.model.ZoneLiveData;
import life.plank.juna.zone.util.customview.CommentarySmall;
import life.plank.juna.zone.util.customview.CommentarySmall.CommentarySmallListener;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.MATCH_EVENTS;
import static life.plank.juna.zone.util.AppConstants.SCORE_DATA;

public class BoardInfoFragment extends Fragment implements CommentarySmallListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();
    private static final String MATCH_ID = "match_id";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.commentary_small)
    CommentarySmall commentarySmall;

    @Inject
    @Named("footballData")
    RestApi restApi;
    private long matchId;

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance(long matchId) {
        BoardInfoFragment fragment = new BoardInfoFragment();
        Bundle args = new Bundle();
        args.putLong(MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            matchId = args.getLong(MATCH_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        progressBar.setVisibility(View.INVISIBLE);
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
                liveScoreData = zoneLiveData.getLiveScoreData();
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
                .subscribe(new Observer<Response<Highlights>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getMatchHighlights() : Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Response<Highlights> response) {
                        Highlights highlights = response.body();
                        if (response.code() == HttpURLConnection.HTTP_OK && highlights != null) {
                            if (highlights.getHighlightsUrl() != null) {
//                                TODO : make highlights VISIBLE and prepare ExoPlayer.
                            } else {
//                                TODO : make highlights layout GONE.
                            }
                        }
                    }
                });
    }
}