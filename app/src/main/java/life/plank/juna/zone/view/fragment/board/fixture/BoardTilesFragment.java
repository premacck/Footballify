package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bvapp.arcmenulibrary.ArcMenu;
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
import life.plank.juna.zone.data.model.FeedEntry;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.interfaces.OnClickFeedItemListener;
import life.plank.juna.zone.util.BoomMenuUtil;
import life.plank.juna.zone.view.activity.MatchBoardActivity;
import life.plank.juna.zone.view.activity.base.BaseBoardActivity;
import life.plank.juna.zone.view.activity.post.PostDetailActivity;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static life.plank.juna.zone.util.AppConstants.BoomMenuPage.BOOM_MENU_FULL;
import static life.plank.juna.zone.util.DataUtil.isNullOrEmpty;
import static life.plank.juna.zone.util.PreferenceManager.getToken;

public class BoardTilesFragment extends Fragment implements OnClickFeedItemListener {

    private static final String TAG = BoardTilesFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.board_tiles_list)
    RecyclerView boardTilesRecyclerView;
    @BindView(R.id.no_data)
    TextView noDataTextView;
    @BindView(R.id.arc_menu)
    ArcMenu arcMenu;

    @Inject
    public
    Picasso picasso;
    @Inject
    Gson gson;
    @Inject
    @Named("default")
    RestApi restApi;
    @Inject
    PagerSnapHelper pagerSnapHelper;
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

        if (isNullOrEmpty(boardId)) {
            noDataTextView.setText(R.string.login_signup_to_view_feed);
            noDataTextView.setVisibility(View.VISIBLE);
            boardTilesRecyclerView.setVisibility(View.GONE);
            arcMenu.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            return rootView;
        }
        initRecyclerViews();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNullOrEmpty(boardId)) {
            return;
        }
        if (isBoardActive) {
            BoomMenuUtil.setupBoomMenu(BOOM_MENU_FULL, Objects.requireNonNull(getActivity()), boardId, arcMenu);
        } else {
            arcMenu.setOnClickListener((view1) -> Toast.makeText(getContext(), R.string.board_not_active, Toast.LENGTH_SHORT).show());
        }
        swipeRefreshLayout.setOnRefreshListener(() -> getBoardFeed(true));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNullOrEmpty(boardId)) {
            return;
        }
        getBoardFeed(false);
    }

    private void initRecyclerViews() {
        adapter = new BoardMediaAdapter(this);
        adapter.setOnClickFeedItemListener(this);
        boardTilesRecyclerView.setAdapter(adapter);
    }

    public void updateNewPost(FeedEntry feedItem) {
        if (adapter.getBoardFeed().isEmpty()) {
            updateUi(true, 0);
        }
        adapter.updateNewPost(feedItem);
        boardTilesRecyclerView.smoothScrollToPosition(0);
    }

    public void getBoardFeed(boolean isRefreshing) {
        restApi.getBoardFeedItems(boardId, getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(true);
                })
                .doOnTerminate(() -> {
                    progressBar.setVisibility(View.GONE);
                    if (isRefreshing) swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(new Subscriber<Response<List<FeedEntry>>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: getBoardFeed()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "On Error() : getBoardFeed() " + e);
                        updateUi(false, R.string.something_went_wrong);
                    }

                    @Override
                    public void onNext(Response<List<FeedEntry>> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                List<FeedEntry> feedItemList = response.body();
                                if (!isNullOrEmpty(feedItemList)) {
                                    updateUi(true, 0);
                                    adapter.update(feedItemList);
                                    if (getActivity() instanceof BaseBoardActivity) {
                                        ((BaseBoardActivity) getActivity()).updateFullScreenAdapter(feedItemList);
                                    }
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
        boardTilesRecyclerView.setVisibility(isDataAvailable ? View.VISIBLE : View.GONE);
        noDataTextView.setVisibility(isDataAvailable ? View.GONE : View.VISIBLE);
        if (noDataTextView.getText().toString().isEmpty() && message != 0) {
            noDataTextView.setText(message);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (!isNullOrEmpty(adapter.getBoardFeed()) && getActivity() instanceof MatchBoardActivity) {
            PostDetailActivity.launch(getActivity(), adapter.getBoardFeed(), boardId, position, ((MatchBoardActivity) getActivity()).getScreenshotLayout());
        }
    }

    @Override
    public void onItemLongClick(int position) {
        if (getActivity() instanceof BaseBoardActivity) {
            ((BaseBoardActivity) getActivity()).setBlurBackgroundAndShowFullScreenTiles(true, position);
        }
    }

    public void moveItem(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }
}