package life.plank.juna.zone.view.fragment.board;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.util.customview.CommentarySmall;
import life.plank.juna.zone.util.customview.CommentarySmall.CommentarySmallListener;

public class BoardInfoFragment extends Fragment implements CommentarySmallListener {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.commentary_small)
    CommentarySmall commentarySmall;

    public BoardInfoFragment() {
    }

    public static BoardInfoFragment newInstance() {
        return new BoardInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);

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
}