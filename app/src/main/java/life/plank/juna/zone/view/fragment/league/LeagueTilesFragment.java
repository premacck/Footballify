package life.plank.juna.zone.view.fragment.league;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import life.plank.juna.zone.R;

public class LeagueTilesFragment extends Fragment {

    public LeagueTilesFragment() {
    }

    public static LeagueTilesFragment newInstance() {
        LeagueTilesFragment fragment = new LeagueTilesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_league_tiles, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}