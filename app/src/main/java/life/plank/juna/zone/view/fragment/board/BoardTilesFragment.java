package life.plank.juna.zone.view.fragment.board;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static life.plank.juna.zone.util.PreferenceManager.getToken;
import static life.plank.juna.zone.util.UIDisplayUtil.loadBitmap;
import static life.plank.juna.zone.view.activity.BoardActivity.boardParentViewBitmap;

public class BoardTilesFragment extends Fragment implements OnClickFeedItemListener {

    private static final String TAG = BoardTilesFragment.class.getSimpleName();
    private static final String BOARD_ID = "board_id";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.board_tiles_list)
    RecyclerView boardTilesList;

    @Inject
    Picasso picasso;
    @Inject
    @Named("default")
    RestApi restApi;
    private BoardMediaAdapter adapter;

    private String boardId;

    public BoardTilesFragment() {
    }

    public static BoardTilesFragment newInstance(String boardId) {
        BoardTilesFragment fragment = new BoardTilesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOARD_ID, boardId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            boardId = bundle.getString(BOARD_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_tiles, container, false);
        ButterKnife.bind(this, rootView);

        ZoneApplication.getApplication().getUiComponent().inject(this);
        initRecyclerView();
        retrieveBoardByBoardId();
        return rootView;
    }

    private void initRecyclerView() {
        adapter = new BoardMediaAdapter(picasso);
        adapter.setOnClickFeedItemListener(this);
        boardTilesList.setAdapter(adapter);
    }

    public void updateNewPost(FootballFeed footballFeed) {
        adapter.updateNewPost(footballFeed);
        boardTilesList.smoothScrollToPosition(0);
    }

    public void retrieveBoardByBoardId() {
        progressBar.setVisibility(View.VISIBLE);
        restApi.retrieveByBoardId(boardId, getToken(ZoneApplication.getContext()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<FootballFeed>>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "On Error()" + e);
                        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Response<List<FootballFeed>> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:
                                if (response.body() != null)
                                    adapter.update(response.body());
                                else
                                    Toast.makeText(getContext(), R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                            case HttpURLConnection.HTTP_NOT_FOUND:
                                Toast.makeText(getContext(), R.string.board_not_populated, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), R.string.failed_to_retrieve_board, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
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