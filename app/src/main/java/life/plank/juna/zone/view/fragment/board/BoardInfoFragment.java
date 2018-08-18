package life.plank.juna.zone.view.fragment.board;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class BoardInfoFragment extends Fragment {

    private static final String TAG = BoardInfoFragment.class.getSimpleName();

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.home_team_lineup_layout)
    LinearLayout homeTeamLineupLayout;
    @BindView(R.id.visiting_team_lineup_layout)
    LinearLayout visitingTeamLineupLayout;

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
}