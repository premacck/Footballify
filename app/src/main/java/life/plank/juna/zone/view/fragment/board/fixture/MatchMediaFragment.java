package life.plank.juna.zone.view.fragment.board.fixture;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import life.plank.juna.zone.R;
import life.plank.juna.zone.ZoneApplication;
import life.plank.juna.zone.data.model.Highlights;
import life.plank.juna.zone.data.model.MatchDetails;
import life.plank.juna.zone.data.model.ZoneLiveData;
import life.plank.juna.zone.data.network.interfaces.RestApi;
import life.plank.juna.zone.view.adapter.board.match.BoardMediaAdapter;
import life.plank.juna.zone.view.fragment.base.BaseFragment;

import static life.plank.juna.zone.util.AppConstants.HIGHLIGHTS_DATA;
import static life.plank.juna.zone.util.DataUtil.findString;
import static life.plank.juna.zone.util.UIDisplayUtil.getScreenSize;

public class MatchMediaFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list_board_info)
    RecyclerView boardInfoRecyclerView;

    @Inject
    RestApi restApi;
    @Inject
    Gson gson;
    @Inject
    Picasso picasso;

    private MatchDetails matchDetails;
    private BoardMediaAdapter adapter;

    public MatchMediaFragment() {
    }

    public static MatchMediaFragment newInstance(MatchDetails matchDetails) {
        MatchMediaFragment fragment = new MatchMediaFragment();
        Bundle args = new Bundle();
        args.putParcelable(findString(R.string.match_id_string), matchDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZoneApplication.getApplication().getUiComponent().inject(this);
        Bundle args = getArguments();
        if (args != null) {
            matchDetails = args.getParcelable(getString(R.string.match_id_string));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board_info, container, false);
        ButterKnife.bind(this, rootView);
        prepareRecyclerView();
        return rootView;
    }

    private void prepareRecyclerView() {
        adapter = new BoardMediaAdapter(matchDetails, getScreenSize(getActivity())[0]);
        boardInfoRecyclerView.setAdapter(adapter);
    }

    public void updateZoneLiveData(ZoneLiveData zoneLiveData) {
        switch (zoneLiveData.getLiveDataType()) {
            case HIGHLIGHTS_DATA:
                List<Highlights> highlightsList = zoneLiveData.getHighlightsList(gson);
                if (highlightsList != null) {
                    adapter.updateHighlights(highlightsList, false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        adapter = null;
        super.onDetach();
    }
}