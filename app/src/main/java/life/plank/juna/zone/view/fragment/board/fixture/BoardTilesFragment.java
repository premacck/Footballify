package life.plank.juna.zone.view.fragment.board.fixture;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bvapp.arcmenulibrary.ArcMenu;
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
import life.plank.juna.zone.data.network.model.FootballFeed;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.view.activity.BoardFeedDetailActivity;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.getSpannedString;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.util.UIDisplayUtil.setupBoomMenu;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class BoardTilesFragment extends Fragment implements OnClickFeedItemListener {

    private static final String TAG = BoardTilesFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.board_tiles_list)
    RecyclerView boardTilesRecyclerView;
    @BindView(R.id.no_data)
    TextView noDataTextView;
    @BindView(R.id.board_arc_menu)
    ArcMenu arcMenu;

    @Inject
    public
    Picasso picasso;
    @Inject
    @Named("default")
    RestApi restApi;
    private BoardMediaAdapter adapter;

    private String boardId;
    private boolean isBoardActive;

    public BoardTilesFragment() {
    }

    public static BoardTilesFragment newInstance(String boardId, boolean isBoardActive) {
        BoardTilesFragment fragment = new BoardTilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ZoneApplication.getContext().getString(R.string.intent_board_id), boardId);
        bundle.putBoolean(ZoneApplication.getContext().getString(R.string.intent_is_board_active), isBoardActive);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            boardId = bundle.getString(getString(R.string.intent_board_id));
            isBoardActive = bundle.getBoolean(getString(R.string.intent_is_board_active));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_tiles, container, false);
        ButterKnife.bind(this, rootView);

        ZoneApplication.getApplication().getUiComponent().inject(this);
        initRecyclerView();
        retrieveBoardByBoardId();
        if (isBoardActive) {
            setupBoomMenu(getActivity(), arcMenu, boardId);
        }
        return rootView;
    }

    private void initRecyclerView() {
        adapter = new BoardMediaAdapter(this);
        adapter.setOnClickFeedItemListener(this);
        boardTilesRecyclerView.setAdapter(adapter);
    }

    public void updateNewPost(FootballFeed footballFeed) {
        if (adapter.getBoardFeed().isEmpty()) {
            updateUi(true, 0);
        }
        adapter.updateNewPost(footballFeed);
        boardTilesRecyclerView.smoothScrollToPosition(0);
    }

    public void retrieveBoardByBoardId() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.retrieveByBoardId(boardId, getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error()" + e);
                        updateUi(false, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<FootballFeed> footballFeedList = response.body();
                                if (!isNullOrEmpty(footballFeedList)) {
                                    updateUi(true, 0);
                                    adapter.update(footballFeedList);
                                } else
                                    updateUi(false, R.string.board_yet_to_be_populated);
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                updateUi(false, R.string.board_yet_to_be_populated);
                                break;
                            default:
                                updateUi(false, R.string.failed_to_retrieve_board);
                                break;
                        }
                    }
                });
    }

    public void updateUi(boolean isDataAvailable, @StringRes int message) {
        progressBar.setVisibility(View.GONE);
        boardTilesRecyclerView.setVisibility(isDataAvailable ? View.VISIBLE : View.GONE);
        noDataTextView.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
        if (noDataTextView.getText().toString().isEmpty() && message != 0) {
            noDataTextView.setText(getSpannedString(getContext(), message));
        }
    }

    @Override
    public void onItemClick(int position, View fromView) {
        Activity boardActivity = getActivity();
        if (boardActivity != null) {
            boardParentViewBitmap = loadBitmap(boardActivity.getWindow().getDecorView(), boardActivity.getWindow().getDecorView(), boardActivity);
        }
        BoardFeedDetailActivity.launch(getActivity(), position, adapter.getBoardFeed(), boardId, fromView);
    }
}