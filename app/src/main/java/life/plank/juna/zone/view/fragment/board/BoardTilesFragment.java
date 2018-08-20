package life.plank.juna.zone.view.fragment.board;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.BoardMediaAdapter;

public class BoardTilesFragment extends Fragment {

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

        return rootView;
    }
}